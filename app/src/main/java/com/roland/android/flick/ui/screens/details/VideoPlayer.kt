package com.roland.android.flick.ui.screens.details

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.roland.android.domain.entity.Video
import com.roland.android.flick.R
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.utils.Constants.YOUTUBE_VIDEO_BASE_URL

@Composable
fun VideoPlayer(
	trailer: Video?,
	modifier: Modifier = Modifier,
	navigateUp: (Screens) -> Unit
) {
	Box {
		Player(
			video = trailer,
			modifier = modifier.fillMaxWidth()
		)
		IconButton(
			onClick = { navigateUp(Screens.Back) },
			modifier = Modifier.padding(start = 2.dp, top = 46.dp)
		) {
			Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
		}
	}
}

@OptIn(UnstableApi::class)
@Composable
private fun Player(
	video: Video?,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current
	val url = Uri.parse(YOUTUBE_VIDEO_BASE_URL + video?.key)
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