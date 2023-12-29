package com.roland.android.flick.utils

import com.roland.android.domain.entity.Result
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.state.State
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
						result.data.trendingMovies,
						result.data.popularMovies,
						result.data.nowPlayingMovies,
						result.data.topRated,
						result.data.upcomingMovies
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
						result.data.bollywoodMovies,
						result.data.animeCollection,
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
						result.data.recommendedMovies,
						result.data.similarMovies,
						result.data.movieDetails,
						result.data.movieCasts
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
						result.data.topRatedShows,
						result.data.popularShows,
						result.data.showsAiringToday,
						result.data.showsSoonToAir,
						result.data.genres
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
						result.data.recommendedShows,
						result.data.similarShows,
						result.data.showDetails,
						result.data.showCasts
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