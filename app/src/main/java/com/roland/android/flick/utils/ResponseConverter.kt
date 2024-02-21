package com.roland.android.flick.utils

import com.roland.android.domain.entity.Result
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetFurtherTvShowUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetMovieListUseCase
import com.roland.android.domain.usecase.GetMoviesAndShowByGenreUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetSearchedMoviesUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.domain.usecase.GetUpcomingMoviesUseCase
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.CategorySelectionModel
import com.roland.android.flick.models.ComingSoonModel
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.FurtherTvShowsModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MovieListModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.SearchModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.state.State
import com.roland.android.flick.utils.Extensions.refactor
import javax.inject.Inject

class ResponseConverter @Inject constructor() {

	fun convertMoviesData(
		result: Result<GetMoviesUseCase.Response>
	): State<MoviesModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					MoviesModel(
						result.data.trendingMovies.refactor(),
						result.data.popularMovies.refactor(),
						result.data.nowPlayingMovies.refactor(),
						result.data.topRated.refactor(),
						result.data.upcomingMovies.refactor()
					)
				)
			}
		}
	}

	fun convertFurtherMoviesData(
		result: Result<GetFurtherMovieCollectionUseCase.Response>
	): State<FurtherMoviesModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					FurtherMoviesModel(
						result.data.bollywoodMovies.refactor(),
						result.data.animeCollection.refactor(),
						result.data.movieGenres
					)
				)
			}
		}
	}

	fun convertMovieDetailsData(
		result: Result<GetMovieDetailsUseCase.Response>
	): State<MovieDetailsModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					MovieDetailsModel(
						result.data.movieDetails,
						result.data.recommendedMovies.refactor(),
						result.data.similarMovies.refactor(),
						result.data.movieGenres,
						result.data.seriesGenres
					)
				)
			}
		}
	}

	fun convertTvShowsData(
		result: Result<GetTvShowUseCase.Response>
	): State<TvShowsModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					TvShowsModel(
						result.data.trendingShows.refactor(),
						result.data.popularShows.refactor(),
						result.data.showsAiringToday.refactor(),
						result.data.topRatedShows.refactor(),
						result.data.showsSoonToAir.refactor()
					)
				)
			}
		}
	}

	fun convertFurtherTvShowsData(
		result: Result<GetFurtherTvShowUseCase.Response>
	): State<FurtherTvShowsModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					FurtherTvShowsModel(
						result.data.bollywoodShows.refactor(),
						result.data.animeShows.refactor(),
						result.data.showGenres
					)
				)
			}
		}
	}

	fun convertComingSoonData(
		result: Result<GetUpcomingMoviesUseCase.Response>
	): State<ComingSoonModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					ComingSoonModel(
						result.data.upcomingMovies.refactor(showsVoteAverage = false),
						result.data.upcomingShows.refactor(showsVoteAverage = false),
						result.data.movieGenres,
						result.data.seriesGenres
					)
				)
			}
		}
	}

	fun convertMovieListData(
		result: Result<GetMovieListUseCase.Response>
	): State<MovieListModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					MovieListModel(
						result.data.movieList.refactor(),
						result.data.movieGenre,
						result.data.seriesGenre
					)
				)
			}
		}
	}

	fun convertCategorySelectionData(
		result: Result<GetMoviesAndShowByGenreUseCase.Response>
	): State<CategorySelectionModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					CategorySelectionModel(
						result.data.movieList.refactor(),
						result.data.movieGenres,
						result.data.seriesGenres
					)
				)
			}
		}
	}

	fun convertSearchedMovieData(
		result: Result<GetSearchedMoviesUseCase.Response>
	): State<SearchModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					SearchModel(
						result.data.moviesAndShows.refactor(),
						result.data.movies.refactor(),
						result.data.tvShows.refactor(),
						result.data.movieGenre,
						result.data.seriesGenre
					)
				)
			}
		}
	}

	fun convertTvShowDetailsData(
		result: Result<GetTvShowDetailsUseCase.Response>
	): State<TvShowDetailsModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					TvShowDetailsModel(
						result.data.showDetails,
						result.data.recommendedShows.refactor(),
						result.data.similarShows.refactor(),
						result.data.movieGenres,
						result.data.seriesGenres
					)
				)
			}
		}
	}

	fun convertSeasonDetailsData(
		result: Result<GetSeasonDetailsUseCase.Response>
	): State<SeasonDetailsModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					SeasonDetailsModel(
						result.data.season,
						result.data.episode,
						result.data.showCasts
					)
				)
			}
		}
	}

	fun convertCastDetailsData(
		result: Result<GetCastDetailsUseCase.Response>
	): State<CastDetailsModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(CastDetailsModel(result.data.cast))
			}
		}
	}

}