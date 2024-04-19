package com.roland.android.flick.state

import android.net.Uri
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.usecase.Collection
import com.roland.android.flick.models.AccountMediaModel
import com.roland.android.flick.models.AccountModel
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.CategorySelectionModel
import com.roland.android.flick.models.ComingSoonModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.MovieListModel
import com.roland.android.flick.models.MoviesByGenreModel
import com.roland.android.flick.models.MoviesByRegionModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.ResponseModel
import com.roland.android.flick.models.SearchModel
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TokenModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.models.TvShowsByGenreModel
import com.roland.android.flick.models.TvShowsByRegionModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.ui.screens.search.SearchCategory
import com.roland.android.flick.utils.Constants.MOVIES

data class HomeUiState(
	val movies: State<MoviesModel>? = null,
	val moviesByGenre: State<MoviesByGenreModel>? = null,
	val moviesByRegion: State<MoviesByRegionModel>? = null,
	val tvShows: State<TvShowsModel>? = null,
	val tvShowsByGenre: State<TvShowsByGenreModel>? = null,
	val tvShowsByRegion: State<TvShowsByRegionModel>? = null,
	val selectedCategory: String = MOVIES
)

data class ComingSoonUiState(
	val movieData: State<ComingSoonModel>? = null,
	val selectedCategory: String = MOVIES
)

data class CategorySelectionUiState(
	val movieData: State<CategorySelectionModel>? = null,
	val selectedGenreIds: List<String> = emptyList(),
	val selectedCollection: Collection = Collection.MOVIES
)

data class MovieDetailsUiState(
	val movieDetails: State<MovieDetailsModel>? = null,
	val tvShowDetails: State<TvShowDetailsModel>? = null,
	val seasonDetails: State<SeasonDetailsModel>? = null,
	val castDetails: State<CastDetailsModel>? = null,
	val selectedSeasonNumber: Int = 1
)

data class MovieListUiState(
	val movieData: State<MovieListModel>? = null
)

data class SearchUiState(
	val movieData: State<SearchModel>? = State.Success(SearchModel()),
	val searchCategory: SearchCategory = SearchCategory.ALL,
	val searchQuery: String = ""
)

data class AccountUiState(
	val accountDetails: AccountDetails = AccountDetails(),
	val moviesData: State<AccountMediaModel>? = null,
	val showsData: State<AccountMediaModel>? = null,
)

data class AuthUiState(
	val tokenData: State<TokenModel>? = null,
	val responseData: State<ResponseModel>? = null,
	val accountData: State<AccountModel>? = null,
	val intentData: Uri? = null,
	val activityResumed: Boolean = false
)
