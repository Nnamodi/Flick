package com.roland.android.flick.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.state.MovieListUiState
import com.roland.android.flick.ui.components.MediumItemPoster
import com.roland.android.flick.ui.components.MovieListTopBar
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.shimmer.LoadingListUi
import com.roland.android.flick.ui.shimmer.MediumBoxItem
import com.roland.android.flick.utils.Extensions.getName
import com.roland.android.flick.utils.MovieListActions
import kotlinx.coroutines.launch

@Composable
fun MovieListScreen(
	uiState: MovieListUiState,
	category: String,
	action: (MovieListActions) -> Unit,
	navigateUp: () -> Unit
) {
	val (movieList) = uiState
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberLazyGridState()

	Scaffold(
		topBar = {
			MovieListTopBar(
				title = stringResource(category.getName()),
				navigateUp = navigateUp
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
									onClick = { action(MovieListActions.Retry(category)) }
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
			state = movieList,
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
			)

			if (clickedMovieItem.value != null) {
				val clickedItemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (clickedItemIsMovie) data.movieGenres else data.seriesGenres,
					viewMore = {},
					closeSheet = { clickedMovieItem.value = null }
				)
			}
		}
	}
}

@Composable
private fun MovieLists(
	paddingValues: PaddingValues,
	scrollState: LazyGridState,
	movies: LazyPagingItems<Movie>,
	onItemClick: (Movie) -> Unit,
) {
	LazyVerticalGrid(
		columns = GridCells.Adaptive(100.dp),
		modifier = Modifier
			.padding(paddingValues)
			.padding(horizontal = 6.dp),
		state = scrollState,
		contentPadding = PaddingValues(bottom = 50.dp)
	) {
		items(movies.itemCount) { index ->
			movies[index]?.let { movie ->
				MediumItemPoster(
					movie = movie,
					modifier = Modifier.padding(6.dp),
					onClick = onItemClick
				)
			}
		}
		movies.apply {
			items(21) {
				when (loadState.refresh) {
					is LoadState.Loading -> {
						MediumBoxItem(true, Modifier.padding(6.dp))
					}
					is LoadState.Error -> {
						MediumBoxItem(false, Modifier.padding(6.dp))
					}
					else -> {}
				}
			}
		}
	}
}