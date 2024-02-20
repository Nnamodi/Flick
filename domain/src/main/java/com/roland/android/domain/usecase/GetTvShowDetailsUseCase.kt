package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.Series
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTvShowDetailsUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetTvShowDetailsUseCase.Request, GetTvShowDetailsUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> = combine(
		tvShowRepository.fetchTvShowDetails(request.seriesId),
		tvShowRepository.fetchRecommendedTvShows(request.seriesId),
		tvShowRepository.fetchSimilarTvShows(request.seriesId),
		movieRepository.fetchMovieGenres(),
		tvShowRepository.fetchTvShowGenres()
	) { movieDetails, recommended, similar, movieGenres, seriesGenres ->
		Response(movieDetails, recommended, similar, movieGenres, seriesGenres)
	}

	data class Request(val seriesId: Int) : UseCase.Request

	data class Response(
		val showDetails: Series,
		val recommendedShows: PagingData<Movie>,
		val similarShows: PagingData<Movie>,
		val movieGenres: List<Genre>,
		val seriesGenres: List<Genre>
	) : UseCase.Response

}
