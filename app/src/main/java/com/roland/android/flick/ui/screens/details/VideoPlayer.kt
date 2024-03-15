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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
	enabled: Boolean = true,
	navigateUp: (Screens) -> Unit
) {
	Box {
		Player(
			videoKey = trailerKey,
			modifier = modifier.fillMaxWidth(),
			thumbnail = thumbnail
		)
		IconButton(
			onClick = { navigateUp(Screens.Back) },
			modifier = Modifier.padding(start = 2.dp, top = 46.dp),
			enabled = enabled,
			colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))
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

	Box(modifier) {
		AndroidView(
			modifier = Modifier.fillMaxSize(),
			factory = {
				view.addYouTubePlayerListener(
					PlayerListener(
						videoKey, autoPlay, canPlay,
						playerIsReady = { playerIsReady.value = true }
					)
				)
				view
			}
		)
		if (!playerIsReady.value) {
			VideoThumbnail(
				thumbnail = thumbnail,
				modifier = Modifier.fillMaxSize()
			)
		}
	}

	DisposableEffect(Unit) {
		onDispose {
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