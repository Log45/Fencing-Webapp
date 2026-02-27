from fastapi import FastAPI
import uvicorn

import cv2
import cv2_common
from argparse import ArgumentParser, Namespace
from yolo_scorebox_classifier import ScoreboxDetectorClassifier
from cv2.typing import MatLike
from fencer_pose import FencerPoseClassifier
from nn_pose_classifier import SimpleNNClassifier
import torch

## Constants
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

app = FastAPI()

@app.get("/")
def read_root():
    return {"Hello": "World"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)