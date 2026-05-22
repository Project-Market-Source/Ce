"use client";

import { useState } from "react";
import Link from "next/link";
import { ArrowLeft, Flame, Rocket, Theater, TrendingUp, Router, Clock } from "lucide-react";
import { TopAppBar } from "@/components/top-app-bar";
import { BottomNav } from "@/components/bottom-nav";
import { MovieCard } from "@/components/movie-card";
import { mockCategories, mockMovies, type Category } from "@/lib/data";

const iconMap: Record<string, React.ElementType> = {
  flame: Flame,
  rocket: Rocket,
  theater: Theater,
  trending: TrendingUp,
  router: Router,
  clock: Clock,
};

function CategoryCard({
  category,
  onClick,
}: {
  category: Category;
  onClick: () => void;
}) {
  const Icon = iconMap[category.iconName] || Flame;

  return (
    <button
      onClick={onClick}
      className="w-full h-24 bg-[var(--card)] rounded-xl border border-white/5 p-3 relative overflow-hidden hover:bg-[var(--card)]/80 transition-colors text-left"
    >
      <div className="absolute top-3 right-3 w-7 h-7 rounded-full bg-[var(--primary)]/15 flex items-center justify-center">
        <Icon className="w-4 h-4 text-[var(--primary)]" />
      </div>
      <span className="absolute bottom-3 left-3 text-white font-bold text-sm">
        {category.name}
      </span>
    </button>
  );
}

export default function CategoriesPage() {
  const [activeCategory, setActiveCategory] = useState<Category | null>(null);

  const filteredMovies = activeCategory
    ? mockMovies.filter((movie) =>
        movie.genres.some((genre) =>
          genre.toLowerCase().includes(activeCategory.name.split(" ")[0].toLowerCase())
        )
      )
    : [];

  if (activeCategory) {
    return (
      <div className="min-h-screen bg-[var(--background)] pb-24">
        <div className="px-4 py-4">
          {/* Back Header */}
          <div className="flex items-center gap-4 mb-4">
            <button
              onClick={() => setActiveCategory(null)}
              className="w-9 h-9 bg-[var(--card)] rounded-full flex items-center justify-center"
            >
              <ArrowLeft className="w-5 h-5 text-white" />
            </button>
            <div>
              <h1 className="text-white text-lg font-bold">{activeCategory.name}</h1>
              <p className="text-[var(--muted-foreground)] text-xs">
                {filteredMovies.length} content pieces available
              </p>
            </div>
          </div>

          {/* Filtered Results */}
          {filteredMovies.length === 0 ? (
            <div className="flex-1 flex items-center justify-center py-20">
              <p className="text-[var(--muted-foreground)]">
                No listings inside {activeCategory.name} yet.
              </p>
            </div>
          ) : (
            <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-3">
              {filteredMovies.map((movie) => (
                <MovieCard key={movie.id} movie={movie} />
              ))}
            </div>
          )}
        </div>

        <BottomNav />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[var(--background)] pb-24">
      <TopAppBar />

      <main className="px-4">
        <h1 className="text-white text-lg font-extrabold mb-2">
          Cinemana Categories
        </h1>

        <div className="grid grid-cols-2 gap-4">
          {mockCategories.map((category) => (
            <CategoryCard
              key={category.id}
              category={category}
              onClick={() => setActiveCategory(category)}
            />
          ))}
        </div>
      </main>

      <BottomNav />
    </div>
  );
}
