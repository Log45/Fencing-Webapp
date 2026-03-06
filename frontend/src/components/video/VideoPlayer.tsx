"use client";

import { forwardRef } from "react";

type Props = {
  src: string;
  className?: string;
};

const VideoPlayer = forwardRef<HTMLVideoElement, Props>(
  ({ src, className }, ref) => {
    return (
      <video
        ref={ref}
        src={src}
        controls
        className={className ?? "w-full rounded-lg shadow-xl"}
      />
    );
  }
);

VideoPlayer.displayName = "VideoPlayer";

export default VideoPlayer;