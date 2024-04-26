package com.roland.android.flick.ui.screens.details

import android.content.Context

sealed class MovieDetailsActions {

	data class FavoriteMedia(
		val mediaId: Int,
		val mediaType: String
	) : MovieDetailsActions()

	data class AddToWatchlist(
		val mediaId: Int,
		val mediaType: String
	) : MovieDetailsActions()

	data class RateMedia(
		val mediaId: Int,
		val mediaType: String,
		val rateValue: Float
	) : MovieDetailsActions()

	data class Share(
		val mediaUrl: String,
		val context: Context
	) : MovieDetailsActions()

}
