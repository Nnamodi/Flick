package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Series
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTvShowDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetTvShowDetailsUseCase.Request, GetTvShowDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchTvShowDetails(request.seriesId),
		tvShowRepository.fetchRecommendedTvShows(request.seriesId),
		tvShowRepository.fetchSimilarTvShows(request.seriesId)
	) { movieDetails, recommended, similar ->
		Response(movieDetails, recommended, similar)
	}

	data class Request(val seriesId: Int) : UseCase.Request

	data class Response(
		val showDetails: Series,
		val recommendedShows: PagingData<Movie>,
		val similarShows: PagingData<Movie>
	) : UseCase.Response

}
