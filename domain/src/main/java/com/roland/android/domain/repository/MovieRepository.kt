package com.roland.android.domain.repository

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

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

	fun fetchMoviesByGenre(genreIds: String): Flow<PagingData<Movie>>

	fun fetchMovieDetails(movieId: Int): Flow<MovieDetails>

	fun fetchMovieGenres(): Flow<List<Genre>>

}
