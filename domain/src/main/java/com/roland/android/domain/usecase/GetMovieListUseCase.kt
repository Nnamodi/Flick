package com.roland.android.domain.usecase

import androidx.paging.PagingData
import com.roland.android.domain.Constant.BOLLYWOOD
import com.roland.android.domain.Constant.HALLYUWOOD
import com.roland.android.domain.Constant.NOLLYWOOD
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.Category.BOLLYWOOD_MOVIES
import com.roland.android.domain.usecase.Category.BOLLYWOOD_SERIES
import com.roland.android.domain.usecase.Category.IN_THEATRES
import com.roland.android.domain.usecase.Category.KOREAN_MOVIES
import com.roland.android.domain.usecase.Category.K_DRAMA
import com.roland.android.domain.usecase.Category.NEW_RELEASES
import com.roland.android.domain.usecase.Category.NOLLYWOOD_MOVIES
import com.roland.android.domain.usecase.Category.NOLLYWOOD_SERIES
import com.roland.android.domain.usecase.Category.POPULAR_MOVIES
import com.roland.android.domain.usecase.Category.POPULAR_SERIES
import com.roland.android.domain.usecase.Category.TOP_RATED_MOVIES
import com.roland.android.domain.usecase.Category.TOP_RATED_SERIES
import com.roland.android.domain.usecase.Category.TRENDING_MOVIES
import com.roland.android.domain.usecase.Category.TRENDING_SERIES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
	configuration: Configuration,
	private val movieRepository: MovieRepository,
	private val tvShowRepository: TvShowRepository
) : UseCase<GetMovieListUseCase.Request, GetMovieListUseCase.Response>(configuration) {

	override fun process(request: Request): Flow<Response> {
		val movieListFlow = when (request.category) {
			TRENDING_MOVIES -> movieRepository.fetchTrendingMovies()
			TRENDING_SERIES -> tvShowRepository.fetchTrendingShows()
			IN_THEATRES -> movieRepository.fetchNowPlayingMovies()
			NEW_RELEASES -> tvShowRepository.fetchShowsAiringToday()
			TOP_RATED_MOVIES -> movieRepository.fetchTopRatedMovies()
			TOP_RATED_SERIES -> tvShowRepository.fetchTopRatedShows()
			NOLLYWOOD_MOVIES -> movieRepository.fetchMoviesByRegion(NOLLYWOOD)
			NOLLYWOOD_SERIES -> tvShowRepository.fetchShowsByRegion(NOLLYWOOD)
			KOREAN_MOVIES -> movieRepository.fetchMoviesByRegion(HALLYUWOOD)
			K_DRAMA -> tvShowRepository.fetchShowsByRegion(HALLYUWOOD)
			BOLLYWOOD_MOVIES -> movieRepository.fetchMoviesByRegion(BOLLYWOOD)
			BOLLYWOOD_SERIES -> tvShowRepository.fetchShowsByRegion(BOLLYWOOD)
			POPULAR_MOVIES -> movieRepository.fetchPopularMovies()
			POPULAR_SERIES -> tvShowRepository.fetchPopularShows()
		}
		return combine(
			movieListFlow,
			movieRepository.fetchMovieGenres(),
			tvShowRepository.fetchTvShowGenres()
		) { movieList, movieGenre, seriesGenre ->
			Response(movieList, movieGenre, seriesGenre)
		}
	}

	data class Request(val category: Category) : UseCase.Request

	data class Response(
		val movieList: PagingData<Movie>,
		val movieGenre: List<Genre>,
		val seriesGenre: List<Genre>
	) : UseCase.Response

}

enum class Category {
	TRENDING_MOVIES,
	TRENDING_SERIES,
	IN_THEATRES,
	NEW_RELEASES,
	TOP_RATED_MOVIES,
	TOP_RATED_SERIES,
	NOLLYWOOD_MOVIES,
	NOLLYWOOD_SERIES,
	KOREAN_MOVIES,
	K_DRAMA,
	BOLLYWOOD_MOVIES,
	BOLLYWOOD_SERIES,
	POPULAR_MOVIES,
	POPULAR_SERIES
}
