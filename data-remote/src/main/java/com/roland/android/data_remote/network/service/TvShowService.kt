package com.roland.android.data_remote.network.service

import com.roland.android.data_remote.network.model.EpisodeModel
import com.roland.android.data_remote.network.model.GenreListModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.SeasonModel
import com.roland.android.data_remote.network.model.SeriesModel
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

interface TvShowService {

	@GET("/3/trending/tv/{time_window}")
	suspend fun fetchTrendingShows(
		@Path("time_window") timeWindow: String = "day", // day, week
		@Query("language") language: String = "en_US"
	): MovieListModel

	@GET("/3/tv/top_rated")
	suspend fun fetchTopRatedShows(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/tv/popular")
	suspend fun fetchPopularShows(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/tv/airing_today")
	suspend fun fetchShowsAiringToday(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/discover/tv")
	suspend fun fetchShowsSoonToAir(
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("first_air_date.gte") fromDate: String = date(TOMORROW),
		@Query("first_air_date.lte") toDate: String = date(NEXT_MONTH),
		@Query("language") language: String = "en_US",
		@Query("page") page: Int,
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

	@GET("/3/discover/tv")
	suspend fun fetchShowsByGenre(
		@Query("with_genres") genreIds: String,
		@Query("page") page: Int,
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

	@GET("/3/discover/tv")
	suspend fun fetchShowsByRegion(
		@Query("with_origin_country") region: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("include_adult") includeAdult: Boolean = false,
		@Query("sort_by") sortBy: String = "popularity.desc"
	): MovieListModel

	@GET("/3/tv/{series_id}/recommendations")
	suspend fun fetchRecommendedTvShows(
		@Path("series_id") seriesId: Int,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/tv/{series_id}/similar")
	suspend fun fetchSimilarTvShows(
		@Path("series_id") seriesId: Int,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/search/tv")
	suspend fun searchTvShows(
		@Query("query") query: String,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int
	): MovieListModel

	@GET("/3/tv/{series_id}")
	suspend fun fetchTvShowDetails(
		@Path("series_id") seriesId: Int,
		@Query("language") language: String = "en_US",
		@Query("append_to_response") appendToResponse: String = "aggregate_credits,external_ids,videos"
	): SeriesModel

	@GET("/3/tv/{series_id}/season/{season_number}")
	suspend fun fetchSeasonDetails(
		@Path("series_id") seriesId: Int,
		@Path("season_number") seasonNumber: Int,
		@Query("language") language: String = "en_US"
	): SeasonModel

	@GET("/3/tv/{series_id}/season/{season_number}/episode/{episode_number}")
	suspend fun fetchEpisodeDetails(
		@Path("series_id") seriesId: Int,
		@Path("season_number") seasonNumber: Int,
		@Path("episode_number") episodeNumber: Int,
		@Query("language") language: String = "en_US"
	): EpisodeModel

	@GET("/3/genre/tv/list")
	suspend fun fetchTvShowGenres(
		@Query("language") language: String = "en"
	): GenreListModel

//	--------------------------------User Authentication Required--------------------------------

	@POST("/3/account/{account_id}/favorite")
	suspend fun favoriteTvShow(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Body request: FavoriteMediaRequestModel
	): ResponseModel

	@GET("/3/account/{account_id}/favorite/tv")
	suspend fun fetchFavoritedTvShows(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "created_at.desc"
	): MovieListModel

	@GET("/4/account/{account_object_id}/tv/recommendations")
	suspend fun fetchRecommendedTvShows(
		@Path("account_object_id") accountId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US"
	): MovieListModel

	@POST("/3/account/{account_id}/watchlist")
	suspend fun watchlistTvShow(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Body request: WatchlistMediaRequestModel
	): ResponseModel

	@GET("/3/account/{account_id}/watchlist/tv")
	suspend fun fetchWatchlistedTvShows(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "created_at.desc"
	): MovieListModel

	@POST("/3/tv/{series_id}/rating")
	suspend fun rateTvShow(
		@Path("series_id") seriesId: Int,
		@Query("session_id") sessionId: String,
		@Body request: RateMediaRequestModel
	): ResponseModel

	@DELETE("/3/tv/{series_id}/rating")
	suspend fun deleteTvShowRating(
		@Path("series_id") seriesId: Int,
		@Query("session_id") sessionId: String
	): ResponseModel

	@GET("/3/account/{account_id}/rated/tv")
	suspend fun fetchRatedTvShows(
		@Path("account_id") accountId: Int,
		@Query("session_id") sessionId: String,
		@Query("page") page: Int,
		@Query("language") language: String = "en_US",
		@Query("sort_by") sortBy: String = "created_at.desc"
	): MovieListModel

}
