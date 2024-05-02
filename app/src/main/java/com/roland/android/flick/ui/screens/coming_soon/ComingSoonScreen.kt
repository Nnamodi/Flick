package com.roland.android.flick.ui.screens.coming_soon

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.flick.R
import com.roland.android.flick.models.ComingSoonModel
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.models.SampleData.showsSoonToAir
import com.roland.android.flick.models.SampleData.upcomingMovies
import com.roland.android.flick.state.ComingSoonUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.ComingSoonTopBar
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.SnackbarDuration
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.WindowType.Portrait
import com.roland.android.flick.utils.animatePagerItem
import com.roland.android.flick.utils.dynamicPageWidth
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComingSoonScreen(
	uiState: ComingSoonUiState,
	action: (ComingSoonActions) -> Unit,
	navigate: (Screens) -> Unit,
	inFullScreen: (Boolean) -> Unit
) {
	val (movieData, selectedCategory) = uiState
	val scope = rememberCoroutineScope()
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberScrollState()
	val windowSize = rememberWindowSize()
	val inPortraitMode by remember(windowSize.width) {
		derivedStateOf { windowSize.width == Portrait }
	}
	var expanded by rememberSaveable { mutableStateOf(false) }
	val itemExpanded: (Boolean) -> Unit = {
		expanded = it; inFullScreen(it)
	}

	CommonScaffold(
		topBar = {
			ComingSoonTopBar(selectedCategory, expanded, action) { itemExpanded(false) }
		}
	) { paddingValues ->
		CommonScreen(
			movieData, paddingValues,
			loadingScreen = { error ->
				ComingSoonLoadingUi(scrollState, isLoading = error == null)
				errorMessage.value = error
			}
		) { movieData ->
			val movies = (if (selectedCategory == MOVIES)
				movieData.upcomingMovies else movieData.upcomingShows
			).collectAsLazyPagingItems()
			val moviesPagerState = rememberPagerState { 61 }
			val seriesPagerState = rememberPagerState { 61 }
			val pagerState = if (selectedCategory == MOVIES) moviesPagerState else seriesPagerState
			val screenWidth = LocalConfiguration.current.screenWidthDp
			val itemWidth = dynamicPageWidth(POSTER_WIDTH_X_LARGE)
			val startPadding by animateDpAsState(
				targetValue = if (expanded) 0.dp else (screenWidth.dp - itemWidth) / 2,
				animationSpec = tween(if (expanded) 700 else 1000),
				label = "start padding"
			)
			val bottomPadding by animateDpAsState(
				targetValue = when {
					expanded || !inPortraitMode -> 0.dp
					else -> NavigationBarHeight + 16.dp
				},
				animationSpec = tween(1000),
				label = "bottom padding"
			)
			val pageSize by animateDpAsState(
				targetValue = if (expanded) screenWidth.dp else itemWidth,
				animationSpec = tween(if (expanded) 700 else 1000),
				label = "page size"
			)

			BoxWithConstraints(
				modifier = Modifier
					.fillMaxSize()
					.padding(
						top = paddingValues.calculateTopPadding(),
						bottom = paddingValues.calculateBottomPadding()
					),
				contentAlignment = Alignment.Center
			) {
				HorizontalPager(
					state = pagerState,
					contentPadding = PaddingValues(
						start = startPadding,
						end = PADDING_WIDTH
					),
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = bottomPadding),
					pageSpacing = 14.dp,
					flingBehavior = PagerDefaults.flingBehavior(
						state = pagerState,
						pagerSnapDistance = PagerSnapDistance.atMost(movies.itemCount)
					),
					pageSize = PageSize.Fixed(pageSize),
					userScrollEnabled = !expanded
				) { page ->
					if (movies.itemCount > 0 && movies.itemCount > page) {
						movies[page]?.let { movie ->
							ComingSoonItem(
								movie = movie,
								genreList = if (selectedCategory == MOVIES) movieData.movieGenres else movieData.seriesGenres,
								expanded = expanded,
								itemPage = page,
								pagerState = pagerState,
								maxHeight = maxHeight,
								maxWidth = maxWidth,
								onExpand = {
									if (page != pagerState.currentPage) scope.launch {
										pagerState.animateScrollToPage(
											page = page,
											animationSpec = tween(500)
										)
									} else itemExpanded(true)
								},
								viewMore = navigate
							) { itemExpanded(false) }
						}
					}
					movies.loadStateUi(
						posterType = PosterType.ComingSoon,
						largeBoxItemModifier = Modifier.animatePagerItem(page, pagerState),
						error = { errorMessage.value = it }
					)
				}

				// This is a workaround to fix a bug
				LaunchedEffect(pagerState.currentPageOffsetFraction) {
					val lastItem = pagerState.currentPage == (pagerState.pageCount - 1)
					if (!lastItem) return@LaunchedEffect
					pagerState.animateScrollToPage(page = pagerState.pageCount - 4)
				}
			}
		}

		if (errorMessage.value != null) {
			Snackbar(
				message = errorMessage.value!!,
				paddingValues = paddingValues,
				modifier = Modifier.padding(bottom = if (windowSize.width == Portrait) NavigationBarHeight else 0.dp),
				actionLabel = stringResource(R.string.retry),
				action = { action(ComingSoonActions.Retry) },
				duration = SnackbarDuration.Indefinite
			)
		}
	}

	if (expanded) {
		BackHandler { itemExpanded(false) }
	}
}

@Preview(showBackground = true)
@Composable
private fun ComingSoonScreenPreview() {
	FlickTheme {
		val movieData = State.Success(
			ComingSoonModel(upcomingMovies, showsSoonToAir, genreList, genreList)
		)
		ComingSoonScreen(
			uiState = ComingSoonUiState(movieData),
			action = {},
			navigate = {},
			inFullScreen = {}
		)
	}
}