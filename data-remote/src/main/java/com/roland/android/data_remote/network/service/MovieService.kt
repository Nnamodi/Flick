package com.roland.android.data_remote.network.service

import com.roland.android.data_remote.network.model.GenreListModel
import com.roland.android.data_remote.network.model.MovieDetailsModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.MultiListModel
import com.roland.android.data_remote.network.model.auth_response.FavoriteMediaRequestModel
import com.roland.android.data_remote.network.model.auth_response.RateMediaRequestModel
import com.roland.android.data_remote.network.model.auth_response.ResponseModel
import com.roland.android.data_remote.network.model.auth_response.WatchlistMediaRequestModel
import com.roland.android.data_remote.utils.Constants.NEXT_MONTH
import com.roland.android.data_remote.utils.Constants.TOMORROW
import com.roland.android.data_remote.utils.Constants.date
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

	@GET("/3/trending/movie/{time_window}")
	suspend fun fetchTrendingMovies(
		@Path("time_window") timeWindow: String = "day", // day, week
		@Query("language") language: String = "en-US"
	): MovieListModel

	@GET("/3/movie/top_rated")
	suspend fun fetchTopRatedMovies(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/movie/now_playing")
	suspend fun fetchNowPlayingMovies(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/movie/popular")
	suspend fun fetchPopularMovies(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/discover/movie")
	suspend fun fetchUpcomingMovies(
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("primary_release_date.gte") fromDate: String = date(TOMORROW),
		@Query("primary_release_date.lte") toDate: String = date(NEXT_MONTH),
		@Query("language") language: String = "en_US",
		@Query("page") page: Int,
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

	@GET("/3/discover/movie")
	suspend fun fetchMoviesByGenre(
		@Query("with_genres") genreIds: String,
		@Query("page") page: Int,
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

	@GET("/3/discover/movie")
	suspend fun fetchMoviesByRegion(
		@Query("with_origin_country") region: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

	@GET("/3/movie/{movie_id}/recommendations")
	suspend fun fetchRecommendedMovies(
		@Path("movie_id") movieId: Int,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/movie/{movie_id}/similar")
	suspend fun fetchSimilarMovies(
		@Path("movie_id") movieId: Int,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/search/movie")
	suspend fun searchMovies(
		@Query("query") query: String,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/search/multi")
	suspend fun searchMoviesAndShows(
		@Query("query") query: String,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MultiListModel

	@GET("/3/movie/{movie_id}")
	suspend fun fetchMovieDetails(
		@Path("movie_id") movieId: Int,
		@Query("language") language: String = "en_US",
		@Query("append_to_response") appendToResponse: String = "credits,videos"
	): MovieDetailsModel

	@GET("/3/genre/movie/list")
	suspend fun fetchMovieGenres(
		@Query("language") language: String = "en"
	): GenreListModel

//	--------------------------------User Authentication Required--------------------------------

	@POST("/3/account/{account_id}/favorite")
	suspend fun favoriteMovie(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Body request: FavoriteMediaRequestModel
	): ResponseModel

	@GET("/3/account/{account_id}/favorite/movies")
	suspend fun fetchFavoritedMovies(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "created_at.desc"
	): MovieListModel

	@GET("/4/account/{account_object_id}/movie/recommendations")
	suspend fun fetchRecommendedMovies(
		@Path("account_object_id") accountId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US"
	): MovieListModel

	@POST("/3/account/{account_id}/watchlist")
	suspend fun watchlistMovie(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Body request: WatchlistMediaRequestModel
	): ResponseModel

	@GET("/3/account/{account_id}/watchlist/movies")
	suspend fun fetchWatchlistedMovies(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "created_at.desc"
	): MovieListModel

	@POST("/3/movie/{movie_id}/rating")
	suspend fun rateMovie(
		@Path("movie_id") movieId: Int,
		@Query("session_id") sessionId: String,
		@Body request: RateMediaRequestModel
	): ResponseModel

	@DELETE("/3/movie/{movie_id}/rating")
	suspend fun deleteMovieRating(
		@Path("movie_id") movieId: Int,
		@Query("session_id") sessionId: String
	): ResponseModel

	@GET("/3/account/{account_id}/rated/movies")
	suspend fun fetchRatedMovies(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "created_at.desc"
	): MovieListModel

}
