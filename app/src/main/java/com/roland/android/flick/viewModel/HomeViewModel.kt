package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.flick.state.HomeUiState
import com.roland.android.flick.utils.HomeScreenActions
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
	private val furtherMovieUseCase: GetFurtherMovieCollectionUseCase,
	private val tvShowsUseCase: GetTvShowUseCase,
	private val converter: ResponseConverter,
) : ViewModel() {

	private val _homeUiState = MutableStateFlow(HomeUiState())
	var homeUiState by mutableStateOf(_homeUiState.value); private set

	init {
		loadMovies()
		loadFurtherMovieCollections()
		loadTvShows()

		viewModelScope.launch {
			_homeUiState.collect {
				homeUiState = it
				Log.i("MoviesInfo", "Fetched movies: $homeUiState")
			}
		}
	}

	private fun loadMovies() {
		viewModelScope.launch {
			moviesUseCase.execute(GetMoviesUseCase.Request)
				.map { converter.convertMoviesData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(movies = data) }
				}
		}
	}

	private fun loadFurtherMovieCollections() {
		viewModelScope.launch {
			furtherMovieUseCase.execute(GetFurtherMovieCollectionUseCase.Request)
				.map { converter.convertFurtherMoviesData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(furtherMovies = data) }
				}
		}
	}

	private fun loadTvShows() {
		viewModelScope.launch {
			tvShowsUseCase.execute(GetTvShowUseCase.Request)
				.map { converter.convertTvShowsData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(tvShows = data) }
				}
		}
	}

	fun homeScreenAction(action: HomeScreenActions) {
		when (action) {
			is HomeScreenActions.ToggleCategory -> toggleCategory(action.category)
		}
	}

	private fun toggleCategory(category: String) {
		_homeUiState.update {
			it.copy(selectedCategory = category)
		}
	}

}