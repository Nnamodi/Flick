package com.roland.android.flick.viewModel

import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowUseCase
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
	private val tvShowsUseCase: GetTvShowUseCase,
	private val converter: ResponseConverter,
	movieDetailsUseCase: GetMovieDetailsUseCase,
	tvShowDetailsUseCase: GetTvShowDetailsUseCase,
	seasonDetailsUseCase: GetSeasonDetailsUseCase,
	castDetailsUseCase: GetCastDetailsUseCase
) : BaseViewModel(
	movieDetailsUseCase,
	tvShowDetailsUseCase,
	seasonDetailsUseCase,
	castDetailsUseCase,
	converter
) {

	init {
		loadTvShows()
	}

	private fun loadTvShows() {
		viewModelScope.launch {
			tvShowsUseCase.execute(GetTvShowUseCase.Request)
				.map { converter.convertTvShowsData(it) }
				.collect { data ->
					moviesState.update { it.copy(tvShows = data) }
				}
		}
	}

}