package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class VideoModel(
	@Json(name = "id")
	val id: String = "",
	@Json(name = "name")
	val name: String = "",
	@Json(name = "key")
	val key: String = "",
	@Json(name = "published_at")
	val publishedAt: String = "",
	@Json(name = "site")
	val site: String = "",
	@Json(name = "size")
	val size: Int = 0,
	@Json(name = "type")
	val type: String = "",
	@Json(name = "official")
	val official: Boolean = true
)
