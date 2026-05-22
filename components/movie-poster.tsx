"use client";

import Image from "next/image";
import { useState } from "react";
import { AlertCircle, RefreshCw } from "lucide-react";

interface MoviePosterProps {
  src: string;
  alt: string;
  fill?: boolean;
  className?: string;
  priority?: boolean;
}

export function MoviePoster({
  src,
  alt,
  fill = false,
  className = "",
  priority = false,
}: MoviePosterProps) {
  const [isError, setIsError] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  if (isError) {
    return (
      <div
        className={`bg-gradient-to-b from-[var(--card)] to-[var(--background)] flex flex-col items-center justify-center ${className}`}
      >
        <AlertCircle className="w-9 h-9 text-[var(--primary)] opacity-50 mb-1" />
        <span className="text-white/50 text-[10px] font-bold text-center px-1 max-w-full truncate">
          {alt}
        </span>
      </div>
    );
  }

  return (
    <div className={`relative ${className}`}>
      {isLoading && (
        <div className="absolute inset-0 bg-gradient-to-b from-[var(--card)] to-[var(--background)] flex flex-col items-center justify-center z-10">
          <RefreshCw className="w-9 h-9 text-[var(--primary)] opacity-50 animate-spin" />
        </div>
      )}
      <Image
        src={src}
        alt={alt}
        fill={fill}
        className={`object-cover ${isLoading ? "opacity-0" : "opacity-100"} transition-opacity duration-300`}
        onLoad={() => setIsLoading(false)}
        onError={() => {
          setIsLoading(false);
          setIsError(true);
        }}
        priority={priority}
        sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
      />
    </div>
  );
}
