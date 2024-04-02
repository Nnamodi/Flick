package com.roland.android.domain.entity.auth_response

data class FavoriteMediaRequest(
	val mediaId: Int = 0,
	val mediaType: String = "",
	val favorite: Boolean = false
)

data class RateMediaRequest(
	val value: Float = 0f
)

data class WatchlistMediaRequest(
	val mediaId: Int = 0,
	val mediaType: String = "",
	val watchlist: Boolean = false
)