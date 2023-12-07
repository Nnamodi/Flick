package com.roland.android.domain.usecase

import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository
) : UseCase<GetMoviesUseCase.Request, GetMoviesUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchTrendingMovies(),
		movieRepository.fetchPopularMovies(),
		movieRepository.fetchNowPlayingMovies(),
		movieRepository.fetchTopRatedMovies(),
		movieRepository.fetchUpcomingMovies()
	) { trending, popular, nowPlaying, topRated, upcoming ->
		Response(trending, popular, nowPlaying, topRated, upcoming)
	}

	object Request : UseCase.Request

	data class Response(
		val trendingMovies: MovieList,
		val popularMovies: MovieList,
		val nowPlayingMovies: MovieList,
		val topRated: MovieList,
		val upcomingMovies: MovieList
	) : UseCase.Response

}
