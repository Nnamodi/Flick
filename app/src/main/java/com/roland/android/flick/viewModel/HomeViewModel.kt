package com.roland.android.flick.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetFurtherTvShowUseCase
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
			moviesUseCase.execute(GetMoviesUseCase.Request)
				.map { converter.convertMoviesData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(movies = data) }
				}
		}
	}

	private fun loadFurtherMovieCollections() {
		viewModelScope.launch {
			furtherMoviesUseCase.execute(GetFurtherMovieCollectionUseCase.Request)
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

	private fun loadFurtherTvShows() {
		viewModelScope.launch {
			furtherTvShowsUseCase.execute(GetFurtherTvShowUseCase.Request)
				.map { converter.convertFurtherTvShowsData(it) }
				.collect { data ->
					_homeUiState.update { it.copy(furtherTvShows = data) }
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