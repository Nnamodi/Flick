package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class CastModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = "",
	@Json(name = "profile_path")
	val profilePath: String? = null,
	@Json(name = "character")
	val character: String = "",
	@Json(name = "cast_id")
	val castId: Int = 0,
	@Json(name = "credit_id")
	val creditId: String = "",
	@Json(name = "order")
	val order: Int = 0
)
