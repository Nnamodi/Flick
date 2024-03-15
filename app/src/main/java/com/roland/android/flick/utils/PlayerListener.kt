package com.roland.android.flick.utils

import android.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class PlayerListener(
	private val videoKey: String?,
	private val autoPlay: Boolean = false,
	private val canPlay: Boolean = false,
	private val playerIsReady: () -> Unit = {}
) : AbstractYouTubePlayerListener() {
	override fun onReady(youTubePlayer: YouTubePlayer) {
		super.onReady(youTubePlayer)
		youTubePlayer.apply {
			if (autoPlay) {
				loadVideo(videoId = videoKey ?: "", startSeconds = 0f)
			} else {
				cueVideo(videoId = videoKey ?: "", startSeconds = 0f)
			}
		}
		Log.i("VideoPlayerInfo", "Player ready")
	}

	override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerState) {
		super.onStateChange(youTubePlayer, state)
		when (state) {
			PlayerState.VIDEO_CUED -> playerIsReady()
			PlayerState.UNSTARTED -> playerIsReady()
			else -> {}
		}
		Log.i("VideoPlayerInfo", "$state")
	}

	override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
		super.onCurrentSecond(youTubePlayer, second)
		if (!canPlay) youTubePlayer.pause()
		Log.i("VideoPlayerInfo", "$second ----- $canPlay")
	}
}