package com.roland.android.data_repository.media_repository

import androidx.paging.PagingData
import com.roland.android.data_repository.data_source.RemoteTvShowSource
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(
	private val remoteTvShowSource: RemoteTvShowSource
) : TvShowRepository {

	override fun fetchTrendingShows(): Flow<PagingData<Movie>> = remoteTvShowSource.fetchTrendingShows()

	override fun fetchTopRatedShows(): Flow<PagingData<Movie>> = remoteTvShowSource.fetchTopRatedShows()

	override fun fetchPopularShows(): Flow<PagingData<Movie>> = remoteTvShowSource.fetchPopularShows()

	override fun fetchShowsAiringToday(): Flow<PagingData<Movie>> = remoteTvShowSource.fetchShowsAiringToday()

	override fun fetchShowsSoonToAir(): Flow<PagingData<Movie>> = remoteTvShowSource.fetchShowsSoonToAir()

	override fun fetchShowsByGenre(genreIds: String): Flow<PagingData<Movie>> = remoteTvShowSource.fetchShowsByGenre(genreIds)

	override fun fetchShowsByRegion(region: String): Flow<PagingData<Movie>> = remoteTvShowSource.fetchShowsByRegion(region)

	override fun fetchRecommendedTvShows(seriesId: Int): Flow<PagingData<Movie>> = remoteTvShowSource.fetchRecommendedTvShows(seriesId)

	override fun fetchSimilarTvShows(seriesId: Int): Flow<PagingData<Movie>> = remoteTvShowSource.fetchSimilarTvShows(seriesId)

	override fun searchTvShows(query: String): Flow<PagingData<Movie>> = remoteTvShowSource.searchTvShows(query)

	override fun fetchTvShowDetails(seriesId: Int): Flow<Series> = remoteTvShowSource.fetchTvShowDetails(seriesId)

	override fun fetchSeasonDetails(
		seriesId: Int,
		seasonNumber: Int
	): Flow<Season> = remoteTvShowSource.fetchSeasonDetails(
		seriesId,
		seasonNumber
	)

	override fun fetchEpisodeDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
	): Flow<Episode> = remoteTvShowSource.fetchEpisodeDetails(
		seriesId,
		seasonNumber,
		episodeNumber
	)

	override fun fetchTvShowGenres(): Flow<List<Genre>> = remoteTvShowSource.fetchTvShowGenres()

//	--------------------------------User Authentication Required--------------------------------

	override fun favoriteTvShow(
		accountId: Int,
		request: FavoriteMediaRequest
	): Flow<Response> = remoteTvShowSource.favoriteTvShow(accountId, request)

	override fun fetchFavoritedTvShows(accountId: String): Flow<PagingData<Movie>> = remoteTvShowSource.fetchFavoritedTvShows(accountId)

	override fun fetchRecommendedTvShows(accountId: String): Flow<PagingData<Movie>> = remoteTvShowSource.fetchRecommendedTvShows(accountId)

	override fun watchlistTvShow(
		accountId: Int,
		request: WatchlistMediaRequest,
	): Flow<Response> = remoteTvShowSource.watchlistTvShow(accountId, request)

	override fun fetchWatchlistedTvShows(accountId: String): Flow<PagingData<Movie>> = remoteTvShowSource.fetchWatchlistedTvShows(accountId)

	override fun rateTvShow(
		seriesId: Int,
		request: RateMediaRequest
	): Flow<Response> = remoteTvShowSource.rateTvShow(seriesId, request)

	override fun deleteTvShowRating(seriesId: Int): Flow<Response> = remoteTvShowSource.deleteTvShowRating(seriesId)

	override fun fetchRatedTvShows(accountId: String): Flow<PagingData<Movie>> = remoteTvShowSource.fetchRatedTvShows(accountId)

}