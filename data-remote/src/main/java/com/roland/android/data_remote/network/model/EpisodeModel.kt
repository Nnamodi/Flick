package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class EpisodeModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = "",
	@Json(name = "overview")
	val overview: String = "",
	@Json(name = "air_date")
	val airDate: String = "",
	@Json(name = "episode_number")
	val episodeNumber: Int = 0,
	@Json(name = "episode_type")
	val episodeType: String = "",
	@Json(name = "production_code")
	val productionCode: String = "",
	@Json(name = "season_number")
	val seasonNumber: Int = 0,
	@Json(name = "show_id")
	val showId: Int = 0,
	@Json(name = "still_path")
	val stillPath: String = "",
	@Json(name = "vote_average")
	val voteAverage: Double = 0.0,
	@Json(name = "vote_count")
	val voteCount: Int = 0
)
