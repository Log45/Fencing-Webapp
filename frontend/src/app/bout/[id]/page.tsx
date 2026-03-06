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
      <main className="min-h-screen flex items-center justify-center bg-slate-950 text-slate-100">
        <div className="max-w-xl w-full px-6 py-10 rounded-2xl bg-slate-900/70 shadow-2xl border border-slate-800 text-center">
          <h1 className="text-2xl font-semibold mb-3">Bout {id}</h1>
          <p className="mb-1">Processing video...</p>
          <p className="text-sm text-slate-300">Status: {bout.status}</p>
        </div>
      </main>
    );
  }

  return (
    <main className="min-h-screen flex items-center justify-center bg-slate-950 text-slate-100 px-4">
      <div className="w-full max-w-5xl">
        <div className="mb-6 text-center">
          <h1 className="text-2xl font-semibold">Bout {id}</h1>
        </div>

        <div className="flex flex-col items-center">
          <div className="w-full max-w-3xl">
            {videoUrl && (
              <VideoPlayer
                ref={videoRef}
                src={videoUrl}
                className="w-full max-w-3xl rounded-lg shadow-xl mx-auto"
              />
            )}

            <Timeline
              events={events.map((e) => ({
                timestamp: e.timestampMs / 1000,
                side: e.side
              }))}
              duration={duration}
            />
          </div>
        </div>
      </div>
    </main>
  );
}