package com.roland.android.flick.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetMoviesByRegionUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetTvShowByRegionUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.flick.state.HomeUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val moviesUseCase: GetMoviesUseCase,
	private val furtherMoviesUseCase: GetFurtherMovieCollectionUseCase,
	private val tvShowsUseCase: GetTvShowUseCase,
	private val furtherTvShowsUseCase: GetFurtherTvShowUseCase,
	private val converter: ResponseConverter,
) : ViewModel() {

	private val _homeUiState = MutableStateFlow(HomeUiState())
	var homeUiState by mutableStateOf(_homeUiState.value); private set

	init {
		loadMovies()
		loadFurtherMovieCollections()
		loadTvShows()
		loadFurtherTvShows()

		viewModelScope.launch {
			_homeUiState.collect {
				homeUiState = it
			}
		}
	}

	private fun loadMovies() {
		viewModelScope.launch {
			if (homeUiState.movies is State.Error) _homeUiState.update { it.copy(movies = null) }
			moviesUseCase.execute(GetMoviesUseCase.Request)
				.map { converter.convertMoviesData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(movies = data) }
				}
		}
	}

	private fun loadFurtherMovieCollections() {
		viewModelScope.launch {
			if (homeUiState.moviesByRegion is State.Error) _homeUiState.update { it.copy(moviesByRegion = null) }
			furtherMoviesUseCase.execute(GetFurtherMovieCollectionUseCase.Request)
				.map { converter.convertFurtherMoviesData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(furtherMovies = data) }
				}
		}
	}

	private fun loadTvShows() {
		viewModelScope.launch {
			if (homeUiState.tvShows is State.Error) _homeUiState.update { it.copy(tvShows = null) }
			tvShowsUseCase.execute(GetTvShowUseCase.Request)
				.map { converter.convertTvShowsData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(tvShows = data) }
				}
		}
	}

	private fun loadFurtherTvShows() {
		viewModelScope.launch {
			if (homeUiState.tvShowsByRegion is State.Error) _homeUiState.update { it.copy(tvShowsByRegion = null) }
			furtherTvShowsUseCase.execute(GetFurtherTvShowUseCase.Request)
				.map { converter.convertFurtherTvShowsData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(furtherTvShows = data) }
				}
		}
	}

	fun homeActions(action: HomeActions) {
		when (action) {
			is HomeActions.ToggleCategory -> toggleCategory(action.category)
			is HomeActions.Retry -> retry()
		}
	}

	private fun retry() {
		loadMovies()
		loadFurtherMovieCollections()
		loadTvShows()
		loadFurtherTvShows()
	}

	private fun toggleCategory(category: String) {
		_homeUiState.update {
			it.copy(selectedCategory = category)
		}
	}

}