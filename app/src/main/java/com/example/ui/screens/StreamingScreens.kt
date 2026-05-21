package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Category
import com.example.data.model.Movie
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.StreamingViewModel
import com.example.ui.viewmodel.UiState
import kotlinx.coroutines.delay

// --- Fallback Image Copier Composable ---
@Composable
fun MoviePoster(
    url: String,
    title: String,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        AsyncImage(
            model = url,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            onState = { state ->
                isLoading = state is coil.compose.AsyncImagePainter.State.Loading
                isError = state is coil.compose.AsyncImagePainter.State.Error
            },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading || isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (isError) Icons.Default.Warning else Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = title,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

// --- Top App Bar Composable ---
@Composable
fun CinemanaTopAppBar(
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo + App Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CM",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
            Text(
                text = "Cinemana",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
        }

        // Actions: Search Icon + Avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // User avatar Circle
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF475569))
                    .border(1.5.dp, Color.White.copy(alpha = 0.15f), CircleShape)
            )
        }
    }
}

// --- Home Screen View ---
@Composable
fun HomeScreen(
    viewModel: StreamingViewModel,
    modifier: Modifier = Modifier
) {
    val movies by viewModel.movies.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is UiState.Error -> {
            Box(modifier = modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Connection issue: CDN Offline", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadData() }) { Text("Retry Connecting") }
                }
            }
        }
        is UiState.Success -> {
            val featuredMovies = remember(movies) { movies.take(4) }
            val latestMovies = remember(movies) { movies.filter { it.type == "movie" } }
            val topSeries = remember(movies) { movies.filter { it.type == "series" } }
            val recentlyAdded = remember(movies) { movies.reversed() }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                CinemanaTopAppBar(
                    onSearchClick = { viewModel.navigateTo(Screen.Search) }
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                // Featured Rotating Sliders
                if (featuredMovies.isNotEmpty()) {
                    item {
                        TrendingCarousel(featuredMovies, viewModel) { movie ->
                            viewModel.navigateTo(Screen.MovieDetail(movie.id))
                        }
                    }
                }

                // Horizontal section components
                item {
                    MovieRowHeader(title = "Latest Movies")
                    ScrollingMovieRow(movies = latestMovies) { movie ->
                        viewModel.navigateTo(Screen.MovieDetail(movie.id))
                    }
                }

                item {
                    MovieRowHeader(title = "Top Series")
                    ScrollingMovieRow(movies = topSeries) { movie ->
                        viewModel.navigateTo(Screen.MovieDetail(movie.id))
                    }
                }

                item {
                    MovieRowHeader(title = "Recently Added Releases")
                    ScrollingMovieRow(movies = recentlyAdded) { movie ->
                        viewModel.navigateTo(Screen.MovieDetail(movie.id))
                    }
                }
            }
        }
    }
}
}

@Composable
fun TrendingCarousel(
    featuredMovies: List<Movie>,
    viewModel: StreamingViewModel,
    onSelect: (Movie) -> Unit
) {
    var activeIdx by remember { mutableStateOf(0) }
    val watchlist by viewModel.watchlist.collectAsState()

    // Carousel Auto-Scrolling Effect
    LaunchedEffect(featuredMovies) {
        while (true) {
            delay(5000)
            if (featuredMovies.isNotEmpty()) {
                activeIdx = (activeIdx + 1) % featuredMovies.size
            }
        }
    }

    val selectedMovie = featuredMovies.getOrNull(activeIdx) ?: return
    val isSaved = watchlist.any { it.id == selectedMovie.id }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onSelect(selectedMovie) }
            .testTag("trending_carousel")
    ) {
        // Backdrop Imagery with Dark Theatre Overlays
        MoviePoster(
            url = selectedMovie.posterUrl,
            title = selectedMovie.title,
            modifier = Modifier.fillMaxSize()
        )

        // Film Shadows Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent,
                            Color(0xFF0F1014).copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Contents
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            // "Featured Today" tag
            Text(
                text = "FEATURED TODAY",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Text(
                text = selectedMovie.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Metadata row: Year, Season/Genre badges, Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = selectedMovie.year,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = selectedMovie.genres.firstOrNull() ?: "Sci-Fi",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = selectedMovie.rating,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Watch Now + Watchlist add
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onSelect(selectedMovie) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.0f)
                        .height(44.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Watch Now", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Plus or minus button to toggle watchlist
                IconButton(
                    onClick = {
                        viewModel.toggleWatchlist(selectedMovie, isSaved)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Star else Icons.Default.Add,
                        contentDescription = "Watchlist",
                        tint = if (isSaved) MaterialTheme.colorScheme.tertiary else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dot indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    featuredMovies.indices.forEach { idx ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == activeIdx) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (idx == activeIdx) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieRowHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

@Composable
fun ScrollingMovieRow(
    movies: List<Movie>,
    onSelect: (Movie) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            MovieCompactCard(movie, onSelect)
        }
    }
}

@Composable
fun MovieCompactCard(
    movie: Movie,
    onSelect: (Movie) -> Unit
) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable { onSelect(movie) }
            .testTag("movie_card_${movie.id}")
    ) {
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
        ) {
            MoviePoster(url = movie.posterUrl, title = movie.title, modifier = Modifier.fillMaxSize())

            // Floating star badge
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .align(Alignment.TopStart)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = movie.rating,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${movie.year} • ${movie.duration}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 9.sp,
            maxLines = 1
        )
    }
}

// --- Search Screen View & Collapsible local cdn settings ---
@Composable
fun SearchScreen(
    viewModel: StreamingViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCdnSettings by remember { mutableStateOf(false) }
    var cdnInputState by remember { mutableStateOf(viewModel.localCdnInput) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())

        // Search Bar Grid Layout Header with dynamic gear button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search title, series, or genres...", color = MaterialTheme.colorScheme.outline) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.outline) },
                singleLine = true,
                modifier = Modifier
                    .weight(1.0f)
                    .testTag("search_input_field")
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(
                onClick = { showCdnSettings = !showCdnSettings },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    .size(48.dp)
                    .testTag("cdn_settings_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "CDN Setup",
                    tint = if (showCdnSettings) MaterialTheme.colorScheme.secondary else Color.White
                )
            }
        }

        // Expanded Local IP/CDN Setup configuration sheet
        AnimatedVisibility(
            visible = showCdnSettings,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Local ISP CDN Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Configure local servers IP to fetch streaming catalogs without SSL restrictions (e.g., 10.15.1.4:8080/api). Defaults to fallback mock lists if blank.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = cdnInputState,
                            onValueChange = { cdnInputState = it },
                            placeholder = { Text("http://192.168.1.1/:8000/api", fontSize = 12.sp) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                            modifier = Modifier
                                .weight(1.0f)
                                .height(50.dp)
                                .testTag("cdn_ip_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                viewModel.applyCdnBaseUrl(cdnInputState)
                                Toast.makeText(context, "CDN Service URL applied", Toast.LENGTH_SHORT).show()
                                showCdnSettings = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(44.dp).testTag("cdn_connect_btn")
                        ) {
                            Text("Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Search Results List
        val results = viewModel.searchResults
        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No movies or series found",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 0.dp, top = 4.dp, end = 0.dp, bottom = 90.dp),
                modifier = Modifier
                    .weight(1.0f)
                    .testTag("search_grid")
            ) {
                items(results) { movie ->
                    MovieCompactCard(movie) {
                        viewModel.navigateTo(Screen.MovieDetail(movie.id))
                    }
                }
            }
        }
    }
}

// --- Categories Screen View ---
@Composable
fun CategoriesScreen(
    viewModel: StreamingViewModel,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val activeCategory = viewModel.selectedCategory

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (activeCategory == null) {
            CinemanaTopAppBar(
                onSearchClick = { viewModel.navigateTo(Screen.Search) }
            )
        } else {
            Spacer(modifier = Modifier.statusBarsPadding())
        }

        if (activeCategory == null) {
            // Main category Grid
            Text(
                text = "Cinemana Categories",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1.0f)
                    .testTag("categories_grid")
            ) {
                items(categories) { category ->
                    CategoryBlockButton(category) {
                        viewModel.selectedCategory = category
                    }
                }
            }
        } else {
            // Filter Results display layout
            val filterMovies = remember(activeCategory, movies) {
                movies.filter { movie ->
                    movie.genres.any { genre ->
                        genre.contains(activeCategory.name.split(" ").first(), ignoreCase = true)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillOuterWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.selectedCategory = null },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .size(36.dp)
                        .testTag("category_back_btn")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = activeCategory.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filterMovies.size} content pieces available",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }

            if (filterMovies.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No listings inside ${activeCategory.name} yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filterMovies) { movie ->
                        MovieCompactCard(movie) {
                            viewModel.navigateTo(Screen.MovieDetail(movie.id))
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.fillOuterWidth(): Modifier = this.fillMaxWidth()

@Composable
fun CategoryBlockButton(
    category: Category,
    onClick: () -> Unit
) {
    // Map icons strings to nice modern system graphics vector
    val iconVec = when (category.iconName) {
        "local_fire_department" -> Icons.Default.ThumbUp
        "rocket_launch" -> Icons.Default.Star
        "theater_comedy" -> Icons.Default.Face
        "trending_up" -> Icons.Default.Star
        "router" -> Icons.Default.Settings
        else -> Icons.Default.Refresh
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable { onClick() }
            .testTag("category_item_${category.id}"),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Neon top-end light accent representation
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(28.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVec,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = category.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

// --- Watchlist Screen View ---
@Composable
fun WatchlistScreen(
    viewModel: StreamingViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.watchlist.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CinemanaTopAppBar(
            onSearchClick = { viewModel.navigateTo(Screen.Search) }
        )

        Text(
            text = "My Watchlist",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your Watchlist is empty",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tab a movie or show from home and save it to watch offline later on local ISP lines.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1.0f)
                    .testTag("watchlist_grid")
            ) {
                items(items) { movie ->
                    MovieCompactCard(movie) {
                        viewModel.navigateTo(Screen.MovieDetail(movie.id))
                    }
                }
            }
        }
    }
}

// --- Movie Details Screen View ---
@Composable
fun MovieDetailScreen(
    movieId: String,
    viewModel: StreamingViewModel,
    onBack: () -> Unit
) {
    val movie = remember(movieId) { viewModel.getMovieById(movieId) } ?: return
    val isSaved by viewModel.isMovieInWatchlistFlow(movieId).collectAsState(initial = false)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("movie_details_screen"),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (movie.type == "series") {
                        // Launch player with first episode
                        val firstEpId = movie.episodes.firstOrNull()?.id
                        viewModel.navigateTo(Screen.Player(movie.id, firstEpId))
                    } else {
                        viewModel.navigateTo(Screen.Player(movie.id))
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
                    .height(48.dp)
                    .testTag("watch_now_fab")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "WATCH STREAMING NOW",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // High Quality Parallax Backdrop Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    MoviePoster(
                        url = movie.posterUrl,
                        title = movie.title,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay Dark Bottom and Top Gradients
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )

                    // Control buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(16.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(36.dp)
                                .testTag("details_back_btn")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }

                        IconButton(
                            onClick = { viewModel.toggleWatchlist(movie, isSaved) },
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(36.dp)
                                .testTag("watchlist_toggle_btn")
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Save Watchlist",
                                tint = if (isSaved) MaterialTheme.colorScheme.secondary else Color.White
                            )
                        }
                    }

                    // Bottom info overlays
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        // Badges Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.tertiary)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = movie.rating, color = MaterialTheme.colorScheme.tertiary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(text = movie.year, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = movie.duration, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Box(
                                modifier = Modifier
                                    .border(0.5.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(text = movie.type.uppercase(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = movie.title,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Specs, Genres and Synopsis
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Genres list
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        movie.genres.forEach { genre ->
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(text = genre, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Text(
                        text = "Synopsis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = movie.synopsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            // TV Show Episodes Layout (Only if Type == series)
            if (movie.type == "series" && movie.episodes.isNotEmpty()) {
                item {
                    Text(
                        text = "Episodes (${movie.episodes.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(movie.episodes) { ep ->
                    EpisodeListTile(ep) {
                        viewModel.navigateTo(Screen.Player(movie.id, ep.id))
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeListTile(
    episode: com.example.data.model.Episode,
    onSelect: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onSelect() }
            .testTag("episode_tile_${episode.id}"),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.04f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // S1E1 representation icon block
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S${episode.seasonNumber}E${episode.episodeNumber}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = episode.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = "Quality: 1080p • 720p • 360p Available",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Watch Episode",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
