package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class CastModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = "",
	@Json(name = "gender")
	val gender: Int = 0,
	@Json(name = "known_for_department")
	val knownForDepartment: String = "",
	@Json(name = "profile_path")
	val profilePath: String = "",
	@Json(name = "popularity")
	val popularity: Double = 0.0,
	@Json(name = "movies_acted")
	val moviesActed: List<MovieModel> = emptyList(),
	@Json(name = "adult")
	val adult: Boolean = false,
	// more details
	@Json(name = "also_known_as")
	val alsoKnownAs: List<String>? = null,
	@Json(name = "biography")
	val biography: String? = null,
	@Json(name = "birthday")
	val birthDay: String? = null,
	@Json(name = "deathday")
	val deathDay: String? = null
)
