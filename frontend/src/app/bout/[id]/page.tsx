"use client";

import { useEffect, useRef, useState } from "react";
import { useParams } from "next/navigation";
import { getBout, getEvents, getVideoUrl } from "@/lib/api";
import VideoPlayer from "@/components/video/VideoPlayer";
import Timeline from "@/components/scoring/Timeline";
import { Bout, ScoringEvent } from "@/types/api";

export default function BoutPage() {
  const params = useParams();
  const id = params.id as string;

  const [bout, setBout] = useState<Bout | null>(null);
  const [events, setEvents] = useState<ScoringEvent[]>([]);
  const [videoUrl, setVideoUrl] = useState<string | null>(null);
  const [duration, setDuration] = useState(0);

  const videoRef = useRef<HTMLVideoElement>(null);

  // Poll bout status
  useEffect(() => {
    const interval = setInterval(async () => {
      const data = await getBout(id);
      setBout(data);

      if (data.status === "COMPLETED") {
        clearInterval(interval);

        const ev = await getEvents(id);
        setEvents(ev);

        const video = await getVideoUrl(id);
        setVideoUrl(video.url);
      }
    }, 2000);

    return () => clearInterval(interval);
  }, [id]);

  // Capture video duration
  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;

    const handler = () => {
      setDuration(video.duration);
    };

    video.addEventListener("loadedmetadata", handler);

    return () => video.removeEventListener("loadedmetadata", handler);
  }, [videoUrl]);

  if (!bout) return <div>Loading...</div>;

  if (bout.status !== "COMPLETED") {
    return (
      <div>
        <h1>Bout {id}</h1>
        <p>Processing video...</p>
        <p>Status: {bout.status}</p>
      </div>
    );
  }

  return (
    <div>
      <h1>Bout {id}</h1>

      {videoUrl && (
        <VideoPlayer
          ref={videoRef}
          src={videoUrl}
        />
      )}

      <Timeline
        events={events.map((e) => ({
          timestamp: e.timestampMs / 1000,
          side: e.side
        }))}
        duration={duration}
        onSeek={(t: number) => {
          if (videoRef.current) {
            videoRef.current.currentTime = t;
          }
        }}
      />
    </div>
  );
}