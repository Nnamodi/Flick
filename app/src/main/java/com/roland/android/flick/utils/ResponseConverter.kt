package com.roland.android.flick.utils

import com.roland.android.domain.entity.Result
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.UiState
import javax.inject.Inject

class ResponseConverter @Inject constructor() {

	fun convertMoviesData(
		result: Result<GetMoviesUseCase.Response>
	): UiState<MoviesModel> {
		return when (result) {
			is Result.Error -> {
				UiState.Error(result.exception.localizedMessage.orEmpty())
			}
			is Result.Success -> {
				UiState.Success(
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

}