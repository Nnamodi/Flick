package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.state.UiState
import com.roland.android.flick.utils.NavigationActions
import com.roland.android.flick.utils.ResponseConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class BaseViewModel(
	private val movieDetailsUseCase: GetMovieDetailsUseCase,
	private val tvShowDetailsUseCase: GetTvShowDetailsUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val movieDetailsFlow = MutableStateFlow<UiState<MovieDetailsModel>?>(null)
	var movieDetails by mutableStateOf(movieDetailsFlow.value ?: UiState.Loading); private set

	private val tvShowDetailsFlow = MutableStateFlow<UiState<TvShowDetailsModel>?>(null)
	var tvShowDetails by mutableStateOf(tvShowDetailsFlow.value ?: UiState.Loading); private set

	init {
		getTvShowDetails(549)
		viewModelScope.launch {
			movieDetailsFlow.collect {
				movieDetails = it ?: UiState.Loading
				Log.i("MoviesInfo", "Fetched movie details: $movieDetails")
			}
		}
		viewModelScope.launch {
			tvShowDetailsFlow.collect {
				tvShowDetails = it ?: UiState.Loading
				Log.i("MoviesInfo", "Fetched tvShow details: $tvShowDetails")
			}
		}
	}

	fun navigationActions(action: NavigationActions) {
		when (action) {
			is NavigationActions.GetMovieDetails -> getMovieDetails(action.movieId)
			is NavigationActions.GetTvShowDetails -> getTvShowDetails(action.seriesId)
		}
	}

	private fun getMovieDetails(movieId: Int) {
		viewModelScope.launch {
			movieDetailsUseCase.execute(GetMovieDetailsUseCase.Request(movieId))
				.map { converter.convertMovieDetailsData(it) }
				.collect {
					movieDetailsFlow.value = it
				}
		}
	}

	private fun getTvShowDetails(seriesId: Int) {
		viewModelScope.launch {
			tvShowDetailsUseCase.execute(GetTvShowDetailsUseCase.Request(seriesId))
				.map { converter.convertTvShowDetailsData(it) }
				.collect {
					tvShowDetailsFlow.value = it
				}
		}
	}

}