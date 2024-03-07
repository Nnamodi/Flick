package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
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
		tvShowRepository.fetchTvShowGenres()
	) { trending, popular, airingToday, topRated, showGenres ->
		Response(trending, popular, airingToday, topRated, showGenres)
	}

	object Request : UseCase.Request

	data class Response(
		val trendingShows: PagingData<Movie>,
		val popularShows: PagingData<Movie>,
		val showsAiringToday: PagingData<Movie>,
		val topRatedShows: PagingData<Movie>,
		val showGenres: List<Genre>
	) : UseCase.Response

}
