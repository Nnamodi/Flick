package com.roland.android.domain.entity

data class MovieDetails(
	val id: Int = 0,
	val title: String? = null,
	val overview: String = "",
	val imdbId: String = "",
	val genres: List<Genre> = emptyList(),
	val backdropPath: String = "",
	val posterPath: String = "",
	val language: String = "",
	val popularity: Double = 0.0,
	val budget: Int = 0,
	val homePage: String = "",
	val movieType: String? = null,
	val videoAvailable: Boolean? = null,
	val voteAverage: Double = 0.0,
	val voteCount: Int = 0,
	val adult: Boolean? = null,
	val productionCompanies: List<ProductionCompany> = emptyList(),
	val productionCountries: List<ProductionCountry> = emptyList(),
	val releaseDate: String? = null,
	val revenue: Int = 0,
	val spokenLanguages: List<Language> = emptyList(),
	val status: String = "",
	val tagline: String = "",
	//tv-specific params
	val tvName: String? = null,
	val firstAirDate: String? = null,
	val originalCountry: List<String>? = null
)

data class GenreList(
	val genres: List<Genre>
)

data class Genre(
	val id: Int = 0,
	val name: String = ""
)

data class ProductionCompany(
	val id: Int = 0,
	val name: String = "",
	val originCountry: String = "",
	val logoPath: String? = null
)

data class ProductionCountry(
	val iso: String = "",
	val name: String = ""
)

data class Language(
	val iso: String = "",
	val name: String = "",
	val englishName: String = ""
)
