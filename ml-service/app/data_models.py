from pydantic import BaseModel

class ScoringEvent(BaseModel):
    timestamp_ms: int # milliseconds since the start of the video
    side: str # which side scored the point
    confidence: float
    model_version: str
    ml_payload: dict # json with payload from ML model for visualization in frontend

class ScoreBoutRequest(BaseModel):
    video_url: str

class Bout(BaseModel):
    video_url: str
    status: str
    final_left_score: int
    final_right_score: int
    created_at: int
    updated_at: int