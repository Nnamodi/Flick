package com.roland.android.data_repository.media_repository

import androidx.paging.PagingData
import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest
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

	override fun fetchMoviesByGenre(genreIds: String): Flow<PagingData<Movie>> = remoteMovieSource.fetchMoviesByGenre(genreIds)

	override fun fetchMoviesByRegion(region: String): Flow<PagingData<Movie>> = remoteMovieSource.fetchMoviesByRegion(region)

	override fun fetchRecommendedMovies(movieId: Int): Flow<PagingData<Movie>> = remoteMovieSource.fetchRecommendedMovies(movieId)

	override fun fetchSimilarMovies(movieId: Int): Flow<PagingData<Movie>> = remoteMovieSource.fetchSimilarMovies(movieId)

	override fun searchMovies(query: String): Flow<PagingData<Movie>> = remoteMovieSource.searchMovies(query)

	override fun searchMoviesAndShows(query: String): Flow<PagingData<Movie>> = remoteMovieSource.searchMoviesAndShows(query)

	override fun fetchMovieDetails(movieId: Int): Flow<MovieDetails> = remoteMovieSource.fetchMovieDetails(movieId)

	override fun fetchMovieGenres(): Flow<List<Genre>> = remoteMovieSource.fetchMovieGenres()

//	--------------------------------User Authentication Required--------------------------------

	override fun favoriteMovie(
		accountId: Int,
		request: FavoriteMediaRequest
	): Flow<Response> = remoteMovieSource.favoriteMovie(accountId, request)

	override fun fetchFavoritedMovies(accountId: String): Flow<PagingData<Movie>> = remoteMovieSource.fetchFavoritedMovies(accountId)

	override fun fetchRecommendedMovies(accountId: String): Flow<PagingData<Movie>> = remoteMovieSource.fetchRecommendedMovies(accountId)

	override fun watchlistMovie(
		accountId: Int,
		request: WatchlistMediaRequest
	): Flow<Response> = remoteMovieSource.watchlistMovie(accountId, request)

	override fun fetchWatchlistedMovies(accountId: String): Flow<PagingData<Movie>> = remoteMovieSource.fetchWatchlistedMovies(accountId)

	override fun rateMovie(
		movieId: Int,
		request: RateMediaRequest
	): Flow<Response> = remoteMovieSource.rateMovie(movieId, request)

	override fun deleteMovieRating(movieId: Int): Flow<Response> = remoteMovieSource.deleteMovieRating(movieId)

	override fun fetchRatedMovies(accountId: String): Flow<PagingData<Movie>> = remoteMovieSource.fetchRatedMovies(accountId)

}