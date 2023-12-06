package com.roland.android.domain.entity

data class Episode(
	val id: Int = 0,
	val name: String = "",
	val overview: String = "",
	val airDate: String = "",
	val episodeNumber: Int = 0,
	val episodeType: String = "",
	val productionCode: String = "",
	val seasonNumber: Int = 0,
	val showId: Int = 0,
	val stillPath: String = "",
	val voteAverage: Double = 0.0,
	val voteCount: Int = 0
)
