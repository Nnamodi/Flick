package com.roland.android.flick.utils

import com.roland.android.domain.entity.Result
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.domain.repository.AutoStreamOptions
import com.roland.android.domain.repository.ThemeOptions
import com.roland.android.domain.usecase.AccountUseCase
import com.roland.android.domain.usecase.AuthUseCase
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetMovieListUseCase
import com.roland.android.domain.usecase.GetMoviesAndShowByGenreUseCase
import com.roland.android.domain.usecase.GetMoviesByGenreUseCase
import com.roland.android.domain.usecase.GetMoviesByRegionUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetSearchedMoviesUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowByRegionUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.domain.usecase.GetTvShowsByGenreUseCase
import com.roland.android.domain.usecase.GetUpcomingMoviesUseCase
import com.roland.android.domain.usecase.MediaUtilUseCase
import com.roland.android.domain.usecase.SettingsUseCase
import com.roland.android.flick.models.AccountModel
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.CategorySelectionModel
import com.roland.android.flick.models.ComingSoonModel
import com.roland.android.flick.models.FavoritedMediaModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MovieListModel
import com.roland.android.flick.models.MoviesByGenreModel
import com.roland.android.flick.models.MoviesByRegionModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.RatedMediaModel
import com.roland.android.flick.models.ResponseModel
import com.roland.android.flick.models.SearchModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TokenModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsByGenreModel
import com.roland.android.flick.models.TvShowsByRegionModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.models.WatchlistedMediaModel
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
						result.data.movieGenres
					)
				)
			}
		}
	}

	fun convertToWatchlistedMedia(
		result: Result<AccountUseCase.Response>
	): State<WatchlistedMediaModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					WatchlistedMediaModel(
						result.data.watchlistedMovies.refactor(),
						result.data.watchlistedShows.refactor(),
						result.data.movieGenres
					)
				)
			}
		}
	}

	fun convertToFavoritedMedia(
		result: Result<AccountUseCase.Response>
	): State<FavoritedMediaModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					FavoritedMediaModel(
						result.data.favoritedMovies.refactor(),
						result.data.favoritedShows.refactor(),
						result.data.showGenres
					)
				)
			}
		}
	}

	fun convertToRatedMedia(
		result: Result<AccountUseCase.Response>
	): State<RatedMediaModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					RatedMediaModel(
						result.data.ratedMovies.refactor(),
						result.data.ratedShows.refactor()
					)
				)
			}
		}
	}

	fun convertMoviesByGenreData(
		result: Result<GetMoviesByGenreUseCase.Response>
	): State<MoviesByGenreModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					MoviesByGenreModel(
						result.data.anime.refactor(),
						result.data.comedy.refactor(),
						result.data.romedy.refactor(),
						result.data.sciFi.refactor(),
						result.data.warStory.refactor()
					)
				)
			}
		}
	}

	fun convertMoviesByRegionData(
		result: Result<GetMoviesByRegionUseCase.Response>
	): State<MoviesByRegionModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					MoviesByRegionModel(
						result.data.nigerianMovies.refactor(),
						result.data.koreanMovies.refactor(),
						result.data.bollywoodMovies.refactor(),
						result.data.recommendedMovies.refactor()
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
						result.data.showGenres
					)
				)
			}
		}
	}

	fun convertTvShowsByGenreData(
		result: Result<GetTvShowsByGenreUseCase.Response>
	): State<TvShowsByGenreModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					TvShowsByGenreModel(
						result.data.anime.refactor(),
						result.data.comedy.refactor(),
						result.data.romedy.refactor(),
						result.data.sciFi.refactor(),
						result.data.warStory.refactor()
					)
				)
			}
		}
	}

	fun convertTvShowsByRegionData(
		result: Result<GetTvShowByRegionUseCase.Response>
	): State<TvShowsByRegionModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					TvShowsByRegionModel(
						result.data.nigerianShows.refactor(),
						result.data.koreanShows.refactor(),
						result.data.bollywoodShows.refactor(),
						result.data.recommendedShows.refactor()
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
						result.data.upcomingMovies.refactor(),
						result.data.upcomingShows.refactor(),
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

	fun convertTokenData(
		result: Result<AuthUseCase.Response>
	): State<TokenModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					TokenModel(
						result.data.requestTokenResponse,
						result.data.accessTokenResponse
					)
				)
			}
		}
	}

	fun convertResponseData(
		result: Result<AuthUseCase.Response>
	): State<ResponseModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					ResponseModel(
						result.data.sessionIdResponse,
						result.data.response
					)
				)
			}
		}
	}

	fun convertAccountData(
		result: Result<AuthUseCase.Response>
	): State<AccountModel> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					AccountModel(
						result.data.accountDetails,
						result.data.accountId
					)
				)
			}
		}
	}

	fun convertResponse(
		result: Result<MediaUtilUseCase.Response>
	): State<Response> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(
					Response(
						result.data.response.statusCode,
						result.data.response.statusMessage,
						result.data.response.success
					)
				)
			}
		}
	}

	/** Settings **/
	fun convertThemeResponse(
		result: Result<SettingsUseCase.Response>
	): State<ThemeOptions> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(result.data.theme)
			}
		}
	}

	fun convertAutoReloadResponse(
		result: Result<SettingsUseCase.Response>
	): State<Boolean> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(result.data.autoReloadData)
			}
		}
	}

	fun convertAutoStreamResponse(
		result: Result<SettingsUseCase.Response>
	): State<AutoStreamOptions> {
		return when (result) {
			is Result.Error -> {
				State.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				State.Success(result.data.autoStreamTrailers)
			}
		}
	}

}