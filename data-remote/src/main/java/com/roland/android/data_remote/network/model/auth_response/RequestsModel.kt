package com.roland.android.data_remote.network.model.auth_response

import com.squareup.moshi.Json

data class FavoriteMediaRequestModel(
	@Json(name = "media_id")
	val mediaId: Int = 0,
	@Json(name = "media_type")
	val mediaType: String = "",
	@Json(name = "favorite")
	val favorite: Boolean = false
)

data class RateMediaRequestModel(
	@Json(name = "value")
	val value: Float = 0f
)

data class WatchlistMediaRequestModel(
	@Json(name = "media_id")
	val mediaId: Int = 0,
	@Json(name = "media_type")
	val mediaType: String = "",
	@Json(name = "wishlist")
	val watchlist: Boolean = false
)
