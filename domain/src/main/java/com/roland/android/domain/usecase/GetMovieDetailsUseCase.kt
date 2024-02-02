package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository
) : UseCase<GetMovieDetailsUseCase.Request, GetMovieDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchMovieDetails(request.movieId),
		movieRepository.fetchRecommendedMovies(request.movieId),
		movieRepository.fetchSimilarMovies(request.movieId)
	) { movieDetails, recommended, similar ->
		Response(movieDetails, recommended, similar)
	}

	data class Request(val movieId: Int) : UseCase.Request

	data class Response(
		val movieDetails: MovieDetails,
		val recommendedMovies: PagingData<Movie>,
		val similarMovies: PagingData<Movie>
	) : UseCase.Response

}
