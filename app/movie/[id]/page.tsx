"use client";

import { useState } from "react";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { ArrowLeft, Star, Play } from "lucide-react";
import { MoviePoster } from "@/components/movie-poster";
import { VideoPlayer } from "@/components/video-player";
import { useStore } from "@/lib/store";
import { getMovieById, type Episode } from "@/lib/data";

function EpisodeTile({
  episode,
  onPlay,
}: {
  episode: Episode;
  onPlay: () => void;
}) {
  return (
    <button
      onClick={onPlay}
      className="w-full bg-[var(--card)] rounded-lg border border-white/5 p-3 flex items-center gap-4 hover:bg-[var(--card)]/80 transition-colors"
    >
      <div className="w-[50px] h-[50px] rounded-md bg-[var(--muted)] flex items-center justify-center shrink-0">
        <span className="text-[var(--primary)] font-bold text-xs">
          S{episode.seasonNumber}E{episode.episodeNumber}
        </span>
      </div>
      <div className="flex-1 text-left">
        <h4 className="text-white font-bold text-[13px]">{episode.title}</h4>
        <p className="text-[var(--muted-foreground)] text-[11px]">
          Quality: 1080p • 720p • 360p Available
        </p>
      </div>
      <Play className="w-7 h-7 text-[var(--primary)]" />
    </button>
  );
}

export default function MovieDetailPage() {
  const params = useParams();
  const router = useRouter();
  const movieId = params.id as string;
  const movie = getMovieById(movieId);
  const { isInWatchlist, toggleWatchlist } = useStore();
  
  const [isPlayerOpen, setIsPlayerOpen] = useState(false);
  const [selectedEpisode, setSelectedEpisode] = useState<Episode | null>(null);

  if (!movie) {
    return (
      <div className="min-h-screen bg-[var(--background)] flex items-center justify-center">
        <p className="text-white">Movie not found</p>
      </div>
    );
  }

  const isSaved = isInWatchlist(movie.id);
  const currentStreamUrls = selectedEpisode?.streamUrls || movie.streamUrls;

  const handlePlayMovie = () => {
    if (movie.type === "series" && movie.episodes && movie.episodes.length > 0) {
      setSelectedEpisode(movie.episodes[0]);
    }
    setIsPlayerOpen(true);
  };

  const handlePlayEpisode = (episode: Episode) => {
    setSelectedEpisode(episode);
    setIsPlayerOpen(true);
  };

  return (
    <div className="min-h-screen bg-[var(--background)]">
      {/* Video Player Modal */}
      {isPlayerOpen && (
        <div className="fixed inset-0 z-50 bg-black flex items-center justify-center">
          <VideoPlayer
            src={currentStreamUrls["1080p"]}
            title={selectedEpisode ? `${movie.title} - ${selectedEpisode.title}` : movie.title}
            qualities={currentStreamUrls}
            onClose={() => {
              setIsPlayerOpen(false);
              setSelectedEpisode(null);
            }}
          />
        </div>
      )}

      {/* Hero Banner */}
      <div className="relative w-full h-[320px]">
        <MoviePoster
          src={movie.posterUrl}
          alt={movie.title}
          fill
          className="absolute inset-0"
          priority
        />
        
        {/* Gradient Overlay */}
        <div className="absolute inset-0 bg-gradient-to-b from-black/70 via-transparent to-[var(--background)]" />

        {/* Top Controls */}
        <div className="absolute top-0 left-0 right-0 p-4 flex items-center justify-between z-10">
          <button
            onClick={() => router.back()}
            className="w-9 h-9 bg-black/60 rounded-full flex items-center justify-center"
          >
            <ArrowLeft className="w-5 h-5 text-white" />
          </button>
          <button
            onClick={() => toggleWatchlist(movie)}
            className="w-9 h-9 bg-black/60 rounded-full flex items-center justify-center"
          >
            <Star
              className={`w-5 h-5 ${
                isSaved
                  ? "text-[var(--secondary)] fill-[var(--secondary)]"
                  : "text-white"
              }`}
            />
          </button>
        </div>

        {/* Bottom Info */}
        <div className="absolute bottom-0 left-0 right-0 p-4">
          {/* Badges */}
          <div className="flex items-center gap-2 mb-2">
            <div className="bg-[var(--secondary)]/20 rounded px-1.5 py-0.5 flex items-center gap-1">
              <Star className="w-3 h-3 text-[var(--secondary)] fill-[var(--secondary)]" />
              <span className="text-[var(--secondary)] text-[10px] font-bold">
                {movie.rating}
              </span>
            </div>
            <span className="text-white/80 text-[11px] font-semibold">{movie.year}</span>
            <span className="text-white/80 text-[11px] font-semibold">{movie.duration}</span>
            <span className="border border-white/40 rounded px-1 py-0.5 text-white text-[9px] font-extrabold uppercase">
              {movie.type}
            </span>
          </div>

          {/* Title */}
          <h1 className="text-white text-3xl font-extrabold">{movie.title}</h1>
        </div>
      </div>

      {/* Content */}
      <div className="p-4 pb-24">
        {/* Genres */}
        <div className="flex flex-wrap gap-1.5 mb-4">
          {movie.genres.map((genre) => (
            <span
              key={genre}
              className="bg-[var(--card)] rounded-xl px-3 py-1 text-white text-[10px] font-bold"
            >
              {genre}
            </span>
          ))}
        </div>

        {/* Synopsis */}
        <h2 className="text-white font-bold mb-1">Synopsis</h2>
        <p className="text-[var(--muted-foreground)] text-sm leading-5 mb-6">
          {movie.synopsis}
        </p>

        {/* Episodes (for series) */}
        {movie.type === "series" && movie.episodes && movie.episodes.length > 0 && (
          <div className="mb-6">
            <h2 className="text-white font-bold text-[15px] mb-2">
              Episodes ({movie.episodes.length})
            </h2>
            <div className="space-y-2">
              {movie.episodes.map((episode) => (
                <EpisodeTile
                  key={episode.id}
                  episode={episode}
                  onPlay={() => handlePlayEpisode(episode)}
                />
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Watch Now FAB */}
      <div className="fixed bottom-6 left-4 right-4 z-40">
        <button
          onClick={handlePlayMovie}
          className="w-full h-12 bg-[var(--primary)] rounded-3xl flex items-center justify-center gap-1.5 shadow-lg shadow-[var(--primary)]/30"
        >
          <Play className="w-6 h-6 text-white fill-white" />
          <span className="text-white text-xs font-extrabold tracking-wide">
            WATCH STREAMING NOW
          </span>
        </button>
      </div>
    </div>
  );
}
