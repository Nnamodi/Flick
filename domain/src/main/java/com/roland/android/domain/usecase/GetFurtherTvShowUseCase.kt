package com.roland.android.domain.usecase

import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFurtherTvShowUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetFurtherTvShowUseCase.Request, GetFurtherTvShowUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchBollywoodShows(),
		tvShowRepository.fetchAnimeShows(),
		tvShowRepository.fetchTvShowGenres()
	) { bollywood, anime, genres ->
		Response(bollywood, anime, genres)
	}

	object Request : UseCase.Request

	data class Response(
		val bollywoodShows: MovieList,
		val animeShows: MovieList,
		val genres: GenreList
	) : UseCase.Response

}
