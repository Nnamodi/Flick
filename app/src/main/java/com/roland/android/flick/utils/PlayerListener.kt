package com.roland.android.flick.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo

class PlayerListener(
	private val videoKey: String?,
	private val autoPlay: Boolean = false,
	private val canPlay: Boolean = false,
	private val lifecycle: Lifecycle? = null,
	private val playerIsReady: () -> Unit = {}
) : AbstractYouTubePlayerListener() {
	override fun onReady(youTubePlayer: YouTubePlayer) {
		super.onReady(youTubePlayer)
		if (videoKey == null) return
		if (autoPlay) {
			if (lifecycle == null) return
			youTubePlayer.loadOrCueVideo(lifecycle, videoKey, 0f)
		} else {
			youTubePlayer.cueVideo(videoId = videoKey, startSeconds = 0f)
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