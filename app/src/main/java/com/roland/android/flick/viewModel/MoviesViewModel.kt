package com.roland.android.flick.viewModel

import androidx.lifecycle.viewModelScope
import com.roland.android.domain.usecase.GetCastDetailsUseCase
import com.roland.android.domain.usecase.GetFurtherMovieCollectionUseCase
import com.roland.android.domain.usecase.GetMovieDetailsUseCase
import com.roland.android.domain.usecase.GetMoviesUseCase
import com.roland.android.domain.usecase.GetSeasonDetailsUseCase
import com.roland.android.domain.usecase.GetTvShowDetailsUseCase
import com.roland.android.flick.utils.ResponseConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
	private val moviesUseCase: GetMoviesUseCase,
	private val furtherMovieUseCase: GetFurtherMovieCollectionUseCase,
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
		loadMovies()
		loadFurtherMovieCollections()
	}

	private fun loadMovies() {
		viewModelScope.launch {
			moviesUseCase.execute(GetMoviesUseCase.Request)
				.map { converter.convertMoviesData(it) }
				.collect { data ->
					moviesState.update { it.copy(movies = data) }
				}
		}
	}

	private fun loadFurtherMovieCollections() {
		viewModelScope.launch {
			furtherMovieUseCase.execute(GetFurtherMovieCollectionUseCase.Request)
				.map { converter.convertFurtherMoviesData(it) }
				.collect { data ->
					moviesState.update { it.copy(furtherMovies = data) }
				}
		}
	}

}