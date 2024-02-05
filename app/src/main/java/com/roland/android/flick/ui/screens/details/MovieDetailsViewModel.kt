package com.roland.android.flick.ui.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.flick.state.MovieDetailsUiState
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
	private val movieDetailsUseCase: GetMovieDetailsUseCase,
	private val tvShowDetailsUseCase: GetTvShowDetailsUseCase,
	private val seasonDetailsUseCase: GetSeasonDetailsUseCase,
	private val castDetailsUseCase: GetCastDetailsUseCase,
	private val converter: ResponseConverter
) : ViewModel() {
	private var _movieDetailsUiState = MutableStateFlow(MovieDetailsUiState())
	var movieDetailsUiState by mutableStateOf(_movieDetailsUiState.value); private set

	init {
		viewModelScope.launch {
			_movieDetailsUiState.collect {
				movieDetailsUiState = it
				Log.i("MovieDetailsInfo", "Fetched item details: $it")
			}
		}
	}

	fun detailsRequest(action: DetailsRequest) {
		Log.i("NavigationInfo", "Action: $action")
		when (action) {
			is DetailsRequest.GetMovieDetails -> getMovieDetails(action.movieId)
			is DetailsRequest.GetTvShowDetails -> getTvShowDetails(action.seriesId)
			is DetailsRequest.GetSeasonDetails -> getSeasonDetails(action.seriesId, action.seasonNumber)
			is DetailsRequest.GetCastDetails -> getCastDetails(action.personId)
		}
	}

	private fun getMovieDetails(movieId: Int) {
		viewModelScope.launch {
			movieDetailsUseCase.execute(GetMovieDetailsUseCase.Request(movieId))
				.map { converter.convertMovieDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(movieDetails = data) }
				}
		}
	}

	private fun getTvShowDetails(seriesId: Int) {
		viewModelScope.launch {
			tvShowDetailsUseCase.execute(GetTvShowDetailsUseCase.Request(seriesId))
				.map { converter.convertTvShowDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(tvShowDetails = data) }
				}
		}
		getSeasonDetails(seriesId = seriesId, seasonNumber = 1)
	}

	private fun getSeasonDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int = 1
	) {
		viewModelScope.launch {
			seasonDetailsUseCase.execute(
				GetSeasonDetailsUseCase.Request(
					seriesId, seasonNumber, episodeNumber
				)
			)
				.map { converter.convertSeasonDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(seasonDetails = data) }
				}
		}
	}

	private fun getCastDetails(personId: Int) {
		viewModelScope.launch {
			castDetailsUseCase.execute(
				GetCastDetailsUseCase.Request(personId)
			)
				.map { converter.convertCastDetailsData(it) }
				.collect { data ->
					_movieDetailsUiState.update { it.copy(castDetails = data) }
				}
		}
	}

}