package com.roland.android.flick.ui.screens.category_selection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CellTower
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.usecase.Collection
import com.roland.android.flick.R
import com.roland.android.flick.models.CategorySelectionModel
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.models.SampleData.trendingMovies
import com.roland.android.flick.state.CategorySelectionUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.MovieLists
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.SnackbarDuration
import com.roland.android.flick.ui.components.TopBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.screens.list.LoadingListUi
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Extensions.getName
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@Composable
fun CategorySelectionScreen(
	uiState: CategorySelectionUiState,
	action: (CategorySelectionActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (movieData, selectedGenreIds, selectedCollection) = uiState
	val scope = rememberCoroutineScope()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val dataLoadedSuccessfully = rememberSaveable { mutableStateOf(false) }
	val scrollState = rememberLazyGridState()
	val showSheet = rememberSaveable { mutableStateOf(selectedGenreIds.isEmpty()) }

	CommonScaffold(
		topBar = {
			CategorySelectionScreenTopBar(
				selectionSheetClosed = !(showSheet.value && dataLoadedSuccessfully.value),
				enableFilterIcon = dataLoadedSuccessfully.value,
				selectedCollection = selectedCollection,
				openSelectionSheet = { if (errorMessage.value == null) showSheet.value = true },
				navigateUp = navigate
			)
		}
	) { paddingValues ->
		CommonScreen(
			state = movieData,
			loadingScreen = { error ->
				dataLoadedSuccessfully.value = false
				if (selectedGenreIds.isNotEmpty()) {
					LoadingListUi(scrollState, paddingValues, error == null)
				} else {
					CategorySelectionSheetLoadingUi(error, paddingValues, action)
				}
				errorMessage.value = error
			}
		) { data -> dataLoadedSuccessfully.value = true
			Box {
				MovieLists(
					paddingValues = paddingValues,
					scrollState = scrollState,
					movies = data.movieList.collectAsLazyPagingItems(),
					onItemClick = { clickedMovieItem.value = it },
					error = { errorMessage.value = it }
				)

				CategorySelectionSheet(
					showSheet = showSheet.value,
					movieGenres = data.movieGenres,
					seriesGenres = data.seriesGenres,
					selectedGenreIds = selectedGenreIds,
					selectedCollection = selectedCollection,
					onCategoriesSelected = { scope.launch {
						action(it)
						scrollState.animateScrollToItem(0)
					} },
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

		if (errorMessage.value != null && selectedGenreIds.isNotEmpty()) {
			Snackbar(
				message = errorMessage.value!!,
				paddingValues = paddingValues,
				actionLabel = stringResource(R.string.retry),
				action = { action(CategorySelectionActions.Retry) },
				duration = SnackbarDuration.Indefinite
			)
		}
	}
}

@Composable
private fun CategorySelectionScreenTopBar(
	selectionSheetClosed: Boolean,
	enableFilterIcon: Boolean,
	selectedCollection: Collection,
	openSelectionSheet: () -> Unit,
	navigateUp: (Screens) -> Unit
) {
	AnimatedVisibility(
		visible = selectionSheetClosed,
		enter = slideInVertically(tween(300)) { -it },
		exit = slideOutVertically(tween(400)) { -it },
	) {
		TopBar(
			title = stringResource(selectedCollection.getName()),
			categoryScreen = true,
			enableFilterIcon = enableFilterIcon,
			openSelectionSheet = openSelectionSheet,
			navigateUp = navigateUp
		)
	}
}

@Composable
private fun CategorySelectionSheetLoadingUi(
	errorMessage: String?,
	paddingValues: PaddingValues,
	retry: (CategorySelectionActions) -> Unit
) {
	val windowSize = rememberWindowSize()
	val iconSize = if (windowSize.width == WindowType.Portrait) 200.dp else 100.dp

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(paddingValues)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (errorMessage == null) {
			CircularProgressIndicator()
		} else {
			Icon(
				imageVector = Icons.Rounded.CellTower,
				contentDescription = errorMessage,
				modifier = Modifier.size(iconSize)
			)
			Text(
				text = errorMessage,
				modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
				style = MaterialTheme.typography.titleLarge
			)
			Button(onClick = { retry(CategorySelectionActions.Retry) }) {
				Text(stringResource(R.string.retry))
			}
		}
	}
}

@Preview
@Composable
fun CategorySelectionScreenPreview() {
	FlickTheme(true) {
		val movieData = State.Success(
			CategorySelectionModel(trendingMovies, genreList, genreList)
		)
		CategorySelectionScreen(
			uiState = CategorySelectionUiState(movieData, listOf("")),
			action = {}
		) {}
	}
}