"use client";

import Link from "next/link";
import { Star } from "lucide-react";
import { MoviePoster } from "./movie-poster";
import type { Movie } from "@/lib/data";

interface MovieCardProps {
  movie: Movie;
}

export function MovieCard({ movie }: MovieCardProps) {
  return (
    <Link href={`/movie/${movie.id}`} className="flex flex-col w-[110px] shrink-0">
      <div className="relative w-[110px] h-[160px] rounded-2xl overflow-hidden border border-white/10">
        <MoviePoster
          src={movie.posterUrl}
          alt={movie.title}
          fill
          className="w-full h-full"
        />
        
        {/* Rating Badge */}
        <div className="absolute top-1.5 left-1.5 bg-black/75 rounded px-1 py-0.5 flex items-center gap-0.5">
          <Star className="w-2.5 h-2.5 text-[var(--secondary)] fill-[var(--secondary)]" />
          <span className="text-white text-[10px] font-bold">{movie.rating}</span>
        </div>
      </div>
      
      <h3 className="mt-1 text-white text-[11px] font-bold truncate">{movie.title}</h3>
      <p className="text-[var(--muted-foreground)] text-[9px] truncate">
        {movie.year} • {movie.duration}
      </p>
    </Link>
  );
}
