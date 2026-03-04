from fastapi import FastAPI
import uvicorn
import cv2
from cv2.typing import MatLike
import torch

import app.cv2_common as cv2_common
from app.yolo_scorebox_classifier import ScoreboxDetectorClassifier
from app.fencer_pose import FencerPoseClassifier
from app.nn_pose_classifier import SimpleNNClassifier
from app.data_models import ScoringEvent, ScoreBoutRequest


### Constants
# In a later version, these should be environmental variables 
# Instead of being included in the container, we will retrieve them from S3 based on model versions.
SCOREBOX_MODEL_PATH = 'trained_models/scorebox_detect/scorebox_detect.pt'
FENCER_POSE_MODEL_PATH = 'trained_models/fencer_keypoint/fencer_keypoint.pt'
POSE_CLASSIFIER_MODEL_PATH = 'trained_models/pose_classifier/pose_classifier.pth'
POINT_DECISION_TREE_PATH = None # 'trained_models/point_decider/point_decider.pkl'

NO_POINT = 0
POINT_LEFT = 1
POINT_RIGHT = 2

POINT_DICT = {
    NO_POINT: "No Point",
    POINT_LEFT: "Left Fencer",
    POINT_RIGHT: "Right Fencer"
}

POSES = ["En Garde", "Lunge", "Parry", "None"]
MODEL_VERSION = "v0.1"

### Inference Methods

def interim_point_decider(left_pose, right_pose, left_movement, right_movement) -> int:
    # Logic Table for determining the point (preferably will be changed to a DecisionTree in the future)
    
    # First check if one of the fencers is retreating
    if left_movement < -10:
        return POINT_RIGHT
    elif right_movement < -10:
        return POINT_LEFT
    
    if left_pose == right_pose:
        if left_movement > right_movement:
            return POINT_LEFT
        elif right_movement > left_movement:
            return POINT_RIGHT          
    elif left_pose == 1 and right_pose == 0:
        return POINT_LEFT
    elif left_pose == 0 and right_pose == 1:
        return POINT_RIGHT

    return NO_POINT

def point_decider(left_pose, right_pose, left_movement, right_movement) -> int:
    # Stub for point decider, will be replaced by a DecisionTree model in the future
    pass

def normalize_keypoints_to_bbox(img: MatLike, keypoints: list[tuple], bbox: tuple, imgsize=(640, 640)) -> list[tuple]:
    """
    Normalize keypoints to a bounding box.
    
    Parameters:
        keypoints (list of tuples): List of keypoints [(x, y), ...], un-normalized.
        bbox (tuple): Bounding box in (x1 y1 x2 y2), un-normalized.
    
    Returns:
        list of tuples: Keypoints normalized to the bounding box.
    """
    h, w, _ = img.shape
    
    x1, y1, x2, y2 = bbox
    
    x_min = x1 / w
    x_max = x2 / w
    y_min = y1 / h
    y_max = y2 / h
    
    width = x_max - x_min
    height = y_max - y_min
    
    normalized_keypoints = []

    for x, y in keypoints:
        # print("Bbox:", x_min, y_min, width, height)
        x_normalized = ((x/imgsize[0])-x_min) / width
        y_normalized = ((y/imgsize[1])-y_min) / height
        normalized_keypoints.extend([x_normalized, y_normalized])
    
    return normalized_keypoints

def determine_point(frame, pose_classifier: SimpleNNClassifier, point_decider, scorebox_classification, fencer_boxes_left, fencer_boxes_right, left_keypoints, right_keypoints, left_movement, right_movement) -> int:
    if fencer_boxes_left == [] or fencer_boxes_right == [] or left_keypoints == [] or right_keypoints == []:    
        return NO_POINT
    # Logic Table for determining the point
    if scorebox_classification == cv2_common.LEFT_SIDE:
        return POINT_LEFT
    elif scorebox_classification == cv2_common.RIGHT_SIDE:
        return POINT_RIGHT
    else:
        left_keypoints = normalize_keypoints_to_bbox(frame, left_keypoints[0], fencer_boxes_left[0][0])
        right_keypoints = normalize_keypoints_to_bbox(frame, right_keypoints[0], fencer_boxes_right[0][0])

        left_pose_probs = pose_classifier.predict_probs(torch.as_tensor(left_keypoints, device=pose_classifier.device).unsqueeze(0))
        right_pose_probs = pose_classifier.predict_probs(torch.as_tensor(right_keypoints, device=pose_classifier.device).unsqueeze(0))
        
        print("Left Pose Max: ", torch.max(left_pose_probs).item(), "Left Prediction: ", POSES[torch.argmax(left_pose_probs)],"\tRight Pose Max: ", torch.max(right_pose_probs).item(), "Right Prediction: ", POSES[torch.argmax(right_pose_probs)])
        
        if torch.max(left_pose_probs) > 0.45 and torch.max(right_pose_probs) > 0.45:
            # Only make a decision if the model is confident
            left_pose = torch.argmax(left_pose_probs)
            right_pose = torch.argmax(right_pose_probs)  
            return point_decider(left_pose, right_pose, left_movement, right_movement)
    return NO_POINT

def process_frame(
    frame,
    scorebox_detector,
    fencer_pose_classifier,
    pose_classifier,
) -> dict | None:
    scorebox_classification, _ = scorebox_detector.detect_and_classify(frame)

    if scorebox_classification == cv2_common.NO_SIDE:
        return None

    (
        fencer_boxes_left,
        fencer_keypoints_left,
        fencer_boxes_right,
        fencer_keypoints_right,
        left_movement,
        right_movement
    ) = fencer_pose_classifier.evaluate_on_input(frame)

    point = determine_point(
        frame,
        pose_classifier,
        interim_point_decider,
        scorebox_classification,
        fencer_boxes_left,
        fencer_boxes_right,
        fencer_keypoints_left,
        fencer_keypoints_right,
        left_movement,
        right_movement
    )

    if point == NO_POINT:
        return None

    return {
        "point": POINT_DICT[point],
        "confidence": None,  # later
        "movement": {
            "left": left_movement,
            "right": right_movement
        }
    }

def score_bout(input_video: str) -> list[dict]:
    """
    Scoring function that can take in either a video file or a live stream to score an entire fencing match.

    If given a live stream, it will run until the stream ends. If given a video file, it will run until the end of the video.
    """
    # Load scorebox model
    scorebox_detector = ScoreboxDetectorClassifier(SCOREBOX_MODEL_PATH)
    # Load fencer pose model
    fencer_pose_classifier = FencerPoseClassifier(FENCER_POSE_MODEL_PATH)
    # Load pose classifier model
    device = (
            torch.device("mps") if torch.backends.mps.is_available()
            else torch.device("cuda") if torch.cuda.is_available()
            else torch.device("cpu")
        )

    pose_classifier = SimpleNNClassifier(classes=["En-Garde", "Lunge", "Parry", "None"])

    state_dict = torch.load(
        POSE_CLASSIFIER_MODEL_PATH,
        map_location=device
    )

    pose_classifier.load_state_dict(state_dict)
    pose_classifier.to(device)
    pose_classifier.eval()
    # Load point decision tree model
    # point_decider = joblib.load(POINT_DECISION_TREE_PATH)
    events : list[ScoringEvent] = []

    # Open video stream
    cap = cv2.VideoCapture(input_video)
    fps = cap.get(cv2.CAP_PROP_FPS)
    current_frame = 1
    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            print('Stream ended; closing...')
            break

        # Process frame with scorebox detection
        scorebox_classification, scorebox_boxes = scorebox_detector.detect_and_classify(frame, debug=False)

        # Process frame with fencer pose classification
        (fencer_boxes_left, fencer_keypoints_left, fencer_boxes_right, fencer_keypoints_right, 
         left_movement, right_movement) = fencer_pose_classifier.evaluate_on_input(frame)

        # Note: the current pipeline will attempt to score every frame that there is a light on the scorebox.
        # In the future, either the ml service or backend must filter to avoid duplicate points being scored for the same touch. 
        if scorebox_classification != cv2_common.NO_SIDE:
            side = POINT_DICT[determine_point(frame, pose_classifier, interim_point_decider, scorebox_classification, fencer_boxes_left, fencer_boxes_right, fencer_keypoints_left, fencer_keypoints_right, left_movement, right_movement)]
            timestamp_ms = (current_frame * 1000) // fps
            confidence = 0.0 # once we train a proper point decider, we can return a confidence score here
            ml_payload = {
                "point": side,
                "scorebox_classification": scorebox_classification,
                "fencer_boxes_left": fencer_boxes_left, # this might be a matrix, so either backend will need to unwrap it or ml service will do more pre-processing after testing
                "fencer_keypoints_left": fencer_keypoints_left,
                "fencer_boxes_right": fencer_boxes_right,
                "fencer_keypoints_right": fencer_keypoints_right,
                "left_movement": left_movement,
                "right_movement": right_movement
            }
            event = ScoringEvent(timestamp_ms=timestamp_ms, side=side, confidence=confidence, model_version=MODEL_VERSION, ml_payload=ml_payload)
            events.append(event)
        current_frame += 1
    cap.release()
    return events

def score_point(input_video) -> dict:
    """
    Scoring function that takes in a clip of a single "touch" and determines which fencer scored the point. This can be used in conjunction with a touch detection model to score points in real-time.
    """
    # Load scorebox model
    scorebox_detector = ScoreboxDetectorClassifier(SCOREBOX_MODEL_PATH)
    # Load fencer pose model
    fencer_pose_classifier = FencerPoseClassifier(FENCER_POSE_MODEL_PATH)
    # Load pose classifier model
    device = (
        torch.device("mps") if torch.backends.mps.is_available()
        else torch.device("cuda") if torch.cuda.is_available()
        else torch.device("cpu")
    )

    pose_classifier = SimpleNNClassifier(classes=["En-Garde", "Lunge", "Parry", "None"])

    state_dict = torch.load(
        POSE_CLASSIFIER_MODEL_PATH,
        map_location=device
    )

    pose_classifier.load_state_dict(state_dict)
    pose_classifier.to(device)
    pose_classifier.eval()
    # Load point decision tree model
    # point_decider = joblib.load(POINT_DECISION_TREE_PATH)

    # Open video stream
    cap = cv2.VideoCapture(input_video)

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            print('Stream ended; closing...')
            break

        # Process frame with scorebox detection
        scorebox_classification, scorebox_boxes = scorebox_detector.detect_and_classify(frame, debug=False)

        # Process frame with fencer pose classification
        (fencer_boxes_left, fencer_keypoints_left, fencer_boxes_right, fencer_keypoints_right, 
         left_movement, right_movement) = fencer_pose_classifier.evaluate_on_input(frame)

        # cv2.waitKey(int(1000 / 30))
        if scorebox_classification != cv2_common.NO_SIDE:
            p = POINT_DICT[determine_point(frame, pose_classifier, interim_point_decider, scorebox_classification, fencer_boxes_left, fencer_boxes_right, fencer_keypoints_left, fencer_keypoints_right, left_movement, right_movement)]
            print(p)
            cap.release() # Release the video capture after processing the point clip
            return {
                "point": p,
                "scorebox_classification": scorebox_classification,
                "fencer_boxes_left": fencer_boxes_left,
                "fencer_keypoints_left": fencer_keypoints_left,
                "fencer_boxes_right": fencer_boxes_right,
                "fencer_keypoints_right": fencer_keypoints_right,
                "left_movement": left_movement,
                "right_movement": right_movement
            }

### API Methods
app = FastAPI()

@app.get("/")
def read_root():
    return {"Fencing ML Service": "Welcome to the Fencing ML Service! Use the /score-bout endpoint to score a fencing match.",
            "Model Version": MODEL_VERSION}

@app.post("/score-bout")
def score_bout_api(request: ScoreBoutRequest):
    return {"model_version": MODEL_VERSION,
            "events": score_bout(request.video_url)}
# def score_bout_api(request):
#     cap = cv2.VideoCapture(request.video_url)

#     events = []

#     while cap.isOpened():
#         ret, frame = cap.read()
#         if not ret:
#             break

#         event = process_frame(frame, ...)
#         if event:
#             events.append(event)

#     cap.release()
#     return {
#         "events": events,
#         "model_version": "v0.1"
#     }

@app.post("/score-point")
def score_point_api(video_url: str):
    return {"Not":"Implemented"} # score_point is not implemented with the right output yet, it is mainly there as a stub for future live scoring.
    return score_point(video_url)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)