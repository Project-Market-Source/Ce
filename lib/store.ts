"use client";

import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { Movie, Category } from "./data";
import { mockMovies, mockCategories } from "./data";

interface WatchlistItem {
  id: string;
  title: string;
  type: "movie" | "series";
  year: string;
  rating: string;
  genres: string[];
  synopsis: string;
  posterUrl: string;
  duration: string;
}

interface StoreState {
  // Data
  movies: Movie[];
  categories: Category[];
  watchlist: WatchlistItem[];
  
  // UI State
  searchQuery: string;
  selectedCategory: Category | null;
  cdnBaseUrl: string;
  
  // Actions
  setSearchQuery: (query: string) => void;
  setSelectedCategory: (category: Category | null) => void;
  setCdnBaseUrl: (url: string) => void;
  addToWatchlist: (movie: Movie) => void;
  removeFromWatchlist: (movieId: string) => void;
  isInWatchlist: (movieId: string) => boolean;
  toggleWatchlist: (movie: Movie) => void;
  getSearchResults: () => Movie[];
}

export const useStore = create<StoreState>()(
  persist(
    (set, get) => ({
      // Initial Data
      movies: mockMovies,
      categories: mockCategories,
      watchlist: [],
      
      // UI State
      searchQuery: "",
      selectedCategory: null,
      cdnBaseUrl: "https://mock.cinemana.local/",
      
      // Actions
      setSearchQuery: (query) => set({ searchQuery: query }),
      
      setSelectedCategory: (category) => set({ selectedCategory: category }),
      
      setCdnBaseUrl: (url) => set({ cdnBaseUrl: url }),
      
      addToWatchlist: (movie) => {
        const { watchlist } = get();
        if (!watchlist.some((item) => item.id === movie.id)) {
          set({
            watchlist: [
              ...watchlist,
              {
                id: movie.id,
                title: movie.title,
                type: movie.type,
                year: movie.year,
                rating: movie.rating,
                genres: movie.genres,
                synopsis: movie.synopsis,
                posterUrl: movie.posterUrl,
                duration: movie.duration,
              },
            ],
          });
        }
      },
      
      removeFromWatchlist: (movieId) => {
        set({
          watchlist: get().watchlist.filter((item) => item.id !== movieId),
        });
      },
      
      isInWatchlist: (movieId) => {
        return get().watchlist.some((item) => item.id === movieId);
      },
      
      toggleWatchlist: (movie) => {
        const { isInWatchlist, addToWatchlist, removeFromWatchlist } = get();
        if (isInWatchlist(movie.id)) {
          removeFromWatchlist(movie.id);
        } else {
          addToWatchlist(movie);
        }
      },
      
      getSearchResults: () => {
        const { movies, searchQuery } = get();
        if (!searchQuery.trim()) return movies;
        const lowerQuery = searchQuery.toLowerCase();
        return movies.filter(
          (movie) =>
            movie.title.toLowerCase().includes(lowerQuery) ||
            movie.genres.some((genre) =>
              genre.toLowerCase().includes(lowerQuery)
            ) ||
            movie.synopsis.toLowerCase().includes(lowerQuery)
        );
      },
    }),
    {
      name: "cinemana-storage",
      partialize: (state) => ({
        watchlist: state.watchlist,
        cdnBaseUrl: state.cdnBaseUrl,
      }),
    }
  )
);
