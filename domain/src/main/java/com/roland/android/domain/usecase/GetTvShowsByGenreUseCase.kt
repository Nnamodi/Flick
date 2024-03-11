package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.Constant.ANIME
import com.roland.android.domain.Constant.COMEDY
import com.roland.android.domain.Constant.DOCUMENTARY
import com.roland.android.domain.Constant.ROMEDY_SERIES
import com.roland.android.domain.Constant.SCI_FI_SERIES
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTvShowsByGenreUseCase @Inject constructor(
	configuration: Configuration,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetTvShowsByGenreUseCase.Request, GetTvShowsByGenreUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchShowsByGenre(ANIME),
		tvShowRepository.fetchShowsByGenre(COMEDY),
		tvShowRepository.fetchShowsByGenre(DOCUMENTARY),
		tvShowRepository.fetchShowsByGenre(ROMEDY_SERIES),
		tvShowRepository.fetchShowsByGenre(SCI_FI_SERIES)
	) { anime, comedy, documentary, romedy, sciFi ->
		Response(anime, comedy, documentary, romedy, sciFi)
	}

	object Request : UseCase.Request

	data class Response(
		val anime: PagingData<Movie>,
		val comedy: PagingData<Movie>,
		val documentary: PagingData<Movie>,
		val romedy: PagingData<Movie>,
		val sciFi: PagingData<Movie>
	) : UseCase.Response

}