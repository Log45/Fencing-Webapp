export type Bout = {
    id: number
    status: "UPLOAD_PENDING" | "PENDING" | "PROCESSING" | "COMPLETED" | "FAILED"
  }
  
export type ScoringEvent = {
timestampMs: number
side: "LEFT" | "RIGHT" | "NONE"
confidence: number
mlPayload: Record<string, any>
}