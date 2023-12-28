package com.roland.android.flick.models

import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.Series

data class TvShowsModel(
	val topRatedShows: MovieList,
	val popularShows: MovieList,
	val showsAiringToday: MovieList,
	val showsSoonToAir: MovieList,
	val genres: GenreList
)

data class TvShowDetailsModel(
	val recommendedShows: MovieList,
	val similarShows: MovieList,
	val showDetails: Series,
	val showCasts: MovieCredits
)
