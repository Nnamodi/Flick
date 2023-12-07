package com.roland.android.domain.usecase

import com.roland.android.domain.entity.Episode
import com.roland.android.domain.entity.MovieCredits
import com.roland.android.domain.entity.Season
import com.roland.android.domain.repository.CastRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSeasonDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository,
	private val castRepository: CastRepository
) : UseCase<GetSeasonDetailsUseCase.Request, GetSeasonDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchSeasonDetails(request.seriesId, request.seasonNumber),
		tvShowRepository.fetchEpisodeDetails(request.seriesId, request.seasonNumber, request.episodeNumber),
		castRepository.fetchMovieCasts(request.seriesId)
	) { season, episode, movieCasts ->
		Response(season, episode, movieCasts)
	}

	data class Request(
		val seriesId: Int,
		val seasonNumber: Int,
		val episodeNumber: Int
	) : UseCase.Request

	data class Response(
		val season: Season,
		val episode: Episode,
		val showCasts: MovieCredits
	) : UseCase.Response

}
