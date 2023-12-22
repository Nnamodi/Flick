package com.roland.android.flick.models

import com.roland.android.domain.entity.MovieList

data class MoviesModel(
	val trendingMovies: MovieList,
	val popularMovies: MovieList,
	val nowPlayingMovies: MovieList,
	val topRated: MovieList,
	val upcomingMovies: MovieList
)
