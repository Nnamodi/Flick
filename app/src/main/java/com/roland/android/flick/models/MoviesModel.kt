package com.roland.android.flick.models

import androidx.paging.PagingData
import com.roland.android.domain.entity.CastDetails
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import kotlinx.coroutines.flow.MutableStateFlow

data class MoviesModel(
	val trending: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val popular: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val nowPlaying: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val topRated: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val genres: List<Genre> = emptyList()
)

data class MoviesByGenreModel(
	val anime: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val comedy: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val romedy: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val sciFi: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val warStory: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
)

data class MoviesByRegionModel(
	val nollywood: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val korean: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val bollywood: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val recommendations: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty())
)

data class MovieListModel(
	val movieList: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: List<Genre> = emptyList(),
	val seriesGenres: List<Genre> = emptyList()
)

data class SearchModel(
	val moviesAndShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val tvShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: List<Genre> = emptyList(),
	val seriesGenres: List<Genre> = emptyList()
)

data class CategorySelectionModel(
	val movieList: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: List<Genre> = emptyList(),
	val seriesGenres: List<Genre> = emptyList()
)

data class ComingSoonModel(
	val upcomingMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val upcomingShows: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: List<Genre> = emptyList(),
	val seriesGenres: List<Genre> = emptyList()
)

data class MovieDetailsModel(
	val details: MovieDetails = MovieDetails(),
	val recommendedMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val similarMovies: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
	val movieGenres: List<Genre> = emptyList(),
	val seriesGenres: List<Genre> = emptyList()
)

data class CastDetailsModel(val castDetails: CastDetails = CastDetails())
