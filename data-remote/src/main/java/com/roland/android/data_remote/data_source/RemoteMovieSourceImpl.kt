package com.roland.android.data_remote.data_source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.roland.android.data_remote.network.service.MovieService
import com.roland.android.data_remote.paging.FavoritedMoviesPagingSource
import com.roland.android.data_remote.paging.MoviesByGenrePagingSource
import com.roland.android.data_remote.paging.MoviesByRegionPagingSource
import com.roland.android.data_remote.paging.MoviesRecommendedPagingSource
import com.roland.android.data_remote.paging.NowPlayingMoviesPagingSource
import com.roland.android.data_remote.paging.PopularMoviesPagingSource
import com.roland.android.data_remote.paging.RatedMoviesPagingSource
import com.roland.android.data_remote.paging.RecommendedMoviesPagingSource
import com.roland.android.data_remote.paging.SearchedMoviesAndShowsPagingSource
import com.roland.android.data_remote.paging.SearchedMoviesPagingSource
import com.roland.android.data_remote.paging.SimilarMoviesPagingSource
import com.roland.android.data_remote.paging.TopRatedMoviesPagingSource
import com.roland.android.data_remote.paging.TrendingMoviesPagingSource
import com.roland.android.data_remote.paging.UpcomingMoviesPagingSource
import com.roland.android.data_remote.paging.WatchlistedMoviesPagingSource
import com.roland.android.data_remote.utils.Constants.MAX_PAGE_SIZE
import com.roland.android.data_remote.utils.Converters.convertFromFavoriteMediaRequest
import com.roland.android.data_remote.utils.Converters.convertFromRateMediaRequest
import com.roland.android.data_remote.utils.Converters.convertFromWatchlistMediaRequest
import com.roland.android.data_remote.utils.Converters.convertToGenreList
import com.roland.android.data_remote.utils.Converters.convertToMovieDetails
import com.roland.android.data_remote.utils.Converters.convertToResponse
import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
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

	override fun fetchMoviesByGenre(genreIds: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				MoviesByGenrePagingSource(movieService, genreIds)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchMoviesByRegion(region: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				MoviesByRegionPagingSource(movieService, region)
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

	override fun searchMoviesAndShows(query: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				SearchedMoviesAndShowsPagingSource(movieService, query)
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

	override fun fetchMovieGenres(): Flow<List<Genre>> = flow {
		emit(movieService.fetchMovieGenres())
	}.map { genreListModel ->
		convertToGenreList(genreListModel.genres)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

//	--------------------------------User Authentication Required--------------------------------

	override fun favoriteMovie(
		accountId: Int,
		sessionId: String,
		request: FavoriteMediaRequest
	): Flow<Response> = flow {
		val requestModel = convertFromFavoriteMediaRequest(request)
		emit(movieService.favoriteMovie(accountId, sessionId, requestModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchFavoritedMovies(
		accountId: Int,
		sessionId: String
	): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				FavoritedMoviesPagingSource(movieService, accountId, sessionId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun fetchRecommendedMovies(accountId: String): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				MoviesRecommendedPagingSource(movieService, accountId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun watchlistMovie(
		accountId: Int,
		sessionId: String,
		request: WatchlistMediaRequest,
	): Flow<Response> = flow {
		val requestModel = convertFromWatchlistMediaRequest(request)
		emit(movieService.watchlistMovie(accountId, sessionId, requestModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchWatchlistedMovies(
		accountId: Int,
		sessionId: String
	): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				WatchlistedMoviesPagingSource(movieService, accountId, sessionId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

	override fun rateMovie(
		movieId: Int,
		sessionId: String,
		request: RateMediaRequest
	): Flow<Response> = flow {
		val requestModel = convertFromRateMediaRequest(request)
		emit(movieService.rateMovie(movieId, sessionId, requestModel))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun deleteMovieRating(
		movieId: Int,
		sessionId: String
	): Flow<Response> = flow {
		emit(movieService.deleteMovieRating(movieId, sessionId))
	}.map { responseModel ->
		convertToResponse(responseModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchRatedMovies(
		accountId: Int,
		sessionId: String
	): Flow<PagingData<Movie>> {
		return Pager(
			config = PagingConfig(
				pageSize = MAX_PAGE_SIZE,
				prefetchDistance = MAX_PAGE_SIZE / 2
			),
			pagingSourceFactory = {
				RatedMoviesPagingSource(movieService, accountId, sessionId)
			}
		).flow
			.distinctUntilChanged()
			.cachedIn(scope)
	}

}