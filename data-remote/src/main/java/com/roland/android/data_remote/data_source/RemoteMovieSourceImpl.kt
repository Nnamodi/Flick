package com.roland.android.data_remote.data_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.roland.android.data_remote.network.service.MovieService
import com.roland.android.data_remote.paging.AnimeCollectionPagingSource
import com.roland.android.data_remote.paging.BollywoodMoviesPagingSource
import com.roland.android.data_remote.paging.NowPlayingMoviesPagingSource
import com.roland.android.data_remote.paging.PopularMoviesPagingSource
import com.roland.android.data_remote.paging.RecommendedMoviesPagingSource
import com.roland.android.data_remote.paging.SearchedMoviesPagingSource
import com.roland.android.data_remote.paging.SimilarMoviesPagingSource
import com.roland.android.data_remote.paging.TopRatedMoviesPagingSource
import com.roland.android.data_remote.paging.TrendingMoviesPagingSource
import com.roland.android.data_remote.paging.UpcomingMoviesPagingSource
import com.roland.android.data_remote.utils.Constants.MAX_PAGE_SIZE
import com.roland.android.data_remote.utils.Converters.convertToGenreList
import com.roland.android.data_remote.utils.Converters.convertToMovieDetails
import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.UseCaseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteMovieSourceImpl @Inject constructor(
	private val movieService: MovieService,
	private val scope: CoroutineScope
) : RemoteMovieSource {

	override fun fetchTrendingMovies(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				TrendingMoviesPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchTopRatedMovies(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				TopRatedMoviesPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchNowPlayingMovies(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				NowPlayingMoviesPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchPopularMovies(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				PopularMoviesPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchUpcomingMovies(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				UpcomingMoviesPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchAnimeCollection(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				AnimeCollectionPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchBollywoodMovies(): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				BollywoodMoviesPagingSource(movieService)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchRecommendedMovies(movieId: Int): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				RecommendedMoviesPagingSource(movieService, movieId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchSimilarMovies(movieId: Int): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				SimilarMoviesPagingSource(movieService, movieId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun searchMovies(query: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				SearchedMoviesPagingSource(movieService, query)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchMovieDetails(movieId: Int): Flow<MovieDetails> = flow {
		emit(movieService.fetchMovieDetails(movieId))
	}.map { movieDetailsModel ->
		convertToMovieDetails(movieDetailsModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchMovieGenres(): Flow<GenreList> = flow {
		emit(movieService.fetchMovieGenres())
	}.map { genreListModel ->
		convertToGenreList(genreListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

}