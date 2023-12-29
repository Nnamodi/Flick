package com.roland.android.flick.viewModel

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
import com.roland.android.flick.state.ItemDetailsUiState
import com.roland.android.flick.state.MoviesUiState
import com.roland.android.flick.utils.NavigationActions
import com.roland.android.flick.utils.ResponseConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel(
	private val movieDetailsUseCase: GetMovieDetailsUseCase,
	private val tvShowDetailsUseCase: GetTvShowDetailsUseCase,
	private val seasonDetailsUseCase: GetSeasonDetailsUseCase,
	private val castDetailsUseCase: GetCastDetailsUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	var moviesState = MutableStateFlow(MoviesUiState()); private set
	private var itemDetailsState = MutableStateFlow(ItemDetailsUiState())
	var moviesUiState by mutableStateOf(MoviesUiState()); private set
	var itemDetailsUiState by mutableStateOf(ItemDetailsUiState()); private set

	init {
		viewModelScope.launch {
			moviesState.collect {
				moviesUiState = it
				Log.i("MoviesInfo", "Fetched movies and tv-shows: $it")
			}
		}
		viewModelScope.launch {
			itemDetailsState.collect {
				itemDetailsUiState = it
				Log.i("ItemDetailsInfo", "Fetched item details: $it")
			}
		}
	}

	fun navigationActions(action: NavigationActions) {
		when (action) {
			is NavigationActions.GetMovieDetails -> getMovieDetails(action.movieId)
			is NavigationActions.GetTvShowDetails -> getTvShowDetails(action.seriesId)
			is NavigationActions.GetSeasonDetails -> getSeasonDetails(action.seriesId, action.seasonNumber, action.episodeNumber)
			is NavigationActions.GetCastDetails -> getCastDetails(action.personId)
		}
	}

	private fun getMovieDetails(movieId: Int) {
		viewModelScope.launch {
			movieDetailsUseCase.execute(GetMovieDetailsUseCase.Request(movieId))
				.map { converter.convertMovieDetailsData(it) }
				.collect { data ->
					itemDetailsState.update { it.copy(movieDetails = data) }
				}
		}
	}

	private fun getTvShowDetails(seriesId: Int) {
		viewModelScope.launch {
			tvShowDetailsUseCase.execute(GetTvShowDetailsUseCase.Request(seriesId))
				.map { converter.convertTvShowDetailsData(it) }
				.collect { data ->
					itemDetailsState.update { it.copy(tvShowDetails = data) }
				}
		}
	}

	private fun getSeasonDetails(
		seriesId: Int,
		seasonNumber: Int,
		episodeNumber: Int
	) {
		viewModelScope.launch {
			seasonDetailsUseCase.execute(
				GetSeasonDetailsUseCase.Request(
					seriesId, seasonNumber, episodeNumber
				)
			)
				.map { converter.convertSeasonDetailsData(it) }
				.collect { data ->
					itemDetailsState.update { it.copy(seasonDetails = data) }
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
					itemDetailsState.update { it.copy(castDetails = data) }
				}
		}
	}

}