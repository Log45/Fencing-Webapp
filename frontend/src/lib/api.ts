const API = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";


export async function getUploadUrl(filename: string) {
    const res = await fetch(
      `${API}/api/bouts/upload-url?filename=${encodeURIComponent(filename)}`,
      {
        method: "POST"
      }
    );
  
    if (!res.ok) {
      throw new Error("Failed to get upload URL");
    }
  
    return res.json();
  }

export async function getVideoUrl(id: string) {
    const res = await fetch(`${API}/api/bouts/${id}/video`);
    return res.json();
}
  
export async function startScoring(boutId: number) {
    const res = await fetch(`${API}/api/bouts/${boutId}/score`, {
        method: "POST"
    });

    if (!res.ok) {
        throw new Error("Failed to start scoring");
    }

return;
}

export async function getBout(id: string) {
  const res = await fetch(`${API}/api/bouts/${id}`);
  return res.json();
}

export async function getEvents(id: string) {
  const res = await fetch(`${API}/api/bouts/${id}/events`);
  return res.json();
}