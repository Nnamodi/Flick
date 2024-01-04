package com.roland.android.flick.state

import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.utils.Constants.MOVIES

data class HomeUiState(
	val movies: State<MoviesModel>? = null,
	val furtherMovies: State<FurtherMoviesModel>? = null,
	val tvShows: State<TvShowsModel>? = null,
	val selectedCategory: String = MOVIES
)

data class ItemDetailsUiState(
	val movieDetails: State<MovieDetailsModel>,
	val tvShowDetails: State<TvShowDetailsModel>,
	val seasonDetails: State<SeasonDetailsModel>,
	val castDetails: State<CastDetailsModel>
)