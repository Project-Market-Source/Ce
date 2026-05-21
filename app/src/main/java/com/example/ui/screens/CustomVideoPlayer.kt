package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.Movie
import kotlinx.coroutines.delay

@Composable
fun CustomVideoPlayer(
    movie: Movie,
    episodeId: String? = null,
    initialQuality: String = "1080p",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = remember { context.findActivity() }

    // Resolve stream and title
    val episode = remember(episodeId) {
        movie.episodes.find { it.id == episodeId }
    }
    val title = if (episode != null) {
         "${movie.title} - S${episode.seasonNumber}E${episode.episodeNumber}: ${episode.title}"
    } else {
         movie.title
    }

    val streamMap = if (episode != null) episode.streamUrls else movie.streamUrls
    
    // Quality selection
    var currentQuality by remember { mutableStateOf(initialQuality) }
    val streamUri = remember(currentQuality, episodeId) {
        streamMap[currentQuality] ?: streamMap.values.firstOrNull() ?: ""
    }

    // Player States
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }
    var totalDuration by remember { mutableStateOf(0) }
    var showControls by remember { mutableStateOf(true) }
    var isLandscape by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    // Keep reference to native VideoView
    var videoViewInstance by remember { mutableStateOf<VideoView?>(null) }

    // Handles Hardware Back button pressed during playback
    BackHandler {
        if (isLandscape) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            isLandscape = false
        } else {
            onBack()
        }
    }

    // Auto orientation check based on activity requested state
    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    // Hide controls automatically after a delay
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    // Update playback position ticker
    LaunchedEffect(isPlaying, streamUri) {
        while (isPlaying) {
            videoViewInstance?.let { view ->
                if (view.isPlaying) {
                    currentPosition = view.currentPosition
                    if (totalDuration == 0 || totalDuration != view.duration) {
                        totalDuration = view.duration
                    }
                }
            }
            delay(500)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("video_player_container")
    ) {
        // --- Native VideoView Renderer ---
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    setOnPreparedListener { mp ->
                        isBuffering = false
                        mp.start()
                        isPlaying = true
                        totalDuration = duration
                        // Match aspect ratio by filling center
                        mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)
                    }
                    setOnErrorListener { _, what, extra ->
                        Log.e("CustomVideoPlayer", "Error loading stream: What $what Extra $extra")
                        hasError = true
                        isBuffering = false
                        true
                    }
                    setOnCompletionListener {
                        isPlaying = false
                        currentPosition = duration
                    }
                    setVideoURI(Uri.parse(streamUri))
                    videoViewInstance = this
                }
            },
            update = { view ->
                // Monitor changes in active stream quality
                val currentVideoUri = Uri.parse(streamUri)
                if (view.tag != streamUri) {
                    view.tag = streamUri
                    isBuffering = true
                    hasError = false
                    val lastPos = currentPosition
                    view.setVideoURI(currentVideoUri)
                    view.setOnPreparedListener { mp ->
                        isBuffering = false
                        mp.seekTo(lastPos)
                        mp.start()
                        isPlaying = true
                        totalDuration = view.duration
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Capture screen click to show/hide controls overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    showControls = !showControls
                }
        )

        // --- Shadow Overlays for cinematic text visibility ---
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
                            )
                        )
                        .align(Alignment.TopCenter)
                )

                // Bottom gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                        .align(Alignment.BottomCenter)
                )
            }
        }

        // --- Buffering Indicator ---
        if (isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Loading stream...",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // --- Failure Alert ---
        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(24.dp).widthIn(max = 400.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Playback Error",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Failed to stream media",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Connecting to CDN stream failed. Ensure your network connection matches the configured base IP.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                hasError = false
                                isBuffering = true
                                videoViewInstance?.setVideoURI(Uri.parse(streamUri))
                                videoViewInstance?.start()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Retry Playback")
                        }
                    }
                }
            }
        }

        // --- HUD HUD HUD Custom Overlay controls ---
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            // HUD TOP CONTROLS
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .align(Alignment.TopCenter),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (isLandscape) {
                                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                isLandscape = false
                            } else {
                                onBack()
                            }
                        },
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .testTag("player_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1.0f)
                    )

                    // Quality Quality selection button
                    Box {
                        var expandedQualityMenu by remember { mutableStateOf(false) }
                        Button(
                            onClick = { expandedQualityMenu = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black.copy(alpha = 0.6f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("quality_select_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Quality Picker",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Quality: $currentQuality", fontSize = 12.sp)
                        }

                        DropdownMenu(
                            expanded = expandedQualityMenu,
                            onDismissRequest = { expandedQualityMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            streamMap.keys.forEach { qual ->
                                DropdownMenuItem(
                                    text = { Text(text = qual, color = Color.White) },
                                    onClick = {
                                        currentQuality = qual
                                        expandedQualityMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // HUD CENTER PLAY-PAUSE-FORWARD-REWIND KNOTS
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Rewind 10 Seconds
                    IconButton(
                        onClick = {
                            videoViewInstance?.let { view ->
                                val target = maxOf(0, view.currentPosition - 10000)
                                view.seekTo(target)
                                currentPosition = target
                            }
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .testTag("skip_backward_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Skip Backward 10 Seconds",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    // Play/Pause Play/Pause
                    IconButton(
                        onClick = {
                            videoViewInstance?.let { view ->
                                if (view.isPlaying) {
                                    view.pause()
                                    isPlaying = false
                                } else {
                                    view.start()
                                    isPlaying = true
                                }
                            }
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .testTag("player_play_pause_btn")
                    ) {
                        if (isPlaying) {
                            Text(
                                text = "||",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    // Forward 10 Seconds
                    IconButton(
                        onClick = {
                            videoViewInstance?.let { view ->
                                val target = minOf(view.duration, view.currentPosition + 10000)
                                view.seekTo(target)
                                currentPosition = target
                            }
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .testTag("skip_forward_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Skip Forward 10 Seconds",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // HUD BOTTOM TIMELINE SLIDER AND ROTATE
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .align(Alignment.BottomCenter)
                ) {
                    val progress = if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTime(currentPosition),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Slider(
                            value = progress,
                            onValueChange = { percent ->
                                videoViewInstance?.let { view ->
                                    val seekTarget = (percent * totalDuration).toInt()
                                    view.seekTo(seekTarget)
                                    currentPosition = seekTarget
                                    // Resume play is paused
                                    if (!isPlaying) {
                                        view.start()
                                        isPlaying = true
                                    }
                                }
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .testTag("player_timeline_slider")
                        )

                        Text(
                            text = formatTime(totalDuration),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Full Screen Toggle
                        IconButton(
                            onClick = {
                                if (isLandscape) {
                                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                                    isLandscape = false
                                } else {
                                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                    isLandscape = true
                                }
                            },
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .testTag("orientation_toggle_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Toggle Orientation",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// Recursively find current Activity Context Wrapper
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

// Audio Time formatter Helper function: MS -> MM:SS
private fun formatTime(millis: Int): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
