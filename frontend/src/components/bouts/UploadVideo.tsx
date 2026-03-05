"use client";

import { useState } from "react";
import { getUploadUrl, startScoring } from "@/lib/api";
import { useRouter } from "next/navigation";

export default function UploadVideo() {
  const [file, setFile] = useState<File | null>(null);
  const router = useRouter();

  async function handleUpload() {
    if (!file) return;
  
    try {
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
    }
  }

  return (
    <div>
      <input
        type="file"
        accept="video/*"
        onChange={(e) => setFile(e.target.files?.[0] || null)}
      />

      <button onClick={handleUpload}>
        Upload Video
      </button>
    </div>
  );
}