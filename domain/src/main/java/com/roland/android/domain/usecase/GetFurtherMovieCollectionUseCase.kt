package com.roland.android.domain.usecase

import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFurtherMovieCollectionUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository
) : UseCase<GetFurtherMovieCollectionUseCase.Request, GetFurtherMovieCollectionUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchBollywoodMovies(),
		movieRepository.fetchAnimeCollection(),
		movieRepository.fetchMovieGenres()
	) { bollywood, anime, movieGenres ->
		Response(bollywood, anime, movieGenres)
	}

	object Request : UseCase.Request

	data class Response(
		val bollywoodMovies: MovieList,
		val animeCollection: MovieList,
		val movieGenres: List<Genre>
	) : UseCase.Response

}
