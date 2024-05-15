package com.roland.android.flick.ui.screens.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.state.SearchUiState
import com.roland.android.flick.ui.components.ChipSet
import com.roland.android.flick.ui.components.MovieLists
import com.roland.android.flick.ui.components.SearchTopBar
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.SnackbarDuration
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.screens.list.LoadingListUi
import com.roland.android.flick.ui.screens.search.SearchCategory.ALL
import com.roland.android.flick.ui.screens.search.SearchCategory.MOVIES
import com.roland.android.flick.ui.screens.search.SearchCategory.TV_SHOWS
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.DynamicContainer
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
	uiState: SearchUiState,
	action: (SearchActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (movieData, searchCategory, searchQuery) = uiState
	val scope = rememberCoroutineScope()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val allScrollState = rememberLazyGridState()
	val moviesScrollState = rememberLazyGridState()
	val seriesScrollState = rememberLazyGridState()

	CommonScaffold(
		topBar = { SearchTopBar(uiState, action, navigate) }
	) { paddingValues ->
		CommonScreen(
			state = movieData,
			loadingScreen = { error ->
				LoadingListUi(
					scrollState = rememberLazyGridState(),
					paddingValues = paddingValues,
					isLoading = error == null,
					isSearchScreen = true
				)
				errorMessage.value = error
			}
		) { data ->
			val windowSize = rememberWindowSize()
			val chipModifier = if (windowSize.width == WindowType.Portrait) {
				Modifier.fillMaxWidth()
			} else Modifier.fillMaxHeight()
			val searchQueryEntered = searchQuery.isNotEmpty()
			val pagerState = rememberPagerState(
				initialPage = SearchCategory.values().indexOf(searchCategory),
				pageCount = { 3 }
			)

			DynamicContainer(Modifier.padding(paddingValues)) {
				ChipSet(
					modifier = chipModifier,
					selectedCategory = searchCategory,
					onValueChanged = {
						val pageIndex = when (it) {
							ALL -> 0
							MOVIES -> 1
							TV_SHOWS -> 2
						}
						if (searchQueryEntered) {
							scope.launch { pagerState.animateScrollToPage(pageIndex) }
						}
						action(SearchActions.ToggleCategory(it))
					}
				)

				HorizontalPager(
					state = pagerState,
					beyondBoundsPageCount = 3,
					userScrollEnabled = false
				) { page ->
					when (page) {
						0 -> MovieLists(
							scrollState = allScrollState,
							searchQueryEntered = searchQueryEntered,
							movies = data.moviesAndShows.collectAsLazyPagingItems(),
							onItemClick = { clickedMovieItem.value = it },
							error = { errorMessage.value = it }
						)

						1 -> MovieLists(
							scrollState = moviesScrollState,
							searchQueryEntered = searchQueryEntered,
							movies = data.movies.collectAsLazyPagingItems(),
							onItemClick = { clickedMovieItem.value = it },
							error = { errorMessage.value = it }
						)

						2 -> MovieLists(
							scrollState = seriesScrollState,
							searchQueryEntered = searchQueryEntered,
							movies = data.tvShows.collectAsLazyPagingItems(),
							onItemClick = { clickedMovieItem.value = it },
							error = { errorMessage.value = it }
						)
					}
				}
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

			val rememberScrollStates = rememberSaveable { mutableStateOf(false) }
			LaunchedEffect(searchQuery) {
				if (rememberScrollStates.value) {
					rememberScrollStates.value = false
					return@LaunchedEffect
				}
				scope.launch {
					allScrollState.animateScrollToItem(0)
					moviesScrollState.animateScrollToItem(0)
					seriesScrollState.animateScrollToItem(0)
				}
			}

			DisposableEffect(Unit) {
				onDispose {
					rememberScrollStates.value = true
					if (searchQuery.isNotEmpty()) return@onDispose
					action(SearchActions.ToggleCategory(ALL))
				}
			}
		}

		if (errorMessage.value != null) {
			Snackbar(
				message = errorMessage.value!!,
				paddingValues = paddingValues,
				actionLabel = stringResource(R.string.retry),
				action = { action(SearchActions.Retry(searchQuery)) },
				duration = SnackbarDuration.Indefinite
			)
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