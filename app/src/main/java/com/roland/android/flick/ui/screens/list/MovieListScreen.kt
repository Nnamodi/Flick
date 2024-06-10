package com.roland.android.flick.ui.screens.list

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.R
import com.roland.android.flick.state.MovieListUiState
import com.roland.android.flick.state.State
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
import com.roland.android.flick.utils.Extensions.refine

@Composable
fun MovieListScreen(
	uiState: MovieListUiState,
	category: String,
	action: (MovieListActions?) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (movieData, isCancellable, isRatedMedia, response) = uiState
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val actionResponseMessage = remember { mutableStateOf<String?>(null) }
	val scrollState = rememberLazyGridState()
	val context = LocalContext.current

	CommonScaffold(
		topBar = {
			TopBar(
				title = stringResource(category.getName()),
				navigateUp = navigate
			)
		}
	) { paddingValues ->
		CommonScreen(
			state = movieData,
			loadingScreen = { error ->
				LoadingListUi(scrollState, paddingValues, error == null)
				errorMessage.value = error
			}
		) { data ->
			val movies = data.movieList.collectAsLazyPagingItems()

			MovieLists(
				paddingValues = paddingValues,
				scrollState = scrollState,
				isCancellable = isCancellable,
				showUserRating = isRatedMedia,
				cancelRequestDone = response != null,
				movies = movies,
				onItemClick = { clickedMovieItem.value = it },
				onCancel = { mediaId, mediaType ->
					action(MovieListActions.RemoveFromList(mediaId, mediaType))
				},
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

		if (actionResponseMessage.value != null) {
			Snackbar(
				message = actionResponseMessage.value!!,
				paddingValues = paddingValues,
				onDismiss = { action(null) }
			)
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

		LaunchedEffect(response) {
			if (!isCancellable) return@LaunchedEffect
			actionResponseMessage.value = when (response) {
				is State.Error -> response.errorMessage.refine()
				is State.Success -> {
					if (response.data.success == true) {
						context.getString(R.string.media_removed)
					} else {
						response.data.statusMessage
					}
				}
				null -> null
			}
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