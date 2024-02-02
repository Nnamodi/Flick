package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class SeriesModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = "",
	@Json(name = "overview")
	val overview: String = "",
	@Json(name = "backdrop_path")
	val backdropPath: String = "",
	@Json(name = "adult")
	val adult: Boolean = false,
	@Json(name = "created_by")
	val createdBy: List<CastModel> = emptyList(),
	@Json(name = "first_air_date")
	val firstAirDate: String = "",
	@Json(name = "last_air_date")
	val lastAirDate: String = "",
	@Json(name = "genre_ids")
	val genres: List<GenreModel> = emptyList(),
	@Json(name = "homepage")
	val homePage: String = "",
	@Json(name = "in_production")
	val inProduction: Boolean = false,
	@Json(name = "languages")
	val languages: List<String> = emptyList(),
	@Json(name = "last_episode_to_air")
	val lastEpisodeToAir: EpisodeModel = EpisodeModel(),
	@Json(name = "next_episode_to_air")
	val nextEpisodeToAir: EpisodeModel = EpisodeModel(),
	@Json(name = "networks")
	val networks: List<ProductionCompanyModel> = emptyList(),
	@Json(name = "number_of_episodes")
	val numberOfEpisodes: Int = 0,
	@Json(name = "number_of_seasons")
	val numberOfSeasons: Int = 0,
	@Json(name = "origin_country")
	val originCountry: List<String> = emptyList(),
	@Json(name = "popularity")
	val popularity: Double = 0.0,
	@Json(name = "poster_path")
	val posterPath: String = "",
	@Json(name = "production_companies")
	val productionCompanies: List<ProductionCompanyModel> = emptyList(),
	@Json(name = "production_countries")
	val productionCountries: List<ProductionCountryModel> = emptyList(),
	@Json(name = "seasons")
	val seasons: List<SeasonModel> = emptyList(),
	@Json(name = "spoken_languages")
	val spokenLanguage: List<LanguageModel> = emptyList(),
	@Json(name = "status")
	val status: String = "",
	@Json(name = "tag_line")
	val tagline: String = "",
	@Json(name = "type")
	val type: String = "",
	@Json(name = "vote_average")
	val voteAverage: Double = 0.0,
	@Json(name = "vote_count")
	val voteCount: Int = 0,
	@Json(name = "videos")
	val videos: VideoListModel = VideoListModel(),
	@Json(name = "credits")
	val credits: MovieCreditsModel = MovieCreditsModel()
)
