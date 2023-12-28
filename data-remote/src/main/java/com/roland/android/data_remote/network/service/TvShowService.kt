package com.roland.android.data_remote.network.service

import com.roland.android.data_remote.network.model.EpisodeModel
import com.roland.android.data_remote.network.model.GenreListModel
import com.roland.android.data_remote.network.model.MovieListModel
import com.roland.android.data_remote.network.model.SeasonModel
import com.roland.android.data_remote.network.model.SeriesModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowService {

	@GET("/3/tv/top_rated")
	suspend fun fetchTopRatedShows(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1
	): MovieListModel

	@GET("/3/tv/popular")
	suspend fun fetchPopularShows(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1
	): MovieListModel

	@GET("/3/tv/airing_today")
	suspend fun fetchShowsAiringToday(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1
	): MovieListModel

	@GET("/3/tv/on_the_air")
	suspend fun fetchShowsSoonToAir(
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1
	): MovieListModel

	@GET("/3/tv/{series_id}/recommendations")
	suspend fun fetchRecommendedTvShows(
		@Path("series_id") seriesId: Int,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1
	): MovieListModel

	@GET("/3/tv/{series_id}/similar")
	suspend fun fetchSimilarTvShows(
		@Path("series_id") seriesId: Int,
		@Query("language") language: String = "en_US",
		@Query("page") page: Int = 1
	): MovieListModel

	@GET("/3/tv/{series_id}")
	suspend fun fetchTvShowDetails(
		@Path("series_id") seriesId: Int,
		@Query("language") language: String = "en_US"
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

}
