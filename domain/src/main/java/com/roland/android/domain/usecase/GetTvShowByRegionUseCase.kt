package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.Constant.BOLLYWOOD
import com.roland.android.domain.Constant.HALLYUWOOD
import com.roland.android.domain.Constant.NOLLYWOOD
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTvShowByRegionUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetTvShowByRegionUseCase.Request, GetTvShowByRegionUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchShowsByRegion(NOLLYWOOD),
		tvShowRepository.fetchShowsByRegion(HALLYUWOOD),
		tvShowRepository.fetchShowsByRegion(BOLLYWOOD)
	) { nigerian, korean, bollywood ->
		Response(nigerian, korean, bollywood)
	}

	object Request : UseCase.Request

	data class Response(
		val nigerianShows: PagingData<Movie>,
		val koreanShows: PagingData<Movie>,
		val bollywoodShows: PagingData<Movie>
	) : UseCase.Response

}
