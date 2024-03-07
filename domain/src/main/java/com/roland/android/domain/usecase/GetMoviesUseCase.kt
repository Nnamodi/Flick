package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
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
		movieRepository.fetchMovieGenres()
	) { trending, popular, nowPlaying, topRated, movieGenres ->
		Response(trending, popular, nowPlaying, topRated, movieGenres)
	}

	object Request : UseCase.Request

	data class Response(
		val trendingMovies: PagingData<Movie>,
		val popularMovies: PagingData<Movie>,
		val nowPlayingMovies: PagingData<Movie>,
		val topRated: PagingData<Movie>,
		val movieGenres: List<Genre>
	) : UseCase.Response

}
