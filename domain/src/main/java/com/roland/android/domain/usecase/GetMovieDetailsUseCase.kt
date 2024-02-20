package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetMovieDetailsUseCase.Request, GetMovieDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchMovieDetails(request.movieId),
		movieRepository.fetchRecommendedMovies(request.movieId),
		movieRepository.fetchSimilarMovies(request.movieId),
		movieRepository.fetchMovieGenres(),
		tvShowRepository.fetchTvShowGenres()
	) { movieDetails, recommended, similar, movieGenres, seriesGenres ->
		Response(movieDetails, recommended, similar, movieGenres, seriesGenres)
	}

	data class Request(val movieId: Int) : UseCase.Request

	data class Response(
		val movieDetails: MovieDetails,
		val recommendedMovies: PagingData<Movie>,
		val similarMovies: PagingData<Movie>,
		val movieGenres: List<Genre>,
		val seriesGenres: List<Genre>
	) : UseCase.Response

}
