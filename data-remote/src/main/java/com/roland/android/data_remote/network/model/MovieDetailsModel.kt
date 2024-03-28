package com.roland.android.data_remote.network.model

import com.squareup.moshi.Json

data class MovieDetailsModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "title")
	val title: String? = null,
	@Json(name = "overview")
	val overview: String = "",
	@Json(name = "imdb_id")
	val imdbId: String? = null,
	@Json(name = "genres")
	val genres: List<GenreModel> = emptyList(),
	@Json(name = "backdrop_path")
	val backdropPath: String = "",
	@Json(name = "poster_path")
	val posterPath: String = "",
	@Json(name = "original_language")
	val language: String = "",
	@Json(name = "popularity")
	val popularity: Double = 0.0,
	@Json(name = "budget")
	val budget: Int = 0,
	@Json(name = "homepage")
	val homePage: String = "",
	@Json(name = "video")
	val videoAvailable: Boolean? = null,
	@Json(name = "vote_average")
	val voteAverage: Double = 0.0,
	@Json(name = "vote_count")
	val voteCount: Int = 0,
	@Json(name = "adult")
	val adult: Boolean? = null,
	@Json(name = "production_companies")
	val productionCompanies: List<ProductionCompanyModel> = emptyList(),
	@Json(name = "production_countries")
	val productionCountries: List<ProductionCountryModel> = emptyList(),
	@Json(name = "release_date")
	val releaseDate: String? = null,
	@Json(name = "runtime")
	val runtime: Int = 0,
	@Json(name = "spoken_languages")
	val spokenLanguages: List<LanguageModel> = emptyList(),
	@Json(name = "status")
	val status: String = "",
	@Json(name = "tagline")
	val tagline: String = "",
	@Json(name = "videos")
	val videos: VideoListModel = VideoListModel(),
	@Json(name = "credits")
	val credits: MovieCreditsModel = MovieCreditsModel()
)

data class GenreListModel(
	@Json(name = "genres")
	val genres: List<GenreModel>
)

data class GenreModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = ""
)

data class ProductionCompanyModel(
	@Json(name = "id")
	val id: Int = 0,
	@Json(name = "name")
	val name: String = "",
	@Json(name = "origin_country")
	val originCountry: String = "",
	@Json(name = "logo_path")
	val logoPath: String? = null
)

data class ProductionCountryModel(
	@Json(name = "iso_3166_1")
	val iso: String = "",
	@Json(name = "name")
	val name: String = ""
)

data class LanguageModel(
	@Json(name = "iso_639_1")
	val iso: String = "",
	@Json(name = "name")
	val name: String = "",
	@Json(name = "english_name")
	val englishName: String = ""
)
