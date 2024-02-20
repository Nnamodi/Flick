package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMoviesAndShowByGenreUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetMoviesAndShowByGenreUseCase.Request, GetMoviesAndShowByGenreUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		if (request.collection != null) {
			return combine(
				movieRepository.fetchMoviesByGenre(request.genreIds),
				movieRepository.fetchMovieGenres(),
				tvShowRepository.fetchTvShowGenres()
			) { movieList, movieGenres, seriesGenres ->
				Response(movieList, movieGenres, seriesGenres)
			}
		} else {
			return combine(
				movieRepository.fetchMovieGenres(),
				tvShowRepository.fetchTvShowGenres()
			) { movieGenres, seriesGenres ->
				Response(PagingData.empty(), movieGenres, seriesGenres)
			}
		}
	}

	data class Request(
		val genreIds: String,
		val collection: Collection?
	) : UseCase.Request

	data class Response(
		val movieList: PagingData<Movie>,
		val movieGenres: List<Genre>,
		val seriesGenres: List<Genre>
	) : UseCase.Response

}

enum class Collection {
	MOVIES, SERIES
}
