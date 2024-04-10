package com.roland.android.data_remote.data_source

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.roland.android.data_remote.network.service.TvShowService
import com.roland.android.data_remote.paging.FavoritedShowsPagingSource
import com.roland.android.data_remote.paging.NowAiringShowsPagingSource
import com.roland.android.data_remote.paging.PopularShowsPagingSource
import com.roland.android.data_remote.paging.RatedShowsPagingSource
import com.roland.android.data_remote.paging.RecommendedShowsPagingSource
import com.roland.android.data_remote.paging.SearchedShowsPagingSource
import com.roland.android.data_remote.paging.ShowsByGenrePagingSource
import com.roland.android.data_remote.paging.ShowsByRegionPagingSource
import com.roland.android.data_remote.paging.ShowsRecommendedPagingSource
import com.roland.android.data_remote.paging.SimilarShowsPagingSource
import com.roland.android.data_remote.paging.TopRatedShowsPagingSource
import com.roland.android.data_remote.paging.TrendingShowsPagingSource
import com.roland.android.data_remote.paging.UpcomingShowsPagingSource
import com.roland.android.data_remote.paging.WatchlistedShowsPagingSource
import com.roland.android.data_remote.utils.Constants.MAX_PAGE_SIZE
import com.roland.android.data_remote.utils.Converters.convertFromFavoriteMediaRequest
import com.roland.android.data_remote.utils.Converters.convertFromRateMediaRequest
import com.roland.android.data_remote.utils.Converters.convertFromWatchlistMediaRequest
import com.roland.android.data_remote.utils.Converters.convertToEpisode
import com.roland.android.data_remote.utils.Converters.convertToGenreList
import com.roland.android.data_remote.utils.Converters.convertToResponse
import com.roland.android.data_remote.utils.Converters.convertToSeason
import com.roland.android.data_remote.utils.Converters.convertToShowDetails
import com.roland.android.data_repository.data_source.RemoteTvShowSource
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.UseCaseException
import com.roland.android.domain.entity.auth_response.FavoriteMediaRequest
import com.roland.android.domain.entity.auth_response.RateMediaRequest
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.entity.auth_response.WatchlistMediaRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteTvShowSourceImpl @Inject constructor(
	private val tvShowService: TvShowService,
	private val scope: CoroutineScope
) : RemoteTvShowSource {

	override fun fetchTrendingShows(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				TrendingShowsPagingSource(tvShowService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchTopRatedShows(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				TopRatedShowsPagingSource(tvShowService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchPopularShows(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				PopularShowsPagingSource(tvShowService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchShowsAiringToday(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				NowAiringShowsPagingSource(tvShowService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchShowsSoonToAir(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				UpcomingShowsPagingSource(tvShowService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchShowsByGenre(genreIds: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				ShowsByGenrePagingSource(tvShowService, genreIds)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchShowsByRegion(region: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				ShowsByRegionPagingSource(tvShowService, region)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchRecommendedTvShows(seriesId: Int): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				RecommendedShowsPagingSource(tvShowService, seriesId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchSimilarTvShows(seriesId: Int): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				SimilarShowsPagingSource(tvShowService, seriesId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun searchTvShows(query: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				SearchedShowsPagingSource(tvShowService, query)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchTvShowDetails(seriesId: Int): Flow<Series> = flow {
		emit(tvShowService.fetchTvShowDetails(seriesId))
	}.map { seriesModel ->
		convertToShowDetails(seriesModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchSeasonDetails(
		seriesId: Int,
		seasonNumber: Int
	): Flow<Season> = flow {
		emit(tvShowService.fetchSeasonDetails(seriesId, seasonNumber))
	}.map { seasonModel ->
		convertToSeason(seasonModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchEpisodeDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int,
	): Flow<Episode> = flow {
		emit(tvShowService.fetchEpisodeDetails(seriesId, seasonNumber, episodeNumber))
	}.map { episodeModel ->
		convertToEpisode(episodeModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

	override fun fetchTvShowGenres(): Flow<List<Genre>> = flow {
		emit(tvShowService.fetchTvShowGenres())
	}.map { genreListModel ->
		convertToGenreList(genreListModel.genres)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}

//	--------------------------------User Authentication Required--------------------------------

	override fun favoriteTvShow(
		accountId: Int,
		request: FavoriteMediaRequest
	): Flow<Response> = flow {
		val requestModel = convertFromFavoriteMediaRequest(request)
		emit(tvShowService.favoriteTvShow(accountId, requestModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchFavoritedTvShows(accountId: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				FavoritedShowsPagingSource(tvShowService, accountId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchRecommendedTvShows(accountId: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				ShowsRecommendedPagingSource(tvShowService, accountId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun watchlistTvShow(
		accountId: Int,
		request: WatchlistMediaRequest,
	): Flow<Response> = flow {
		val requestModel = convertFromWatchlistMediaRequest(request)
		emit(tvShowService.watchlistTvShow(accountId, requestModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchWatchlistedTvShows(accountId: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				WatchlistedShowsPagingSource(tvShowService, accountId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun rateTvShow(
		seriesId: Int,
		request: RateMediaRequest
	): Flow<Response> = flow {
		val requestModel = convertFromRateMediaRequest(request)
		emit(tvShowService.rateTvShow(seriesId, requestModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun deleteTvShowRating(seriesId: Int): Flow<Response> = flow {
		emit(tvShowService.deleteTvShowRating(seriesId))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchRatedTvShows(accountId: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				RatedShowsPagingSource(tvShowService, accountId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

}