package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.SearchCategory.MOVIES
import com.roland.android.domain.usecase.SearchCategory.TV_SHOWS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSearchedMoviesUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetSearchedMoviesUseCase.Request, GetSearchedMoviesUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		val movieListFlow = when (request.searchCategory) {
			MOVIES -> movieRepository.searchMovies(request.query)
			TV_SHOWS -> tvShowRepository.searchTvShows(request.query)
		}
		return combine(
			movieListFlow,
			movieRepository.fetchMovieGenres(),
			tvShowRepository.fetchTvShowGenres()
		) { movieList, movieGenre, seriesGenre ->
			Response(movieList, movieGenre, seriesGenre)
		}
	}

	data class Request(
		val query: String,
		val searchCategory: SearchCategory
	) : UseCase.Request

	data class Response(
		val movieList: PagingData<Movie>,
		val movieGenre: GenreList,
		val seriesGenre: GenreList
	) : UseCase.Response

}

enum class SearchCategory {
	MOVIES, TV_SHOWS
}