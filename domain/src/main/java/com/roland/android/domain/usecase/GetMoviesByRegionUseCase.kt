package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.Constant.BOLLYWOOD
import com.roland.android.domain.Constant.HALLYUWOOD
import com.roland.android.domain.Constant.NOLLYWOOD
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMoviesByRegionUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository
) : UseCase<GetMoviesByRegionUseCase.Request, GetMoviesByRegionUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchMoviesByRegion(NOLLYWOOD),
		movieRepository.fetchMoviesByRegion(HALLYUWOOD),
		movieRepository.fetchMoviesByRegion(BOLLYWOOD),
		movieRepository.fetchRecommendedMovies(request.accountId)
	) { nigerian, korean, bollywood, recommendation ->
		Response(nigerian, korean, bollywood, recommendation)
	}

	data class Request(val accountId: String) : UseCase.Request

	data class Response(
		val nigerianMovies: PagingData<Movie>,
		val koreanMovies: PagingData<Movie>,
		val bollywoodMovies: PagingData<Movie>,
		val recommendedMovies: PagingData<Movie>
	) : UseCase.Response

}
