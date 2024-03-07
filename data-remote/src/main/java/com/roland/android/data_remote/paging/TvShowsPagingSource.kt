package com.roland.android.data_remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.roland.android.data_remote.network.service.TvShowService
import com.roland.android.data_remote.utils.Constants.INITIAL_PAGE
import com.roland.android.data_remote.utils.Converters.convertToMovieList
import com.roland.android.domain.entity.Movie

class TrendingShowsPagingSource(
	private val tvShowService: TvShowService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchTrendingShows()
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class TopRatedShowsPagingSource(
	private val tvShowService: TvShowService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchTopRatedShows(page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class NowAiringShowsPagingSource(
	private val tvShowService: TvShowService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchShowsAiringToday(page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class PopularShowsPagingSource(
	private val tvShowService: TvShowService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchPopularShows(page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class UpcomingShowsPagingSource(
	private val tvShowService: TvShowService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchShowsSoonToAir(page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class ShowsByGenrePagingSource(
	private val tvShowService: TvShowService,
	private val genreIds: String
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchShowsByGenre(genreIds, currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class ShowsByRegionPagingSource(
	private val tvShowService: TvShowService,
	private val region: String
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchShowsByRegion(region, currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class RecommendedShowsPagingSource(
	private val tvShowService: TvShowService,
	private val seriesId: Int
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchRecommendedTvShows(seriesId = seriesId, page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class SimilarShowsPagingSource(
	private val tvShowService: TvShowService,
	private val seriesId: Int
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.fetchSimilarTvShows(seriesId = seriesId, page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}

class SearchedShowsPagingSource(
	private val tvShowService: TvShowService,
	private val query: String
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = tvShowService.searchTvShows(query = query, page = currentPage)
			LoadResult.Page(
				data = convertToMovieList(movies).results,
				prevKey = if (currentPage == 1) null else currentPage - 1,
				nextKey = if (movies.results.isEmpty()) null else currentPage + 1
			)
		} catch (e: Exception) {
			LoadResult.Error(throwable = e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}
