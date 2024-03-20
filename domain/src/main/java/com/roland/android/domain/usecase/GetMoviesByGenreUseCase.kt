package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.Constant.ANIME
import com.roland.android.domain.Constant.COMEDY
import com.roland.android.domain.Constant.ROMEDY_MOVIES
import com.roland.android.domain.Constant.SCI_FI_MOVIES
import com.roland.android.domain.Constant.WAR_STORY_MOVIES
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository
) : UseCase<GetMoviesByGenreUseCase.Request, GetMoviesByGenreUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchMoviesByGenre(ANIME),
		movieRepository.fetchMoviesByGenre(COMEDY),
		movieRepository.fetchMoviesByGenre(ROMEDY_MOVIES),
		movieRepository.fetchMoviesByGenre(SCI_FI_MOVIES),
		movieRepository.fetchMoviesByGenre(WAR_STORY_MOVIES)
	) { anime, comedy, romedy, sciFi, warStory ->
		Response(anime, comedy, romedy, sciFi, warStory)
	}

	object Request : UseCase.Request

	data class Response(
		val anime: PagingData<Movie>,
		val comedy: PagingData<Movie>,
		val romedy: PagingData<Movie>,
		val sciFi: PagingData<Movie>,
		val warStory: PagingData<Movie>
	) : UseCase.Response

}