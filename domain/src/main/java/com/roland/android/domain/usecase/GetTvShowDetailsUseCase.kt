package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.Series
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTvShowDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository,
	private val castRepository: CastRepository
) : UseCase<GetTvShowDetailsUseCase.Request, GetTvShowDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchRecommendedTvShows(request.seriesId),
		tvShowRepository.fetchSimilarTvShows(request.seriesId),
		tvShowRepository.fetchTvShowDetails(request.seriesId),
		castRepository.fetchMovieCasts(request.seriesId)
	) { recommended, similar, movieDetails, movieCasts ->
		Response(recommended, similar, movieDetails, movieCasts)
	}

	data class Request(val seriesId: Int) : UseCase.Request

	data class Response(
		val recommendedShows: PagingData<Movie>,
		val similarShows: PagingData<Movie>,
		val showDetails: Series,
		val showCasts: MovieCredits
	) : UseCase.Response

}
