package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val castRepository: CastRepository
) : UseCase<GetMovieDetailsUseCase.Request, GetMovieDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchRecommendedMovies(request.movieId),
		movieRepository.fetchSimilarMovies(request.movieId),
		movieRepository.fetchMovieDetails(request.movieId),
		castRepository.fetchMovieCasts(request.movieId),
	) { recommended, similar, movieDetails, movieCasts ->
		Response(recommended, similar, movieDetails, movieCasts)
	}

	data class Request(val movieId: Int) : UseCase.Request

	data class Response(
		val recommendedMovies: PagingData<Movie>,
		val similarMovies: PagingData<Movie>,
		val movieDetails: MovieDetails,
		val movieCasts: MovieCredits
	) : UseCase.Response

}
