package com.roland.android.flick.ui.screens.details

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.roland.android.domain.entity.Video
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.VideoThumbnail
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_SMALL
import com.roland.android.flick.utils.Constants.YOUTUBE_VIDEO_BASE_URL
import com.roland.android.flick.utils.PlayerListener

@Composable
fun VideoPlayer(
	video: Video,
	modifier: Modifier = Modifier,
	canPlay: Boolean
) {
	Column(modifier) {
		Player(
			videoKey = video.key,
			modifier = Modifier.height(POSTER_HEIGHT_SMALL),
			autoPlay = false,
			canPlay = canPlay
		)
		Text(
			text = video.name,
			modifier = Modifier.padding(top = 8.dp),
			overflow = TextOverflow.Ellipsis,
			maxLines = 2
		)
	}
}

@Composable
fun VideoPlayer(
	trailerKey: String?,
	thumbnail: String,
	modifier: Modifier = Modifier,
	autoPlay: Boolean,
	enabled: Boolean = true,
	navigateUp: (Screens) -> Unit
) {
	Box {
		Player(
			videoKey = trailerKey,
			modifier = modifier.fillMaxWidth(),
			autoPlay = autoPlay,
			thumbnail = thumbnail
		)
		IconButton(
			onClick = { navigateUp(Screens.Back) },
			modifier = Modifier.padding(start = 2.dp, top = 46.dp),
			enabled = enabled,
			colors = iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
		) {
			Icon(
				imageVector = Icons.Rounded.ArrowBackIos,
				contentDescription = stringResource(R.string.back),
				tint = Color.White
			)
		}
	}
}

@Composable
private fun Player(
	videoKey: String?,
	modifier: Modifier = Modifier,
	thumbnail: String = "",
	autoPlay: Boolean = true,
	canPlay: Boolean = true
) {
	val view = YouTubePlayerView(LocalContext.current)
	val playerIsReady = rememberSaveable { mutableStateOf(false) }
	val canPlayVideo = rememberSaveable(canPlay) { mutableStateOf(canPlay) }
	var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
	val lifecycleOwner = LocalLifecycleOwner.current

	Box(modifier.drawBehind { drawRect(Color.Black) }) {
		AndroidView(
			factory = {
				view.addYouTubePlayerListener(
					PlayerListener(
						videoKey, autoPlay, canPlayVideo.value,
						lifecycle = lifecycleOwner.lifecycle,
						playerIsReady = { videoKey?.let { playerIsReady.value = true } }
					)
				)
				view
			},
			modifier = Modifier.fillMaxSize(),
			update = {
				when (lifecycle) {
					Lifecycle.Event.ON_PAUSE -> canPlayVideo.value = false
					Lifecycle.Event.ON_DESTROY -> {
						view.removeYouTubePlayerListener(PlayerListener(videoKey))
						view.release()
					}
					else -> {}
				}
			}
		)
		if (!playerIsReady.value) {
			VideoThumbnail(
				thumbnail = thumbnail,
				trailerAvailable = videoKey != null,
				modifier = Modifier.fillMaxSize()
			)
		}
	}

	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			lifecycle = event
		}
		lifecycleOwner.lifecycle.addObserver(observer)

		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
			// in case the player has not loaded yet
			view.removeYouTubePlayerListener(PlayerListener(videoKey))
			view.release()
		}
	}
}

@OptIn(UnstableApi::class)
@Composable
private fun ExoPlayer(
	videoKey: String?,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current
	val url = Uri.parse(YOUTUBE_VIDEO_BASE_URL + videoKey)
	val exoPlayer = remember {
		ExoPlayer.Builder(context).build().apply {
			setMediaItem(MediaItem.fromUri(url))
			prepare()
			playWhenReady = true
		}
	}

	AndroidView(
		modifier = modifier,
		factory = { playerContext ->
			PlayerView(playerContext).also {
				it.apply {
					player = exoPlayer
					setShowNextButton(false)
					setShowPreviousButton(false)
				}
			}
		}
	)

	DisposableEffect(Unit) {
		onDispose(exoPlayer::release)
	}
}