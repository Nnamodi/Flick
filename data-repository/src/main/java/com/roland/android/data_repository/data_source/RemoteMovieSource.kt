package com.roland.android.data_repository.data_source

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest
import kotlinx.coroutines.flow.Flow

interface RemoteMovieSource {

	fun fetchTrendingMovies(): Flow<PagingData<Movie>>

	fun fetchTopRatedMovies(): Flow<PagingData<Movie>>

	fun fetchNowPlayingMovies(): Flow<PagingData<Movie>>

	fun fetchPopularMovies(): Flow<PagingData<Movie>>

	fun fetchUpcomingMovies(): Flow<PagingData<Movie>>

	fun fetchMoviesByGenre(genreIds: String): Flow<PagingData<Movie>>

	fun fetchMoviesByRegion(region: String): Flow<PagingData<Movie>>

	fun fetchRecommendedMovies(movieId: Int): Flow<PagingData<Movie>>

	fun fetchSimilarMovies(movieId: Int): Flow<PagingData<Movie>>

	fun searchMovies(query: String): Flow<PagingData<Movie>>

	fun searchMoviesAndShows(query: String): Flow<PagingData<Movie>>

	fun fetchMovieDetails(movieId: Int): Flow<MovieDetails>

	fun fetchMovieGenres(): Flow<List<Genre>>

//	--------------------------------User Authentication Required--------------------------------

	fun favoriteMovie(
		accountId: Int,
		request: FavoriteMediaRequest
	): Flow<Response>

	fun fetchFavoritedMovies(accountId: String): Flow<PagingData<Movie>>

	fun fetchRecommendedMovies(accountId: String): Flow<PagingData<Movie>>

	fun watchlistMovie(
		accountId: Int,
		request: WatchlistMediaRequest
	): Flow<Response>

	fun fetchWatchlistedMovies(accountId: String): Flow<PagingData<Movie>>

	fun rateMovie(
		movieId: Int,
		request: RateMediaRequest
	): Flow<Response>

	fun deleteMovieRating(movieId: Int): Flow<Response>

	fun fetchRatedMovies(accountId: String): Flow<PagingData<Movie>>

}
