package com.roland.android.data_repository.repository

import androidx.paging.PagingData
import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
	private val remoteMovieSource: RemoteMovieSource
) : MovieRepository {

	override fun fetchTrendingMovies(): Flow<PagingData<Movie>> = remoteMovieSource.fetchTrendingMovies()

	override fun fetchTopRatedMovies(): Flow<PagingData<Movie>> = remoteMovieSource.fetchTopRatedMovies()

	override fun fetchNowPlayingMovies(): Flow<PagingData<Movie>> = remoteMovieSource.fetchNowPlayingMovies()

	override fun fetchPopularMovies(): Flow<PagingData<Movie>> = remoteMovieSource.fetchPopularMovies()

	override fun fetchUpcomingMovies(): Flow<PagingData<Movie>> = remoteMovieSource.fetchUpcomingMovies()

	override fun fetchAnimeCollection(): Flow<PagingData<Movie>> = remoteMovieSource.fetchAnimeCollection()

	override fun fetchBollywoodMovies(): Flow<PagingData<Movie>> = remoteMovieSource.fetchBollywoodMovies()

	override fun fetchRecommendedMovies(movieId: Int): Flow<PagingData<Movie>> = remoteMovieSource.fetchRecommendedMovies(movieId)

	override fun fetchSimilarMovies(movieId: Int): Flow<PagingData<Movie>> = remoteMovieSource.fetchSimilarMovies(movieId)

	override fun searchMovies(query: String): Flow<PagingData<Movie>> = remoteMovieSource.searchMovies(query)

	override fun fetchMovieDetails(movieId: Int): Flow<MovieDetails> = remoteMovieSource.fetchMovieDetails(movieId)

	override fun fetchMovieGenres(): Flow<GenreList> = remoteMovieSource.fetchMovieGenres()

}