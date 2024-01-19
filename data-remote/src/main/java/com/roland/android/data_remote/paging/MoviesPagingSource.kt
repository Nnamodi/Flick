package com.roland.android.data_remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.roland.android.data_remote.network.service.MovieService
import com.roland.android.data_remote.utils.Constants.INITIAL_PAGE
import com.roland.android.data_remote.utils.Converters.convertToMovieList
import com.roland.android.domain.entity.Movie

class TrendingMoviesPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchTrendingMovies()
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

class TopRatedMoviesPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchTopRatedMovies(page = currentPage)
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

class NowPlayingMoviesPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchNowPlayingMovies(page = currentPage)
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

class PopularMoviesPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchPopularMovies(page = currentPage)
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

class UpcomingMoviesPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchUpcomingMovies(page = currentPage)
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

class AnimeCollectionPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchAnimeCollection(page = currentPage)
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

class BollywoodMoviesPagingSource(
	private val movieService: MovieService
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchBollywoodMovies(page = currentPage)
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

class RecommendedMoviesPagingSource(
	private val movieService: MovieService,
	private val movieId: Int
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchRecommendedMovies(movieId = movieId, page = currentPage)
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

class SimilarMoviesPagingSource(
	private val movieService: MovieService,
	private val movieId: Int
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.fetchSimilarMovies(movieId = movieId, page = currentPage)
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

class SearchedMoviesPagingSource(
	private val movieService: MovieService,
	private val query: String
) : PagingSource<Int, Movie>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
		return try {
			val currentPage = params.key ?: INITIAL_PAGE
			val movies = movieService.searchMovies(query = query, page = currentPage)
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
