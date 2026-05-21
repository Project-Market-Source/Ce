package com.example.data.model

data class Movie(
    val id: String,
    val title: String,
    val type: String, // "movie" or "series"
    val year: String,
    val rating: String,
    val genres: List<String>,
    val synopsis: String,
    val posterUrl: String,
    val duration: String,
    val streamUrls: Map<String, String>, // e.g., view qualities MapOf("1080p" to "url...", "720p" to "url...")
    val episodes: List<Episode> = emptyList() // If type == "series"
)

data class Episode(
    val id: String,
    val title: String,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val streamUrls: Map<String, String>
)

data class Category(
    val id: String,
    val name: String,
    val iconName: String
)
