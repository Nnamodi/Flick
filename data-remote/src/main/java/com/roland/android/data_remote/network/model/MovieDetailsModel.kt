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
	val imdbId: String = "",
	@Json(name = "genres")
	val genres: List<GenreModel> = emptyList(),
	@Json(name = "backdrop_path")
	val backdropPath: String = "",
	@Json(name = "poster_path")
	val posterPath: String = "",
	@Json(name = "language")
	val language: String = "",
	@Json(name = "popularity")
	val popularity: Double = 0.0,
	@Json(name = "budget")
	val budget: Int = 0,
	@Json(name = "homepage")
	val homePage: String = "",
	@Json(name = "movie_type")
	val movieType: String? = null,
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
	@Json(name = "revenue")
	val revenue: Int = 0,
	@Json(name = "spoken_languages")
	val spokenLanguages: List<LanguageModel> = emptyList(),
	@Json(name = "status")
	val status: String = "",
	@Json(name = "tagline")
	val tagline: String = "",
	//tv-specific params
	@Json(name = "tv_name")
	val tvName: String? = null,
	@Json(name = "first_air_date")
	val firstAirDate: String? = null,
	@Json(name = "original_country")
	val originalCountry: List<String>? = null
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
	val logoPath: String = ""
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
