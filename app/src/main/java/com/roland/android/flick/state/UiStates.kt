package com.roland.android.flick.state

import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.ComingSoonModel
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.FurtherTvShowsModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MovieListModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.SearchModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.ui.screens.search.SearchCategory
import com.roland.android.flick.utils.Constants.MOVIES

data class HomeUiState(
	val movies: State<MoviesModel>? = null,
	val furtherMovies: State<FurtherMoviesModel>? = null,
	val tvShows: State<TvShowsModel>? = null,
	val furtherTvShows: State<FurtherTvShowsModel>? = null,
	val selectedCategory: String = MOVIES
)

data class ComingSoonUiState(
	val movieData: State<ComingSoonModel>? = null,
	val selectedCategory: String = MOVIES
)

data class MovieDetailsUiState(
	val movieDetails: State<MovieDetailsModel>? = null,
	val tvShowDetails: State<TvShowDetailsModel>? = null,
	val seasonDetails: State<SeasonDetailsModel>? = null,
	val castDetails: State<CastDetailsModel>? = null
)

data class MovieListUiState(
	val movieData: State<MovieListModel>? = null
)

data class SearchUiState(
	val movieData: State<SearchModel>? = State.Success(SearchModel()),
	val searchCategory: SearchCategory = SearchCategory.ALL,
	val searchQuery: String = ""
)
