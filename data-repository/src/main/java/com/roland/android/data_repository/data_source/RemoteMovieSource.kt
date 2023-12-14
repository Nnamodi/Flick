package com.roland.android.data_repository.data_source

import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList
import kotlinx.coroutines.flow.Flow

interface RemoteMovieSource {

	fun fetchTrendingMovies(): Flow<MovieList>

	fun fetchTopRatedMovies(): Flow<MovieList>

	fun fetchNowPlayingMovies(): Flow<MovieList>

	fun fetchPopularMovies(): Flow<MovieList>

	fun fetchUpcomingMovies(): Flow<MovieList>

	fun fetchAnimeCollection(): Flow<MovieList>

	fun fetchBollywoodMovies(): Flow<MovieList>

	fun fetchRecommendedMovies(movieId: Int): Flow<MovieList>

	fun fetchSimilarMovies(movieId: Int): Flow<MovieList>

	fun fetchMovieDetails(movieId: Int): Flow<MovieDetails>

	fun fetchMovieGenres(): Flow<List<Genre>>

}
