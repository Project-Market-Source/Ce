"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { Star, Play, Plus, Check } from "lucide-react";
import { MoviePoster } from "./movie-poster";
import { useStore } from "@/lib/store";
import type { Movie } from "@/lib/data";

interface TrendingCarouselProps {
  movies: Movie[];
}

export function TrendingCarousel({ movies }: TrendingCarouselProps) {
  const [activeIndex, setActiveIndex] = useState(0);
  const [mounted, setMounted] = useState(false);
  const { isInWatchlist, toggleWatchlist } = useStore();
  
  // Handle hydration
  useEffect(() => {
    setMounted(true);
  }, []);
  
  // Auto-scroll effect
  useEffect(() => {
    if (movies.length === 0) return;
    
    const interval = setInterval(() => {
      setActiveIndex((prev) => (prev + 1) % movies.length);
    }, 5000);
    
    return () => clearInterval(interval);
  }, [movies.length]);
  
  if (movies.length === 0) return null;
  
  const movie = movies[activeIndex];
  const isSaved = mounted && isInWatchlist(movie.id);
  
  return (
    <section className="px-4 py-1.5">
      <Link
        href={`/movie/${movie.id}`}
        className="relative block w-full h-[310px] rounded-3xl overflow-hidden"
      >
        {/* Background Image */}
        <MoviePoster
          src={movie.posterUrl}
          alt={movie.title}
          fill
          className="absolute inset-0"
          priority
        />
        
        {/* Gradient Overlay */}
        <div className="absolute inset-0 bg-gradient-to-b from-black/10 via-transparent to-[#0F1014]/95" />
        
        {/* Content */}
        <div className="absolute bottom-0 left-0 right-0 p-4">
          {/* Featured Tag */}
          <span className="text-[var(--secondary)] text-[11px] font-extrabold tracking-widest mb-0.5 block">
            FEATURED TODAY
          </span>
          
          {/* Title */}
          <h2 className="text-white text-2xl font-bold truncate mb-1">
            {movie.title}
          </h2>
          
          {/* Metadata */}
          <div className="flex items-center gap-2 mb-3">
            <span className="text-[var(--muted-foreground)] text-xs font-medium">
              {movie.year}
            </span>
            <span className="bg-white/10 rounded px-1.5 py-0.5 text-white text-[10px] font-bold">
              {movie.genres[0] || "Sci-Fi"}
            </span>
            <div className="flex items-center gap-1">
              <Star className="w-3.5 h-3.5 text-[var(--secondary)] fill-[var(--secondary)]" />
              <span className="text-[var(--secondary)] text-xs font-bold">
                {movie.rating}
              </span>
            </div>
          </div>
          
          {/* Action Buttons */}
          <div className="flex items-center gap-2.5">
            <Link
              href={`/movie/${movie.id}`}
              className="flex-1 h-11 bg-white text-black rounded-xl flex items-center justify-center gap-1.5 font-bold text-sm hover:bg-white/90 transition-colors"
              onClick={(e) => e.stopPropagation()}
            >
              <Play className="w-5 h-5 fill-black" />
              Watch Now
            </Link>
            
            <button
              onClick={(e) => {
                e.preventDefault();
                e.stopPropagation();
                toggleWatchlist(movie);
              }}
              className="w-11 h-11 bg-white/10 border border-white/15 rounded-xl flex items-center justify-center hover:bg-white/20 transition-colors"
            >
              {isSaved ? (
                <Check className="w-5 h-5 text-[var(--secondary)]" />
              ) : (
                <Plus className="w-5 h-5 text-white" />
              )}
            </button>
          </div>
          
          {/* Dot Indicators */}
          <div className="flex justify-center gap-1.5 mt-2">
            {movies.map((_, idx) => (
              <button
                key={idx}
                onClick={(e) => {
                  e.preventDefault();
                  e.stopPropagation();
                  setActiveIndex(idx);
                }}
                className={`rounded-full transition-all ${
                  idx === activeIndex
                    ? "w-2.5 h-2.5 bg-[var(--primary)]"
                    : "w-1.5 h-1.5 bg-white/30 hover:bg-white/50"
                }`}
              />
            ))}
          </div>
        </div>
      </Link>
    </section>
  );
}
