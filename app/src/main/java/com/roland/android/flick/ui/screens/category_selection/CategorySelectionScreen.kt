package com.roland.android.flick.ui.screens.category_selection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.usecase.Collection
import com.roland.android.flick.R
import com.roland.android.flick.state.CategorySelectionUiState
import com.roland.android.flick.ui.components.MovieListTopBar
import com.roland.android.flick.ui.components.MovieLists
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.screens.list.LoadingListUi
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Extensions.getName
import kotlinx.coroutines.launch

@Composable
fun CategorySelectionScreen(
	uiState: CategorySelectionUiState,
	action: (CategorySelectionActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (movieData, selectedGenreIds, selectedCollection) = uiState
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberLazyGridState()
	val showSheet = rememberSaveable { mutableStateOf(selectedGenreIds.isEmpty()) }

	Scaffold(
		topBar = {
			CategorySelectionScreenTopBar(
				selectionSheetClosed = !showSheet.value,
				selectedCollection = selectedCollection,
				openSelectionSheet = { showSheet.value = true },
				navigateUp = navigate
			)
		},
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				errorMessage.value?.let {
					Snackbar(
						modifier = Modifier.padding(16.dp),
						action = {
							data.visuals.actionLabel?.let {
								TextButton(
									onClick = { action(CategorySelectionActions.Retry) }
								) { Text(it) }
							}
						}
					) {
						Text(data.visuals.message)
					}
				}
			}
		}
	) { paddingValues ->
		CommonScreen(
			state = movieData,
			loadingScreen = { error ->
				if (selectedGenreIds.isNotEmpty()) {
					LoadingListUi(scrollState, paddingValues, error == null)
				}
				errorMessage.value = error
				error?.let {
					val actionLabel = stringResource(R.string.retry)
					scope.launch {
						snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
					}
				}
			}
		) { data ->
			Box {
				MovieLists(
					paddingValues = paddingValues,
					scrollState = scrollState,
					movies = data.movieList.collectAsLazyPagingItems(),
					onItemClick = { clickedMovieItem.value = it }
				) { error ->
					errorMessage.value = error
					error?.let {
						val actionLabel = stringResource(R.string.retry)
						scope.launch {
							snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
						}
					}
				}

				CategorySelectionSheet(
					showSheet = showSheet.value,
					movieGenres = data.movieGenres,
					seriesGenres = data.seriesGenres,
					selectedGenreIds = selectedGenreIds,
					selectedCollection = selectedCollection,
					onCategoriesSelected = action,
					closeSheet = { showSheet.value = false },
					navigateUp = navigate
				)
			}

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
	}
}

@Composable
private fun CategorySelectionScreenTopBar(
	selectionSheetClosed: Boolean,
	selectedCollection: Collection,
	openSelectionSheet: () -> Unit,
	navigateUp: (Screens) -> Unit
) {
	AnimatedVisibility(
		visible = selectionSheetClosed,
		enter = slideInVertically(tween(300)) { -it },
		exit = slideOutVertically(tween(400)) { -it },
	) {
		MovieListTopBar(
			title = stringResource(selectedCollection.getName()),
			categoryScreen = true,
			openSelectionSheet = openSelectionSheet,
			navigateUp = navigateUp
		)
	}
}

@Preview
@Composable
fun CategorySelectionScreenPreview() {
	FlickTheme {
		CategorySelectionScreen(
			uiState = CategorySelectionUiState(),
			action = {}
		) {}
	}
}