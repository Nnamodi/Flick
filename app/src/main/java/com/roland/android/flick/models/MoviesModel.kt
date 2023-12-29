package com.roland.android.flick.models

import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList

data class MoviesModel(
	val trendingMovies: MovieList = MovieList(),
	val popularMovies: MovieList = MovieList(),
	val nowPlayingMovies: MovieList = MovieList(),
	val topRated: MovieList = MovieList(),
	val upcomingMovies: MovieList = MovieList()
)

data class FurtherMoviesModel(
	val bollywoodMovies: MovieList = MovieList(),
	val animeCollection: MovieList = MovieList(),
	val movieGenres: GenreList = GenreList()
)

data class MovieDetailsModel(
	val recommendedMovies: MovieList = MovieList(),
	val similarMovies: MovieList = MovieList(),
	val movieDetails: MovieDetails = MovieDetails(),
	val movieCasts: MovieCredits = MovieCredits()
)

data class CastDetailsModel(val cast: Cast = Cast())
