package com.example.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface CinemanaApiService {
    @GET("movies")
    suspend fun getMovies(): List<MovieDto>

    @GET("movies/{id}")
    suspend fun getMovieDetail(@Path("id") id: String): MovieDto

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}

data class MovieDto(
    val id: String,
    val title: String,
    val type: String, // "movie" or "series"
    val year: String,
    val rating: String,
    val genres: List<String>,
    val synopsis: String,
    val posterUrl: String,
    val duration: String,
    val stream_1080p: String?,
    val stream_720p: String?,
    val stream_360p: String?,
    val episodes: List<EpisodeDto>? = null
)

data class EpisodeDto(
    val id: String,
    val title: String,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val stream_1080p: String?,
    val stream_720p: String?,
    val stream_360p: String?
)

data class CategoryDto(
    val id: String,
    val name: String,
    val iconName: String
)

object ApiServiceManager {
    private var baseIpOrUrl: String = "https://mock.cinemana.local/"
    private var cachedService: CinemanaApiService? = null

    fun updateBaseUrl(newUrl: String) {
        var formatted = newUrl.trim()
        if (formatted.isEmpty()) {
            baseIpOrUrl = "https://mock.cinemana.local/"
            cachedService = null
            return
        }
        if (!formatted.endsWith("/")) {
            formatted += "/"
        }
        if (!formatted.startsWith("http://") && !formatted.startsWith("https://")) {
            formatted = "http://$formatted"
        }
        baseIpOrUrl = formatted
        cachedService = null
    }

    fun getBaseUrl(): String = baseIpOrUrl

    fun getService(): CinemanaApiService {
        cachedService?.let { return it }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(4, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseIpOrUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(CinemanaApiService::class.java)
        cachedService = service
        return service
    }
}
