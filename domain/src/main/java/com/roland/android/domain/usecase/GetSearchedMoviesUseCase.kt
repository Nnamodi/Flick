package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSearchedMoviesUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetSearchedMoviesUseCase.Request, GetSearchedMoviesUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.searchMoviesAndShows(request.query),
		movieRepository.searchMovies(request.query),
		tvShowRepository.searchTvShows(request.query),
		movieRepository.fetchMovieGenres(),
		tvShowRepository.fetchTvShowGenres()
	) { moviesAndShows, movies, tvShows, movieGenre, seriesGenre ->
		Response(moviesAndShows, movies, tvShows, movieGenre, seriesGenre)
	}

	data class Request(val query: String) : UseCase.Request

	data class Response(
		val moviesAndShows: PagingData<Movie>,
		val movies: PagingData<Movie>,
		val tvShows: PagingData<Movie>,
		val movieGenre: GenreList,
		val seriesGenre: GenreList
	) : UseCase.Response

}
