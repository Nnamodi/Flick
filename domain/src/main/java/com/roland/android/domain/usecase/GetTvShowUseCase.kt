package com.roland.android.domain.usecase

import com.roland.android.domain.entity.Genre
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
		tvShowRepository.fetchTopRatedShows(),
		tvShowRepository.fetchPopularShows(),
		tvShowRepository.fetchShowsAiringToday(),
		tvShowRepository.fetchShowsSoonToAir(),
		tvShowRepository.fetchTvShowGenres()
	) { topRated, popular, airingToday, soonToAir, genres ->
		Response(topRated, popular, airingToday, soonToAir, genres)
	}

	object Request : UseCase.Request

	data class Response(
		val topRatedShows: MovieList,
		val popularShows: MovieList,
		val showsAiringToday: MovieList,
		val showsSoonToAir: MovieList,
		val genres: List<Genre>
	) : UseCase.Response

}
