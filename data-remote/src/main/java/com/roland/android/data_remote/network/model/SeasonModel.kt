package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class SeasonModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = "",
	@Json(name = "overview")
	val overview: String = "",
	@Json(name = "air_date")
	val airDate: String = "",
	@Json(name = "season_number")
	val seasonNumber: Int = 0,
	@Json(name = "episode_count")
	val episodeCount: Int? = null,
	@Json(name = "episodes")
	val episodes: List<EpisodeModel>? = null,
	@Json(name = "poster_path")
	val posterPath: String = "",
	@Json(name = "vote_average")
	val voteAverage: Double = 0.0,
)
