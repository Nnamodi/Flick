package com.roland.android.domain.usecase

import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTvShowUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetTvShowUseCase.Request, GetTvShowUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchTrendingShows(),
		tvShowRepository.fetchPopularShows(),
		tvShowRepository.fetchShowsAiringToday(),
		tvShowRepository.fetchTopRatedShows(),
		tvShowRepository.fetchShowsSoonToAir()
	) { trending, popular, airingToday, topRated, soonToAir ->
		Response(trending, popular, airingToday, topRated, soonToAir)
	}

	object Request : UseCase.Request

	data class Response(
		val trendingShows: MovieList,
		val popularShows: MovieList,
		val showsAiringToday: MovieList,
		val topRatedShows: MovieList,
		val showsSoonToAir: MovieList
	) : UseCase.Response

}
