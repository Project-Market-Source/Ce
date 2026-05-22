// Data Types for Cinemana Streaming App

export interface Episode {
  id: string;
  title: string;
  episodeNumber: number;
  seasonNumber: number;
  streamUrls: Record<string, string>;
}

export interface Movie {
  id: string;
  title: string;
  type: "movie" | "series";
  year: string;
  rating: string;
  genres: string[];
  synopsis: string;
  posterUrl: string;
  duration: string;
  streamUrls: Record<string, string>;
  episodes?: Episode[];
}

export interface Category {
  id: string;
  name: string;
  iconName: string;
}

// Mock Streams - Public Domain Videos
const MOCK_STREAMS: Record<string, string> = {
  "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
  "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
  "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
};

// Mock Categories
export const mockCategories: Category[] = [
  { id: "1", name: "Action Thrillers", iconName: "flame" },
  { id: "2", name: "Sci-Fi Space", iconName: "rocket" },
  { id: "3", name: "Crime & Drama", iconName: "theater" },
  { id: "4", name: "Trending Shows", iconName: "trending" },
  { id: "5", name: "Local ISP Premium", iconName: "router" },
  { id: "6", name: "Recently Added", iconName: "clock" },
];

// Mock Movies Data
export const mockMovies: Movie[] = [
  {
    id: "m1",
    title: "Tears of Steel",
    type: "movie",
    year: "2024",
    rating: "8.4",
    genres: ["Sci-Fi", "Action", "Cyberpunk"],
    synopsis: "Set in a dystopian future where robots have seized control, a rogue band of software engineers and warriors try to salvage humanity's fate using forgotten neural arrays in the heart of Amsterdam.",
    posterUrl: "https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&q=80&w=600",
    duration: "1h 52m",
    streamUrls: MOCK_STREAMS,
  },
  {
    id: "m2",
    title: "The Cosmos Within",
    type: "movie",
    year: "2025",
    rating: "9.1",
    genres: ["Space", "Sci-Fi", "Documentary"],
    synopsis: "A cinematic journey exploring the boundless expanses of our outer cosmos, contrasted against the deep internal neural pathways of human synapses.",
    posterUrl: "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&q=80&w=600",
    duration: "2h 10m",
    streamUrls: {
      "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
      "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
      "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
    },
  },
  {
    id: "m3",
    title: "Tokyo Driftway",
    type: "movie",
    year: "2023",
    rating: "7.8",
    genres: ["Action", "Racing", "Suspense"],
    synopsis: "Underground racing teams navigate neon-lit alleys of neo-Tokyo using prototype vehicles built on local network optical nodes, bypassing city surveillance networks.",
    posterUrl: "https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&q=80&w=600",
    duration: "1h 45m",
    streamUrls: MOCK_STREAMS,
  },
  {
    id: "m4",
    title: "Neon Sins",
    type: "series",
    year: "2024",
    rating: "8.9",
    genres: ["Crime", "Mystery", "Noir"],
    synopsis: "A private investigator on a cybernetic city block is hired to track down missing databases holding the key to the local server grids supplying offline video libraries.",
    posterUrl: "https://images.unsplash.com/photo-1514306191717-452ec28c7814?auto=format&fit=crop&q=80&w=600",
    duration: "1 Season",
    streamUrls: MOCK_STREAMS,
    episodes: [
      {
        id: "e1_1",
        title: "The Grid Unlocked",
        episodeNumber: 1,
        seasonNumber: 1,
        streamUrls: {
          "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
          "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
          "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        },
      },
      {
        id: "e1_2",
        title: "Dark Fiber",
        episodeNumber: 2,
        seasonNumber: 1,
        streamUrls: {
          "1080p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
          "720p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
          "360p": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
        },
      },
      {
        id: "e1_3",
        title: "The CDN Heist",
        episodeNumber: 3,
        seasonNumber: 1,
        streamUrls: MOCK_STREAMS,
      },
    ],
  },
  {
    id: "m5",
    title: "Whispers in Dust",
    type: "movie",
    year: "2024",
    rating: "7.9",
    genres: ["Thriller", "Drama"],
    synopsis: "In an abandoned wind-farm in central Arizona, researchers discover encrypted low-frequency audio tracks that tell a story of an impending cosmic rupture.",
    posterUrl: "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&q=80&w=600",
    duration: "1h 51m",
    streamUrls: MOCK_STREAMS,
  },
  {
    id: "m6",
    title: "Deep Space Heist",
    type: "series",
    year: "2025",
    rating: "8.6",
    genres: ["Sci-Fi", "Comedy"],
    synopsis: "A band of cosmic mercenaries attempts to steal server archives holding ancient classical terrestrial video files. A humorous, action-packed high-speed space chase.",
    posterUrl: "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&q=80&w=600",
    duration: "2 Seasons",
    streamUrls: MOCK_STREAMS,
    episodes: [
      {
        id: "e2_1",
        title: "Pilot: Storage Full",
        episodeNumber: 1,
        seasonNumber: 1,
        streamUrls: MOCK_STREAMS,
      },
      {
        id: "e2_2",
        title: "The Helium Vault",
        episodeNumber: 2,
        seasonNumber: 1,
        streamUrls: MOCK_STREAMS,
      },
    ],
  },
  {
    id: "m7",
    title: "Retro Beats: Synthesizer Saga",
    type: "movie",
    year: "2024",
    rating: "8.2",
    genres: ["Music", "Documentary"],
    synopsis: "Chronicles the evolution of early electronic modular music and its modern-day surge on indie internet broadcasts across isolated community relays.",
    posterUrl: "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&q=80&w=600",
    duration: "1h 35m",
    streamUrls: MOCK_STREAMS,
  },
];

// Helper Functions
export function getMovieById(id: string): Movie | undefined {
  return mockMovies.find((movie) => movie.id === id);
}

export function getMoviesByType(type: "movie" | "series"): Movie[] {
  return mockMovies.filter((movie) => movie.type === type);
}

export function searchMovies(query: string): Movie[] {
  const lowerQuery = query.toLowerCase();
  return mockMovies.filter(
    (movie) =>
      movie.title.toLowerCase().includes(lowerQuery) ||
      movie.genres.some((genre) => genre.toLowerCase().includes(lowerQuery)) ||
      movie.synopsis.toLowerCase().includes(lowerQuery)
  );
}

export function getMoviesByCategory(categoryName: string): Movie[] {
  const categoryKeyword = categoryName.split(" ")[0].toLowerCase();
  return mockMovies.filter((movie) =>
    movie.genres.some((genre) => genre.toLowerCase().includes(categoryKeyword))
  );
}
