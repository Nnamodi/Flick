package com.roland.android.data_remote.network.service

import com.roland.android.data_remote.network.model.GenreListModel
import com.roland.android.data_remote.network.model.MovieDetailsModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.MultiListModel
import com.roland.android.data_remote.utils.Constants.NEXT_MONTH
import com.roland.android.data_remote.utils.Constants.TOMORROW
import com.roland.android.data_remote.utils.Constants.date
import retrofit2.http.GET
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
	suspend fun fetchAnimeCollection(
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1,
		@Query("sort_by") sortBy: String = "popularity.desc",
		@Query("with_genres") genre: String = "16"
	): MovieListModel

	@GET("/3/discover/movie")
	suspend fun fetchBollywoodMovies(
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("language") language: String = "hi_IN",
		@Query("page") page: Int = 1,
		@Query("sort_by") sortBy: String = "popularity.desc",
		@Query("region") region: String = "IN",
		@Query("watch_region") watchRegion: String = "IN",
		@Query("with_original_language") originalLang: String = "hi"
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

	@GET("/3/discover/movie")
	suspend fun fetchMoviesByGenre(
		@Query("with_genres") genreIds: String,
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1,
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

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

}
