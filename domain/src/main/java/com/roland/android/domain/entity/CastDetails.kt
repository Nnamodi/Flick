package com.roland.android.domain.entity

data class CastDetails(
	val id: Int = 0,
	val name: String = "",
	val gender: Int = 0,
	val knownForDepartment: String = "",
	val profilePath: String? = null,
	val popularity: Double = 0.0,
	val adult: Boolean = false,
	val alsoKnownAs: List<String>? = null,
	val biography: String? = null,
	val birthDay: String? = null,
	val deathDay: String? = null,
	val moviesAndShowsActed: List<Movie> = emptyList()
)
