package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class MovieListModel(
	@Json(name = "dates")
	val dates: DatesModel? = null,
	@Json(name = "page")
	val page: Int = 0,
	@Json(name = "results")
	val results: List<MovieModel> = emptyList(),
	@Json(name = "total_pages")
	val totalPages: Int = 0,
	@Json(name = "total_results")
	val totalResults: Int = 0
)

data class CastListModel(
	@Json(name = "page")
	val page: Int = 0,
	@Json(name = "results")
	val results: List<CastModel> = emptyList(),
	@Json(name = "total_pages")
	val totalPages: Int = 0,
	@Json(name = "total_results")
	val totalResults: Int = 0
)

data class MovieCreditsModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "casts")
	val casts: List<CastModel> = emptyList()
)

data class DatesModel(
	@Json(name = "maximum")
	val maximum: String = "",
	@Json(name = "minimum")
	val minimum: String = ""
)
