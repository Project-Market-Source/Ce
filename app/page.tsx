import { TopAppBar } from "@/components/top-app-bar";
import { BottomNav } from "@/components/bottom-nav";
import { TrendingCarousel } from "@/components/trending-carousel";
import { MovieRow } from "@/components/movie-row";
import { mockMovies } from "@/lib/data";

export default function HomePage() {
  const featuredMovies = mockMovies.slice(0, 4);
  const latestMovies = mockMovies.filter((m) => m.type === "movie");
  const topSeries = mockMovies.filter((m) => m.type === "series");
  const recentlyAdded = [...mockMovies].reverse();

  return (
    <div className="min-h-screen bg-[var(--background)] pb-24">
      <TopAppBar />

      <main className="flex flex-col gap-4">
        <TrendingCarousel movies={featuredMovies} />
        <MovieRow title="Latest Movies" movies={latestMovies} />
        <MovieRow title="Top Series" movies={topSeries} />
        <MovieRow title="Recently Added Releases" movies={recentlyAdded} />
      </main>

      <BottomNav />
    </div>
  );
}
