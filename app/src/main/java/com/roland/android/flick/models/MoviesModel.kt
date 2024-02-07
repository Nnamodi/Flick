package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import kotlinx.coroutines.flow.MutableStateFlow

data class MoviesModel(
	val trending: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val popular: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val nowPlaying: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val topRated: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val upcoming: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
)

data class FurtherMoviesModel(
	val bollywood: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val anime: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val genres: GenreList = GenreList()
)

data class MovieListModel(
	val movieList: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: GenreList = GenreList(),
	val seriesGenres: GenreList = GenreList()
)

data class SearchModel(
	val moviesAndShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val tvShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: GenreList = GenreList(),
	val seriesGenres: GenreList = GenreList()
)

data class ComingSoonModel(
	val upcomingMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val upcomingShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: GenreList = GenreList(),
	val seriesGenres: GenreList = GenreList()
)

data class MovieDetailsModel(
	val details: MovieDetails = MovieDetails(),
	val recommendedMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val similarMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: GenreList = GenreList(),
	val seriesGenres: GenreList = GenreList()
)

data class CastDetailsModel(val castDetails: CastDetails = CastDetails())
