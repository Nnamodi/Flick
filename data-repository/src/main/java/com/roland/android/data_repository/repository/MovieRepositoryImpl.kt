package com.roland.android.data_repository.repository

import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
	private val remoteMovieSource: RemoteMovieSource
) : MovieRepository {

	override fun fetchTrendingMovies(): Flow<MovieList> = remoteMovieSource.fetchTrendingMovies()

	override fun fetchTopRatedMovies(): Flow<MovieList> = remoteMovieSource.fetchTopRatedMovies()

	override fun fetchNowPlayingMovies(): Flow<MovieList> = remoteMovieSource.fetchNowPlayingMovies()

	override fun fetchPopularMovies(): Flow<MovieList> = remoteMovieSource.fetchPopularMovies()

	override fun fetchUpcomingMovies(): Flow<MovieList> = remoteMovieSource.fetchUpcomingMovies()

	override fun fetchAnimeCollection(): Flow<MovieList> = remoteMovieSource.fetchAnimeCollection()

	override fun fetchBollywoodMovies(): Flow<MovieList> = remoteMovieSource.fetchBollywoodMovies()

	override fun fetchRecommendedMovies(movieId: Int): Flow<MovieList> = remoteMovieSource.fetchRecommendedMovies(movieId)

	override fun fetchSimilarMovies(movieId: Int): Flow<MovieList> = remoteMovieSource.fetchSimilarMovies(movieId)

	override fun fetchMovieDetails(movieId: Int): Flow<MovieDetails> = remoteMovieSource.fetchMovieDetails(movieId)

	override fun fetchMovieGenres(): Flow<List<Genre>> = remoteMovieSource.fetchMovieGenres()

}