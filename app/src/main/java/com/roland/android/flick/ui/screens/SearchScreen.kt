package com.roland.android.flick.ui.screens

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
import androidx.compose.runtime.LaunchedEffect
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
import com.roland.android.flick.R
import com.roland.android.flick.state.SearchUiState
import com.roland.android.flick.ui.components.MovieLists
import com.roland.android.flick.ui.components.SearchTopBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.shimmer.LoadingListUi
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.SearchActions
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
	uiState: SearchUiState,
	action: (SearchActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberLazyGridState()

	Scaffold(
		topBar = { SearchTopBar(uiState, action, navigate) },
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				errorMessage.value?.let {
					Snackbar(
						modifier = Modifier.padding(16.dp),
						action = {
							data.visuals.actionLabel?.let {
								TextButton(
									onClick = { action(SearchActions.Retry(uiState.searchQuery)) }
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
			state = uiState.movieData,
			loadingScreen = { error ->
				LoadingListUi(scrollState, paddingValues, error == null)
				errorMessage.value = error
				error?.let {
					val actionLabel = stringResource(R.string.retry)
					scope.launch {
						snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
					}
				}
			}
		) { data ->
			val movies = data.movieList.collectAsLazyPagingItems()

			MovieLists(
				paddingValues = paddingValues,
				scrollState = scrollState,
				movies = movies,
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

			if (clickedMovieItem.value != null) {
				val clickedItemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (clickedItemIsMovie) data.movieGenres else data.seriesGenres,
					viewMore = {},
					closeSheet = { clickedMovieItem.value = null }
				)
			}

			LaunchedEffect(uiState.searchQuery) {
				scope.launch { scrollState.animateScrollToItem(0) }
			}
		}
	}
}

@Preview
@Composable
fun SearchScreenPreview() {
	FlickTheme {
		SearchScreen(
			uiState = SearchUiState(),
			action = {},
			navigate = {}
		)
	}
}