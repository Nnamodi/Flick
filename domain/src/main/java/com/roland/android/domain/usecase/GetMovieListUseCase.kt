package com.roland.android.domain.usecase

import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.MovieList
import com.roland.android.domain.repository.MovieRepository
import com.roland.android.domain.repository.TvShowRepository
import com.roland.android.domain.usecase.Category.ANIME
import com.roland.android.domain.usecase.Category.ANIME_SERIES
import com.roland.android.domain.usecase.Category.BOLLYWOOD_MOVIES
import com.roland.android.domain.usecase.Category.BOLLYWOOD_SERIES
import com.roland.android.domain.usecase.Category.IN_THEATRES
import com.roland.android.domain.usecase.Category.NEW_RELEASES
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
			ANIME -> movieRepository.fetchAnimeCollection()
			ANIME_SERIES -> tvShowRepository.fetchAnimeShows()
			BOLLYWOOD_MOVIES -> movieRepository.fetchBollywoodMovies()
			BOLLYWOOD_SERIES -> tvShowRepository.fetchBollywoodShows()
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
		val result: MovieList,
		val movieGenre: GenreList,
		val seriesGenre: GenreList
	) : UseCase.Response

}

enum class Category {
	TRENDING_MOVIES,
	TRENDING_SERIES,
	IN_THEATRES,
	NEW_RELEASES,
	TOP_RATED_MOVIES,
	TOP_RATED_SERIES,
	ANIME,
	ANIME_SERIES,
	BOLLYWOOD_MOVIES,
	BOLLYWOOD_SERIES,
	POPULAR_MOVIES,
	POPULAR_SERIES
}
