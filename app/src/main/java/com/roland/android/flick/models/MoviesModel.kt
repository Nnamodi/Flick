package com.roland.android.flick.models

import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList

data class MoviesModel(
	val trending: MovieList = MovieList(),
	val popular: MovieList = MovieList(),
	val nowPlaying: MovieList = MovieList(),
	val topRated: MovieList = MovieList(),
	val upcoming: MovieList = MovieList()
)

data class FurtherMoviesModel(
	val bollywood: MovieList = MovieList(),
	val anime: MovieList = MovieList(),
	val genres: GenreList = GenreList()
)

data class MovieListModel(
	val movieList: MovieList = MovieList(),
	val movieGenres: GenreList = GenreList(),
	val seriesGenres: GenreList = GenreList()
)

data class MovieDetailsModel(
	val recommendedMovies: MovieList = MovieList(),
	val similarMovies: MovieList = MovieList(),
	val movieDetails: MovieDetails = MovieDetails(),
	val movieCasts: MovieCredits = MovieCredits()
)

data class CastDetailsModel(val cast: Cast = Cast())
