package com.roland.android.flick.models

import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList

data class MoviesModel(
	val trendingMovies: MovieList,
	val popularMovies: MovieList,
	val nowPlayingMovies: MovieList,
	val topRated: MovieList,
	val upcomingMovies: MovieList
)

data class FurtherMoviesModel(
	val bollywoodMovies: MovieList,
	val animeCollection: MovieList,
	val movieGenres: GenreList
)

data class MovieDetailsModel(
	val recommendedMovies: MovieList,
	val similarMovies: MovieList,
	val movieDetails: MovieDetails,
	val movieCasts: MovieCredits
)
