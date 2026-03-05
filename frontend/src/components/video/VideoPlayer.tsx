"use client";

import { forwardRef } from "react";

type Props = {
  src: string;
};

const VideoPlayer = forwardRef<HTMLVideoElement, Props>(({ src }, ref) => {
  return (
    <video
      ref={ref}
      src={src}
      controls
      width={800}
      style={{ maxWidth: "100%" }}
    />
  );
});

VideoPlayer.displayName = "VideoPlayer";

export default VideoPlayer;