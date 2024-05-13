package com.roland.android.domain.entity

data class Series(
	val id: Int = 0,
	val name: String = "",
	val overview: String = "",
	val backdropPath: String = "",
	val adult: Boolean = false,
	val createdBy: List<Cast> = emptyList(),
	val externalIds: ExternalIds = ExternalIds(),
	val firstAirDate: String = "",
	val lastAirDate: String? = null,
	val genres: List<Genre> = emptyList(),
	val homePage: String = "",
	val inProduction: Boolean = false,
	val languages: List<String> = emptyList(),
	val lastEpisodeToAir: Episode? = null,
	val nextEpisodeToAir: Episode? = null,
	val networks: List<ProductionCompany> = emptyList(),
	val numberOfEpisodes: Int = 0,
	val numberOfSeasons: Int = 0,
	val originCountry: List<String> = emptyList(),
	val popularity: Double = 0.0,
	val posterPath: String = "",
	val productionCompanies: List<ProductionCompany> = emptyList(),
	val productionCountries: List<ProductionCountry> = emptyList(),
	val seasons: List<Season> = emptyList(),
	val spokenLanguage: List<Language> = emptyList(),
	val status: String = "",
	val tagline: String = "",
	val type: String = "",
	val voteAverage: Double = 0.0,
	val voteCount: Int = 0,
	val videos: List<Video> = emptyList(),
	val credits: MovieCredits = MovieCredits()
)

data class ExternalIds(
	val id: Int = 0,
	val imdbId: String? = null,
	val tvdbId: Int? = null,
	val wikidataId: String? = null,
	val facebookId: String? = null,
	val instagramId: String? = null,
	val twitterId: String? = null
)
