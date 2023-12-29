package com.roland.android.flick.state

import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsModel

data class MoviesUiState(
	val movies: State<out MoviesModel> = State.Loading,
	val furtherMovies: State<out FurtherMoviesModel> = State.Loading,
	val tvShows: State<out TvShowsModel> = State.Loading
)

data class ItemDetailsUiState(
	val movieDetails: State<out MovieDetailsModel> = State.Loading,
	val tvShowDetails: State<out TvShowDetailsModel> = State.Loading,
	val seasonDetails: State<out SeasonDetailsModel> = State.Loading,
	val castDetails: State<out CastDetailsModel> = State.Loading
)