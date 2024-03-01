package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class MultiModel(
	// movie-specific params
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "title")
	val title: String? = null,
	@Json(name = "overview")
	val overview: String? = null,
	@Json(name = "genre_ids")
	val genreIds: List<Int>? = null,
	@Json(name = "backdrop_path")
	val backdropPath: String? = null,
	@Json(name = "poster_path")
	val posterPath: String? = null,
	@Json(name = "original_language")
	val language: String? = null,
	@Json(name = "popularity")
	val popularity: Double = 0.0,
	@Json(name = "media_type")
	val mediaType: String = "", // movie, tv, person
	@Json(name = "video")
	val videoAvailable: Boolean? = null,
	@Json(name = "vote_average")
	val voteAverage: Double? = null,
	@Json(name = "vote_count")
	val voteCount: Int? = null,
	@Json(name = "adult")
	val adult: Boolean? = null,
	@Json(name = "release_date")
	val releaseDate: String? = null,

	// tv-specific params
	@Json(name = "name")
	val tvName: String? = null,
	@Json(name = "first_air_date")
	val firstAirDate: String? = null,
	@Json(name = "origin_country")
	val originCountry: List<String>? = null,

	// person-specific params
	@Json(name = "gender")
	val gender: Int? = null,
	@Json(name = "known_for_department")
	val knownForDepartment: String? = null,
	@Json(name = "profile_path")
	val profilePath: String? = null,
	@Json(name = "known_for")
	val moviesActed: List<MovieModel>? = null,
)
