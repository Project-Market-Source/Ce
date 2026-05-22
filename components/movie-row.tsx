"use client";

import type { Movie } from "@/lib/data";
import { MovieCard } from "./movie-card";

interface MovieRowProps {
  title: string;
  movies: Movie[];
}

export function MovieRow({ title, movies }: MovieRowProps) {
  if (movies.length === 0) return null;

  return (
    <section className="mb-4">
      <h2 className="text-white text-base font-bold px-4 mb-2">{title}</h2>
      <div className="flex gap-3 overflow-x-auto px-4 hide-scrollbar">
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div>
    </section>
  );
}
