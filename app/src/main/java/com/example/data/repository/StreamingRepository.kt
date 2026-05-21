package com.example.data.repository

import android.util.Log
import com.example.data.api.ApiServiceManager
import com.example.data.api.MovieDto
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistMovie
import com.example.data.model.Category
import com.example.data.model.Episode
import com.example.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StreamingRepository(private val watchlistDao: WatchlistDao) {

    // --- Watchlist DB Operations ---
    val watchlistFlow: Flow<List<Movie>> = watchlistDao.getAllWatchlist()
        .map { list ->
            list.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    type = entity.type,
                    year = entity.year,
                    rating = entity.rating,
                    genres = entity.genresString.split(",").filter { it.isNotEmpty() },
                    synopsis = entity.synopsis,
                    posterUrl = entity.posterUrl,
                    duration = entity.duration,
                    streamUrls = mapOf(
                        "1080p" to MOCK_STREAMS["1080p"]!!,
                        "720p" to MOCK_STREAMS["720p"]!!,
                        "360p" to MOCK_STREAMS["360p"]!!
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    suspend fun addToWatchlist(movie: Movie) = withContext(Dispatchers.IO) {
        watchlistDao.addToWatchlist(
            WatchlistMovie(
                id = movie.id,
                title = movie.title,
                type = movie.type,
                year = movie.year,
                rating = movie.rating,
                genresString = movie.genres.joinToString(","),
                synopsis = movie.synopsis,
                posterUrl = movie.posterUrl,
                duration = movie.duration
            )
        )
    }

    suspend fun removeFromWatchlist(movieId: String) = withContext(Dispatchers.IO) {
        watchlistDao.removeFromWatchlist(movieId)
    }

    fun isMovieInWatchlist(movieId: String): Flow<Boolean> {
        return watchlistDao.isInWatchlist(movieId).flowOn(Dispatchers.IO)
    }

    // --- Dynamic API Fetching with Offline Failure Protection ---
    suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        val currentUrl = ApiServiceManager.getBaseUrl()
        if (currentUrl == "https://mock.cinemana.local/") {
            Log.d("StreamingRepository", "Operating in safe-mock mode.")
            return@withContext getMockMovies()
        }

        try {
            val service = ApiServiceManager.getService()
            val response = service.getMovies()
            return@withContext response.map { mapDtoToMovie(it) }
        } catch (e: Exception) {
            Log.e("StreamingRepository", "Local network CDN request failed, falling back to local ISP simulation model.", e)
            return@withContext getMockMovies()
        }
    }

    suspend fun getMovieDetail(id: String): Movie? = withContext(Dispatchers.IO) {
        val currentUrl = ApiServiceManager.getBaseUrl()
        if (currentUrl == "https://mock.cinemana.local/") {
            return@withContext getMockMovies().find { it.id == id }
        }
        try {
            val service = ApiServiceManager.getService()
            val response = service.getMovieDetail(id)
            return@withContext mapDtoToMovie(response)
        } catch (e: Exception) {
            Log.e("StreamingRepository", "Failed loading movie detailed endpoint, finding local representation.", e)
            return@withContext getMockMovies().find { it.id == id }
        }
    }

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val currentUrl = ApiServiceManager.getBaseUrl()
        if (currentUrl == "https://mock.cinemana.local/") {
            return@withContext getMockCategories()
        }
        try {
            val service = ApiServiceManager.getService()
            val response = service.getCategories()
            return@withContext response.map { Category(it.id, it.name, it.iconName) }
        } catch (e: Exception) {
            Log.e("StreamingRepository", "Categories download failed, injecting local list.", e)
            return@withContext getMockCategories()
        }
    }

    private fun mapDtoToMovie(dto: MovieDto): Movie {
        val mappedStreams = mutableMapOf<String, String>()
        dto.stream_1080p?.let { mappedStreams["1080p"] = it }
        dto.stream_720p?.let { mappedStreams["720p"] = it }
        dto.stream_360p?.let { mappedStreams["360p"] = it }

        // Fallback Streams if DTO was empty on certain qualities
        if (mappedStreams.isEmpty()) {
            mappedStreams["1080p"] = MOCK_STREAMS["1080p"]!!
            mappedStreams["720p"] = MOCK_STREAMS["720p"]!!
            mappedStreams["360p"] = MOCK_STREAMS["360p"]!!
        }

        return Movie(
            id = dto.id,
            title = dto.title,
            type = dto.type,
            year = dto.year,
            rating = dto.rating,
            genres = dto.genres,
            synopsis = dto.synopsis,
            posterUrl = dto.posterUrl,
            duration = dto.duration,
            streamUrls = mappedStreams,
            episodes = dto.episodes?.map { ep ->
                val epStreams = mutableMapOf<String, String>()
                ep.stream_1080p?.let { epStreams["1080p"] = it }
                ep.stream_720p?.let { epStreams["720p"] = it }
                ep.stream_360p?.let { epStreams["360p"] = it }
                if (epStreams.isEmpty()) {
                    epStreams["1080p"] = MOCK_STREAMS["1080p"]!!
                    epStreams["720p"] = MOCK_STREAMS["720p"]!!
                    epStreams["360p"] = MOCK_STREAMS["360p"]!!
                }
                Episode(
                    id = ep.id,
                    title = ep.title,
                    episodeNumber = ep.episodeNumber,
                    seasonNumber = ep.seasonNumber,
                    streamUrls = epStreams
                )
            } ?: emptyList()
        )
    }

    companion object {
        // High Quality Public Domain MP4 Streams for robust performance assessment
        private val MOCK_STREAMS = mapOf(
            "1080p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            "720p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            "360p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        )

        fun getMockCategories(): List<Category> {
            return listOf(
                Category("1", "Action Thrillers", "local_fire_department"),
                Category("2", "Sci-Fi Space", "rocket_launch"),
                Category("3", "Crime & Drama", "theater_comedy"),
                Category("4", "Trending Shows", "trending_up"),
                Category("5", "Local ISP Premium", "router"),
                Category("6", "Recently Added", "schedule")
            )
        }

        fun getMockMovies(): List<Movie> {
            return listOf(
                Movie(
                    id = "m1",
                    title = "Tears of Steel",
                    type = "movie",
                    year = "2024",
                    rating = "8.4",
                    genres = listOf("Sci-Fi", "Action", "Cyberpunk"),
                    synopsis = "Set in a dystopian future where robots have seized control, a rogue band of software engineers and warriors try to salvage humanity's fate using forgotten neural arrays in the heart of Amsterdam.",
                    posterUrl = "https://images.unsplash.com/photo-1578301978693-85fa9c0320b9?auto=format&fit=crop&q=80&w=600",
                    duration = "1h 52m",
                    streamUrls = MOCK_STREAMS
                ),
                Movie(
                    id = "m2",
                    title = "The Cosmos Within",
                    type = "movie",
                    year = "2025",
                    rating = "9.1",
                    genres = listOf("Space", "Sci-Fi", "Documentary"),
                    synopsis = "A cinematic journey exploring the boundless expanses of our outer cosmos, contrasted against the deep internal neural pathways of human synapses.",
                    posterUrl = "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&q=80&w=600",
                    duration = "2h 10m",
                    streamUrls = mapOf(
                        "1080p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                        "720p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                        "360p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
                    )
                ),
                Movie(
                    id = "m3",
                    title = "Tokyo Driftway",
                    type = "movie",
                    year = "2023",
                    rating = "7.8",
                    genres = listOf("Action", "Racing", "Suspense"),
                    synopsis = "Underground racing teams navigate neon-lit alleys of neo-Tokyo using prototype vehicles built on local network optical nodes, bypassing city surveillance networks.",
                    posterUrl = "https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&q=80&w=600",
                    duration = "1h 45m",
                    streamUrls = MOCK_STREAMS
                ),
                Movie(
                    id = "m4",
                    title = "Neon Sins",
                    type = "series",
                    year = "2024",
                    rating = "8.9",
                    genres = listOf("Crime", "Mystery", "Noir"),
                    synopsis = "A private investigator on a cybernetic city block is hired to track down missing databases holding the key to the local server grids supplying offline video libraries.",
                    posterUrl = "https://images.unsplash.com/photo-1514306191717-452ec28c7814?auto=format&fit=crop&q=80&w=600",
                    duration = "1 Season",
                    streamUrls = MOCK_STREAMS,
                    episodes = listOf(
                        Episode(
                            id = "e1_1",
                            title = "The Grid Unlocked",
                            episodeNumber = 1,
                            seasonNumber = 1,
                            streamUrls = mapOf(
                                "1080p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                "720p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                                "360p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                            )
                        ),
                        Episode(
                            id = "e1_2",
                            title = "Dark Fiber",
                            episodeNumber = 2,
                            seasonNumber = 1,
                            streamUrls = mapOf(
                                "1080p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                                "720p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                                "360p" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
                            )
                        ),
                        Episode(
                            id = "e1_3",
                            title = "The CDN Heist",
                            episodeNumber = 3,
                            seasonNumber = 1,
                            streamUrls = MOCK_STREAMS
                        )
                    )
                ),
                Movie(
                    id = "m5",
                    title = "Whispers found in Dust",
                    type = "movie",
                    year = "2024",
                    rating = "7.9",
                    genres = listOf("Thriller", "Drama"),
                    synopsis = "In an abandoned wind-farm in central Arizona, researchers discover encrypted low-frequency audio tracks that tell a story of an impending cosmic rupture.",
                    posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&q=80&w=600",
                    duration = "1h 51m",
                    streamUrls = MOCK_STREAMS
                ),
                Movie(
                    id = "m6",
                    title = "Deep Space Heist",
                    type = "series",
                    year = "2025",
                    rating = "8.6",
                    genres = listOf("Sci-Fi", "Comedy"),
                    synopsis = "A band of cosmic mercenaries attempts to steal server archives holding ancient classical terrestrial video files. A humorous, action-packed high-speed space chase.",
                    posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&q=80&w=600",
                    duration = "2 Seasons",
                    streamUrls = MOCK_STREAMS,
                    episodes = listOf(
                        Episode(
                            id = "e2_1",
                            title = "Pilot: Storage Full",
                            episodeNumber = 1,
                            seasonNumber = 1,
                            streamUrls = MOCK_STREAMS
                        ),
                        Episode(
                            id = "e2_2",
                            title = "The Helium Vault",
                            episodeNumber = 2,
                            seasonNumber = 1,
                            streamUrls = MOCK_STREAMS
                        )
                    )
                ),
                Movie(
                    id = "m7",
                    title = "Retro Beats: Synthesizer Saga",
                    type = "movie",
                    year = "2024",
                    rating = "8.2",
                    genres = listOf("Music", "Documentary"),
                    synopsis = "Chronicles the evolution of early electronic modular music and its modern-day surge on indie internet broadcasts across isolated community relays.",
                    posterUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&q=80&w=600",
                    duration = "1h 35m",
                    streamUrls = MOCK_STREAMS
                )
            )
        }
    }
}
