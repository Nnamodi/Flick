package com.roland.android.data_remote.data_source

import com.roland.android.data_remote.network.service.MovieService
import com.roland.android.data_remote.utils.Converters.convertToGenreList
import com.roland.android.data_remote.utils.Converters.convertToMovieDetails
import com.roland.android.data_remote.utils.Converters.convertToMovieList
import com.roland.android.data_repository.data_source.RemoteMovieSource
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.entity.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoteMovieSourceImpl @Inject constructor(
	private val movieService: MovieService
) : RemoteMovieSource {

	override fun fetchTrendingMovies(): Flow<MovieList> = flow {
		emit(movieService.fetchTrendingMovies())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchTopRatedMovies(): Flow<MovieList> = flow {
		emit(movieService.fetchTopRatedMovies())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchNowPlayingMovies(): Flow<MovieList> = flow {
		emit(movieService.fetchNowPlayingMovies())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchPopularMovies(): Flow<MovieList> = flow {
		emit(movieService.fetchPopularMovies())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchUpcomingMovies(): Flow<MovieList> = flow {
		emit(movieService.fetchUpcomingMovies())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchAnimeCollection(): Flow<MovieList> = flow {
		emit(movieService.fetchAnimeCollection())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchBollywoodMovies(): Flow<MovieList> = flow {
		emit(movieService.fetchBollywoodMovies())
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchRecommendedMovies(movieId: Int): Flow<MovieList> = flow {
		emit(movieService.fetchRecommendedMovies(movieId))
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
	}

	override fun fetchSimilarMovies(movieId: Int): Flow<MovieList> = flow {
		emit(movieService.fetchSimilarMovies(movieId))
	}.map { movieListModel ->
		convertToMovieList(movieListModel)
	}.catch {
		throw UseCaseException.MovieException(it)
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