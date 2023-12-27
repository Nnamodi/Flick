package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class MovieModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "title")
	val title: String? = null,
	@Json(name = "overview")
	val overview: String = "",
	@Json(name = "genre_ids")
	val genreIds: List<Int> = emptyList(),
	@Json(name = "backdrop_path")
	val backdropPath: String? = null,
	@Json(name = "poster_path")
	val posterPath: String = "",
	@Json(name = "language")
	val language: String = "",
	@Json(name = "popularity")
	val popularity: Double = 0.0,
	@Json(name = "movie_type")
	val movieType: String? = null,
	@Json(name = "video")
	val videoAvailable: Boolean? = null,
	@Json(name = "vote_average")
	val voteAverage: Double = 0.0,
	@Json(name = "vote_count")
	val voteCount: Int = 0,
	@Json(name = "adult")
	val adult: Boolean? = null,
	@Json(name = "release_date")
	val releaseDate: String? = null,
	//tv-specific params
	@Json(name = "name")
	val tvName: String? = null,
	@Json(name = "first_air_date")
	val firstAirDate: String? = null,
	@Json(name = "original_country")
	val originalCountry: List<String>? = null
)
