package com.roland.android.domain.repository

import androidx.paging.PagingData
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {

	fun fetchTrendingShows(): Flow<PagingData<Movie>>

	fun fetchTopRatedShows(): Flow<PagingData<Movie>>

	fun fetchPopularShows(): Flow<PagingData<Movie>>

	fun fetchShowsAiringToday(): Flow<PagingData<Movie>>

	fun fetchShowsSoonToAir(): Flow<PagingData<Movie>>

	fun fetchShowsByGenre(genreIds: String): Flow<PagingData<Movie>>

	fun fetchShowsByRegion(region: String): Flow<PagingData<Movie>>

	fun fetchRecommendedTvShows(seriesId: Int): Flow<PagingData<Movie>>

	fun fetchSimilarTvShows(seriesId: Int): Flow<PagingData<Movie>>

	fun searchTvShows(query: String): Flow<PagingData<Movie>>

	fun fetchTvShowDetails(seriesId: Int): Flow<Series>

	fun fetchSeasonDetails(
		seriesId: Int,
		seasonNumber: Int
	): Flow<Season>

	fun fetchEpisodeDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int
	): Flow<Episode>

	fun fetchTvShowGenres(): Flow<List<Genre>>

//	--------------------------------User Authentication Required--------------------------------

	fun favoriteTvShow(
		accountId: Int,
		sessionId: String,
		request: FavoriteMediaRequest
	): Flow<Response>

	fun fetchFavoritedTvShows(
		accountId: Int,
		sessionId: String
	): Flow<PagingData<Movie>>

	fun fetchRecommendedTvShows(accountId: String): Flow<PagingData<Movie>>

	fun watchlistTvShow(
		accountId: Int,
		sessionId: String,
		request: WatchlistMediaRequest
	): Flow<Response>

	fun fetchWatchlistedTvShows(
		accountId: Int,
		sessionId: String
	): Flow<PagingData<Movie>>

	fun rateTvShow(
		seriesId: Int,
		sessionId: String,
		request: RateMediaRequest
	): Flow<Response>

	fun deleteTvShowRating(
		seriesId: Int,
		sessionId: String
	): Flow<Response>

	fun fetchRatedTvShows(
		accountId: Int,
		sessionId: String
	): Flow<PagingData<Movie>>

}
