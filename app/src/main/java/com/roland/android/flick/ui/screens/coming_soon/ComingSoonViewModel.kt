package com.roland.android.flick.ui.screens.coming_soon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetUpcomingMoviesUseCase
import com.roland.android.flick.state.ComingSoonUiState
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComingSoonViewModel @Inject constructor(
	private val upcomingMoviesUseCase: GetUpcomingMoviesUseCase,
	private val converter: ResponseConverter
) : ViewModel() {

	private val _comingSoonUiState = MutableStateFlow(ComingSoonUiState())
	var comingSoonUiState by mutableStateOf(_comingSoonUiState.value); private set

	init {
		loadComingSoonData()

		viewModelScope.launch {
			_comingSoonUiState.collect {
				comingSoonUiState = it
			}
		}
	}

	private fun loadComingSoonData() {
		viewModelScope.launch {
			upcomingMoviesUseCase.execute(GetUpcomingMoviesUseCase.Request)
				.map { converter.convertComingSoonData(it) }
				.collect { data ->
					_comingSoonUiState.update { it.copy(movieData = data) }
				}
		}
	}

	fun comingSoonActions(action: ComingSoonActions) {
		when (action) {
			is ComingSoonActions.ToggleCategory -> toggleCategory()
			is ComingSoonActions.Retry -> retry()
		}
	}

	private fun retry() {
		_comingSoonUiState.update { it.copy(movieData = null) }
		loadComingSoonData()
	}

	private fun toggleCategory() {
		val currentCategory = comingSoonUiState.selectedCategory
		val selectedCategory = if (currentCategory == MOVIES) SERIES else MOVIES
		_comingSoonUiState.update {
			it.copy(selectedCategory = selectedCategory)
		}
	}

}