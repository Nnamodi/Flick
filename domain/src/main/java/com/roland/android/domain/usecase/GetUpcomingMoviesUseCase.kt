package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetUpcomingMoviesUseCase.Request, GetUpcomingMoviesUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		movieRepository.fetchUpcomingMovies(),
		tvShowRepository.fetchShowsSoonToAir(),
		movieRepository.fetchMovieGenres(),
		tvShowRepository.fetchTvShowGenres()
	) { upcomingMovies, upcomingShows, movieGenres, seriesGenres ->
		Response(upcomingMovies, upcomingShows, movieGenres, seriesGenres)
	}

	object Request : UseCase.Request

	data class Response(
		val upcomingMovies: PagingData<Movie>,
		val upcomingShows: PagingData<Movie>,
		val movieGenres: GenreList,
		val seriesGenres: GenreList
	) : UseCase.Response

}
