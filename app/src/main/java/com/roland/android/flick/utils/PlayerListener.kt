package com.roland.android.flick.utils

import android.util.Log
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class PlayerListener(
	private val videoKey: String?,
	private val autoPlay: Boolean,
	private val canPlay: Boolean
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

	override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
		super.onCurrentSecond(youTubePlayer, second)
		if (!canPlay) youTubePlayer.pause()
		Log.i("VideoPlayerInfo", "$second ----- $canPlay")
	}
}