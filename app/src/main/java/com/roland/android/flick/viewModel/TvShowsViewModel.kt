package com.roland.android.flick.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.state.UiState
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
	private val tvShowsUseCase: GetTvShowUseCase,
	private val converter: ResponseConverter,
	movieDetailsUseCase: GetMovieDetailsUseCase,
	tvShowDetailsUseCase: GetTvShowDetailsUseCase
) : BaseViewModel(movieDetailsUseCase, tvShowDetailsUseCase, converter) {

	private val tvShowsFlow = MutableStateFlow<UiState<TvShowsModel>?>(null)
	var tvShows by mutableStateOf(tvShowsFlow.value ?: UiState.Loading); private set

	init {
		loadTvShows()

		viewModelScope.launch {
			tvShowsFlow.collect {
				tvShows = it ?: UiState.Loading
				Log.i("TvShowsInfo", "Fetched: $tvShows")
			}
		}
	}

	private fun loadTvShows() {
		viewModelScope.launch {
			tvShowsUseCase.execute(GetTvShowUseCase.Request)
				.map { converter.convertTvShowsData(it) }
				.collect {
					tvShowsFlow.value = it
				}
		}
	}

}