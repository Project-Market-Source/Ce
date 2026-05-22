"use client";

import { Star } from "lucide-react";
import { TopAppBar } from "@/components/top-app-bar";
import { BottomNav } from "@/components/bottom-nav";
import { MovieCard } from "@/components/movie-card";
import { useStore } from "@/lib/store";
import { mockMovies, type Movie } from "@/lib/data";

export default function WatchlistPage() {
  const { watchlist } = useStore();

  // Convert watchlist items back to full Movie objects for MovieCard
  const watchlistMovies: Movie[] = watchlist.map((item) => {
    const fullMovie = mockMovies.find((m) => m.id === item.id);
    if (fullMovie) return fullMovie;
    return {
      ...item,
      streamUrls: {
        "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
        "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
      },
    };
  });

  return (
    <div className="min-h-screen bg-[var(--background)] pb-24">
      <TopAppBar />

      <main className="px-4">
        <h1 className="text-white text-lg font-extrabold mb-2">My Watchlist</h1>

        {watchlistMovies.length === 0 ? (
          <div className="flex-1 flex flex-col items-center justify-center py-20">
            <Star className="w-14 h-14 text-[var(--muted-foreground)] mb-3" />
            <p className="text-white font-bold text-[15px] mb-1">
              Your Watchlist is empty
            </p>
            <p className="text-[var(--muted-foreground)] text-xs text-center max-w-[260px]">
              Tap a movie or show from home and save it to watch offline later on
              local ISP lines.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-3">
            {watchlistMovies.map((movie) => (
              <MovieCard key={movie.id} movie={movie} />
            ))}
          </div>
        )}
      </main>

      <BottomNav />
    </div>
  );
}
