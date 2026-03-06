"use client";

import { useState } from "react";
import { getUploadUrl, startScoring } from "@/lib/api";
import { useRouter } from "next/navigation";

export default function UploadVideo() {
  const [file, setFile] = useState<File | null>(null);
  const [isUploading, setIsUploading] = useState(false);
  const router = useRouter();

  async function handleUpload() {
    if (!file || isUploading) return;
  
    try {
      setIsUploading(true);
      const { uploadUrl, boutId } = await getUploadUrl(file.name);
  
      const uploadRes = await fetch(uploadUrl, {
        method: "PUT",
        body: file
      });
  
      if (!uploadRes.ok) {
        const text = await uploadRes.text();
        console.error("Upload error:", text);
        throw new Error("Upload failed");
      }
  
      await startScoring(boutId);
  
      router.push(`/bout/${boutId}`);
  
    } catch (err) {
      console.error(err);
      alert("Upload failed");
    } finally {
      setIsUploading(false);
    }
  }

  return (
    <div className="space-y-4">
      <div className="flex flex-col items-center gap-3">
        <label
          htmlFor="video-upload"
          className="inline-flex items-center justify-center px-4 py-2 rounded-full border border-dashed border-slate-600 text-sm text-slate-200 cursor-pointer hover:border-slate-400 hover:bg-slate-800/60 transition-colors"
        >
          <span className="mr-2">Choose video file</span>
        </label>
        <input
          id="video-upload"
          type="file"
          accept="video/*"
          className="hidden"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
        />
        <p className="text-xs text-slate-400 min-h-[1rem]">
          {file ? file.name : "No file selected"}
        </p>
      </div>

      <div className="flex justify-center">
        <button
          onClick={handleUpload}
          disabled={!file || isUploading}
          className="inline-flex items-center justify-center px-6 py-2.5 rounded-full text-sm font-medium bg-emerald-500 text-slate-950 hover:bg-emerald-400 disabled:opacity-40 disabled:cursor-not-allowed transition-colors shadow-lg shadow-emerald-500/20"
        >
          {isUploading ? "Uploading..." : "Start Scoring"}
        </button>
      </div>
    </div>
  );
}