package com.roland.android.flick.models

import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series

data class TvShowsModel(
	val topRatedShows: MovieList = MovieList(),
	val popularShows: MovieList = MovieList(),
	val showsAiringToday: MovieList = MovieList(),
	val showsSoonToAir: MovieList = MovieList(),
	val genres: GenreList = GenreList()
)

data class TvShowDetailsModel(
	val recommendedShows: MovieList = MovieList(),
	val similarShows: MovieList = MovieList(),
	val showDetails: Series = Series(),
	val showCasts: MovieCredits = MovieCredits()
)

data class SeasonDetailsModel(
	val season: Season = Season(),
	val episode: Episode = Episode(),
	val showCasts: MovieCredits = MovieCredits()
)
