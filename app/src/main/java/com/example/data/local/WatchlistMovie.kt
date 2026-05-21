package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistMovie(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "movie" or "series"
    val year: String,
    val rating: String,
    val genresString: String, // comma-separated strings
    val synopsis: String,
    val posterUrl: String,
    val duration: String,
    val addedAt: Long = System.currentTimeMillis()
)
