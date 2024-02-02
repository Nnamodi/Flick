package com.roland.android.domain.entity

data class MovieList(
	val dates: Dates? = null,
	val page: Int = 0,
	val results: List<Movie> = emptyList(),
	val totalPages: Int = 0,
	val totalResults: Int = 0
)

data class MovieCredits(
	val cast: List<Cast> = emptyList()
)

data class Dates(
	val maximum: String = "",
	val minimum: String = ""
)
