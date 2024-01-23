package com.roland.android.data_repository.data_source

import androidx.paging.PagingData
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import kotlinx.coroutines.flow.Flow

interface RemoteMovieSource {

	fun fetchTrendingMovies(): Flow<PagingData<Movie>>

	fun fetchTopRatedMovies(): Flow<PagingData<Movie>>

	fun fetchNowPlayingMovies(): Flow<PagingData<Movie>>

	fun fetchPopularMovies(): Flow<PagingData<Movie>>

	fun fetchUpcomingMovies(): Flow<PagingData<Movie>>

	fun fetchAnimeCollection(): Flow<PagingData<Movie>>

	fun fetchBollywoodMovies(): Flow<PagingData<Movie>>

	fun fetchRecommendedMovies(movieId: Int): Flow<PagingData<Movie>>

	fun fetchSimilarMovies(movieId: Int): Flow<PagingData<Movie>>

	fun searchMovies(query: String): Flow<PagingData<Movie>>

	fun searchMoviesAndShows(query: String): Flow<PagingData<Movie>>

	fun fetchMovieDetails(movieId: Int): Flow<MovieDetails>

	fun fetchMovieGenres(): Flow<GenreList>

}
