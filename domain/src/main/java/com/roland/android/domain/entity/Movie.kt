package com.roland.android.domain.entity

data class Movie(
	val id: Int = 0,
	val title: String? = null,
	val overview: String = "",
	val genreIds: List<Int> = emptyList(),
	val backdropPath: String? = null,
	val posterPath: String = "",
	val language: String = "",
	val popularity: Double = 0.0,
	val movieType: String? = null,
	val videoAvailable: Boolean? = null,
	val voteAverage: Double = 0.0,
	val voteCount: Int = 0,
	val adult: Boolean? = null,
	val releaseDate: String? = null,
	//tv-specific params
	val tvName: String? = null,
	val firstAirDate: String? = null,
	val originCountry: List<String>? = null
)
