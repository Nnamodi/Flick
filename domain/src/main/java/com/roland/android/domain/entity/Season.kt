package com.roland.android.domain.entity

data class Season(
	val id: Int = 0,
	val name: String = "",
	val overview: String = "",
	val airDate: String? = null,
	val seasonNumber: Int = 0,
	val episodeCount: Int? = null,
	val episodes: List<Episode>? = null,
	val posterPath: String? = null,
	val voteAverage: Double = 0.0,
)
