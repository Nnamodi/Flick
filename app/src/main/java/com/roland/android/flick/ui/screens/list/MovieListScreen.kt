package com.roland.android.flick.ui.screens.list

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.R
import com.roland.android.flick.state.MovieListUiState
import com.roland.android.flick.ui.components.MovieLists
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.SnackbarDuration
import com.roland.android.flick.ui.components.TopBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Extensions.getName

@Composable
fun MovieListScreen(
	uiState: MovieListUiState,
	category: String,
	action: (MovieListActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (movieList) = uiState
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberLazyGridState()

	CommonScaffold(
		topBar = {
			TopBar(
				title = stringResource(category.getName()),
				navigateUp = navigate
			)
		}
	) { paddingValues ->
		CommonScreen(
			state = movieList,
			loadingScreen = { error ->
				LoadingListUi(scrollState, paddingValues, error == null)
				errorMessage.value = error
			}
		) { data ->
			val movies = data.movieList.collectAsLazyPagingItems()

			MovieLists(
				paddingValues = paddingValues,
				scrollState = scrollState,
				movies = movies,
				onItemClick = { clickedMovieItem.value = it },
				error = { errorMessage.value = it }
			)

			if (clickedMovieItem.value != null) {
				val clickedItemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (clickedItemIsMovie) data.movieGenres else data.seriesGenres,
					viewMore = navigate,
					closeSheet = { clickedMovieItem.value = null }
				)
			}
		}

		if (errorMessage.value != null) {
			Snackbar(
				message = errorMessage.value!!,
				paddingValues = paddingValues,
				actionLabel = stringResource(R.string.retry),
				action = { action(MovieListActions.Retry(category)) },
				duration = SnackbarDuration.Indefinite
			)
		}
	}
}

@Preview
@Composable
fun MovieListScreenPreview() {
	FlickTheme {
		MovieListScreen(
			uiState = MovieListUiState(),
			category = Category.IN_THEATRES.name,
			action = {},
			navigate = {}
		)
	}
}