package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.ApiServiceManager
import com.example.data.local.AppDatabase
import com.example.data.model.Category
import com.example.data.model.Movie
import com.example.data.model.Episode
import com.example.data.repository.StreamingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Search : Screen()
    object Categories : Screen()
    object Watchlist : Screen()
    data class MovieDetail(val movieId: String) : Screen()
    data class Player(val movieId: String, val episodeId: String? = null) : Screen()
}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}

class StreamingViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = StreamingRepository(db.watchlistDao())

    // --- Navigation System with Backstack Stack Support ---
    val backStack = mutableStateListOf<Screen>(Screen.Home)
    val currentScreen: Screen get() = backStack.lastOrNull() ?: Screen.Home

    fun navigateTo(screen: Screen) {
        // Avoid adding duplicate consecutive screen states
        if (backStack.lastOrNull() != screen) {
            backStack.add(screen)
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
            return true
        }
        return false // Exits activity
    }

    // --- Repository Data Flow ---
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val watchlist: StateFlow<List<Movie>> = repository.watchlistFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Search & CDN Editing States ---
    var searchQuery by mutableStateOf("")
        private set

    var localCdnInput by mutableStateOf("")

    // List of movies filtered by search query
    val searchResults: List<Movie>
        get() = if (searchQuery.isBlank()) {
            _movies.value
        } else {
            _movies.value.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.genres.any { genre -> genre.contains(searchQuery, ignoreCase = true) }
            }
        }

    // Active Category Filter for Categories Panel
    var selectedCategory by mutableStateOf<Category?>(null)

    // --- Video Streaming Selected Options ---
    var selectedQuality by mutableStateOf("1080p") // Default High-Definition streaming

    init {
        localCdnInput = ApiServiceManager.getBaseUrl()
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                _movies.value = repository.getMovies()
                _categories.value = repository.getCategories()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                Log.e("StreamingViewModel", "Failed fetching lists", e)
                _uiState.value = UiState.Error(e.localizeMessage() ?: "An error occurred")
            }
        }
    }

    private fun Exception.localizeMessage(): String? {
        return message
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun applyCdnBaseUrl(newUrl: String) {
        ApiServiceManager.updateBaseUrl(newUrl)
        localCdnInput = ApiServiceManager.getBaseUrl()
        loadData() // Reload movies and categories from custom endpoint!
    }

    // --- Watchlist Controllers ---
    fun toggleWatchlist(movie: Movie, isInWatchlist: Boolean) {
        viewModelScope.launch {
            if (isInWatchlist) {
                repository.removeFromWatchlist(movie.id)
            } else {
                repository.addToWatchlist(movie)
            }
        }
    }

    fun isMovieInWatchlistFlow(movieId: String): Flow<Boolean> {
        return repository.isMovieInWatchlist(movieId)
    }

    // --- Helper to fetch Movie by ID ---
    fun getMovieById(id: String): Movie? {
        return _movies.value.find { it.id == id }
    }
}
