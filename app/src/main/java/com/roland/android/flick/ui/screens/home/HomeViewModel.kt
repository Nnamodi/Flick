package com.roland.android.flick.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetMoviesByGenreUseCase
import com.roland.android.domain.usecase.GetMoviesByRegionUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetTvShowByRegionUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.domain.usecase.GetTvShowsByGenreUseCase
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
	private val moviesByGenreUseCase: GetMoviesByGenreUseCase,
	private val moviesByRegionUseCase: GetMoviesByRegionUseCase,
	private val tvShowsUseCase: GetTvShowUseCase,
	private val tvShowByGenreUseCase: GetTvShowsByGenreUseCase,
	private val tvShowByRegionUseCase: GetTvShowByRegionUseCase,
	private val converter: ResponseConverter,
) : ViewModel() {

	private val _homeUiState = MutableStateFlow(HomeUiState())
	var homeUiState by mutableStateOf(_homeUiState.value); private set

	init {
		loadMovies()
		loadMoviesByGenre()
		loadMoviesByRegion()
		loadTvShows()
		loadTvShowsByGenre()
		loadTvShowsByRegion()

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

	private fun loadMoviesByGenre() {
		viewModelScope.launch {
			if (homeUiState.moviesByGenre is State.Error) _homeUiState.update { it.copy(moviesByGenre = null) }
			moviesByGenreUseCase.execute(GetMoviesByGenreUseCase.Request)
				.map { converter.convertMoviesByGenreData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(moviesByGenre = data) }
				}
		}
	}

	private fun loadMoviesByRegion() {
		viewModelScope.launch {
			if (homeUiState.moviesByRegion is State.Error) _homeUiState.update { it.copy(moviesByRegion = null) }
			moviesByRegionUseCase.execute(GetMoviesByRegionUseCase.Request)
				.map { converter.convertMoviesByRegionData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(moviesByRegion = data) }
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

	private fun loadTvShowsByGenre() {
		viewModelScope.launch {
			if (homeUiState.tvShowsByGenre is State.Error) _homeUiState.update { it.copy(tvShowsByGenre = null) }
			tvShowByGenreUseCase.execute(GetTvShowsByGenreUseCase.Request)
				.map { converter.convertTvShowsByGenreData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(tvShowsByGenre = data) }
				}
		}
	}

	private fun loadTvShowsByRegion() {
		viewModelScope.launch {
			if (homeUiState.tvShowsByRegion is State.Error) _homeUiState.update { it.copy(tvShowsByRegion = null) }
			tvShowByRegionUseCase.execute(GetTvShowByRegionUseCase.Request)
				.map { converter.convertTvShowsByRegionData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(tvShowsByRegion = data) }
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
		loadMoviesByGenre()
		loadMoviesByRegion()
		loadTvShows()
		loadTvShowsByGenre()
		loadTvShowsByRegion()
	}

	private fun toggleCategory(category: String) {
		_homeUiState.update {
			it.copy(selectedCategory = category)
		}
	}

}