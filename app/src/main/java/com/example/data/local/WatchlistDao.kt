package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movie: WatchlistMovie)

    @Query("DELETE FROM watchlist WHERE id = :movieId")
    suspend fun removeFromWatchlist(movieId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :movieId)")
    fun isInWatchlist(movieId: String): Flow<Boolean>
}
