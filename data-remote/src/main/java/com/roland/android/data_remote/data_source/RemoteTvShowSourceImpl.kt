package com.roland.android.data_remote.data_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.roland.android.data_remote.network.service.TvShowService
import com.roland.android.data_remote.paging.AnimeShowsPagingSource
import com.roland.android.data_remote.paging.BollywoodShowsPagingSource
import com.roland.android.data_remote.paging.NowAiringShowsPagingSource
import com.roland.android.data_remote.paging.PopularShowsPagingSource
import com.roland.android.data_remote.paging.RecommendedShowsPagingSource
import com.roland.android.data_remote.paging.SearchedShowsPagingSource
import com.roland.android.data_remote.paging.SimilarShowsPagingSource
import com.roland.android.data_remote.paging.TopRatedShowsPagingSource
import com.roland.android.data_remote.paging.TrendingShowsPagingSource
import com.roland.android.data_remote.paging.UpcomingShowsPagingSource
import com.roland.android.data_remote.utils.Constants.MAX_PAGE_SIZE
import com.roland.android.data_remote.utils.Converters.convertToEpisode
import com.roland.android.data_remote.utils.Converters.convertToGenreList
import com.roland.android.data_remote.utils.Converters.convertToSeason
import com.roland.android.data_remote.utils.Converters.convertToShowDetails
import com.roland.android.data_repository.data_source.RemoteTvShowSource
import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.UseCaseException
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

	override fun fetchAnimeShows(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				AnimeShowsPagingSource(tvShowService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchBollywoodShows(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				BollywoodShowsPagingSource(tvShowService)
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

	override fun fetchTvShowGenres(): Flow<GenreList> = flow {
		emit(tvShowService.fetchTvShowGenres())
	}.map { genreListModel ->
		convertToGenreList(genreListModel)
	}.catch {
		throw UseCaseException.TvShowException(it)
	}
}