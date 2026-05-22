"use client";

import { useState } from "react";
import { Search, Settings, Info } from "lucide-react";
import { TopAppBar } from "@/components/top-app-bar";
import { BottomNav } from "@/components/bottom-nav";
import { MovieCard } from "@/components/movie-card";
import { useStore } from "@/lib/store";

export default function SearchPage() {
  const [showCdnSettings, setShowCdnSettings] = useState(false);
  const [cdnInput, setCdnInput] = useState("");
  const { searchQuery, setSearchQuery, getSearchResults, setCdnBaseUrl } = useStore();
  
  const results = getSearchResults();

  const handleCdnConnect = () => {
    setCdnBaseUrl(cdnInput);
    setShowCdnSettings(false);
  };

  return (
    <div className="min-h-screen bg-[var(--background)] pb-24">
      <TopAppBar />

      <main className="px-4">
        {/* Search Bar */}
        <div className="flex items-center gap-3 py-3">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-[var(--muted-foreground)]" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search title, series, or genres..."
              className="w-full h-12 pl-10 pr-4 bg-[var(--card)] rounded-xl text-white placeholder:text-[var(--muted-foreground)] outline-none focus:ring-2 focus:ring-[var(--primary)]/50"
            />
          </div>
          <button
            onClick={() => setShowCdnSettings(!showCdnSettings)}
            className={`w-12 h-12 bg-[var(--card)] rounded-xl flex items-center justify-center transition-colors ${
              showCdnSettings ? "text-[var(--secondary)]" : "text-white"
            }`}
          >
            <Settings className="w-6 h-6" />
          </button>
        </div>

        {/* CDN Settings Panel */}
        {showCdnSettings && (
          <div className="mb-4 p-4 bg-[var(--card)] rounded-xl border border-[var(--primary)]/40">
            <h3 className="text-[var(--primary)] font-bold text-sm mb-1.5">
              Local ISP CDN Settings
            </h3>
            <p className="text-[var(--muted-foreground)] text-[11px] mb-3">
              Configure local servers IP to fetch streaming catalogs without SSL
              restrictions (e.g., 10.15.1.4:8080/api). Defaults to fallback mock
              lists if blank.
            </p>
            <div className="flex items-center gap-2">
              <input
                type="text"
                value={cdnInput}
                onChange={(e) => setCdnInput(e.target.value)}
                placeholder="http://192.168.1.1:8000/api"
                className="flex-1 h-[50px] px-3 bg-[var(--muted)] rounded-lg text-white text-xs placeholder:text-[var(--muted-foreground)] outline-none"
              />
              <button
                onClick={handleCdnConnect}
                className="h-11 px-4 bg-[var(--primary)] text-white rounded-lg text-[11px] font-bold hover:bg-[var(--primary)]/90 transition-colors"
              >
                Connect
              </button>
            </div>
          </div>
        )}

        {/* Search Results */}
        {results.length === 0 ? (
          <div className="flex-1 flex flex-col items-center justify-center py-20">
            <Info className="w-14 h-14 text-[var(--muted-foreground)] mb-3" />
            <p className="text-[var(--muted-foreground)] text-sm font-medium">
              No movies or series found
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-3 pb-4">
            {results.map((movie) => (
              <MovieCard key={movie.id} movie={movie} />
            ))}
          </div>
        )}
      </main>

      <BottomNav />
    </div>
  );
}
