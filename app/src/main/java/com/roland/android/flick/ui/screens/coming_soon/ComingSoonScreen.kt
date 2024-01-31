package com.roland.android.flick.ui.screens.coming_soon

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.animatePagerItem
import com.roland.android.flick.utils.dynamicPageWidth
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComingSoonScreen(
	uiState: ComingSoonUiState,
	action: (ComingSoonActions) -> Unit,
	inFullScreen: (Boolean) -> Unit
) {
	val (movieData, selectedCategory) = uiState
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberScrollState()
	val windowSize = rememberWindowSize()
	var expanded by rememberSaveable { mutableStateOf(false) }
	val itemExpanded: (Boolean) -> Unit = {
		expanded = it; inFullScreen(it)
	}

	Scaffold(
		topBar = {
			ComingSoonTopBar(selectedCategory, expanded, action) { itemExpanded(false) }
		},
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				errorMessage.value?.let {
					Snackbar(
						modifier = Modifier
							.padding(16.dp)
							.padding(bottom = if (windowSize.width == WindowType.Portrait) NavigationBarHeight else 0.dp),
						action = {
							data.visuals.actionLabel?.let {
								TextButton(
									onClick = { action(ComingSoonActions.Retry) }
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
			movieData,
			loadingScreen = { error ->
				ComingSoonLoadingUi(scrollState, isLoading = error == null)
				errorMessage.value = error
				error?.let {
					val actionLabel = stringResource(R.string.retry)
					scope.launch {
						snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
					}
				}
			}
		) { movieData ->
			val movies = (if (selectedCategory == MOVIES)
				movieData.upcomingMovies else movieData.upcomingShows
			).collectAsLazyPagingItems()
			val moviesPagerState = rememberPagerState { 60 }
			val seriesPagerState = rememberPagerState { 60 }
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
					expanded || windowSize.width == WindowType.Landscape -> 0.dp
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
						pagerSnapDistance = PagerSnapDistance.atMost(5)
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
											animationSpec = tween(durationMillis = 1000)
										)
									} else itemExpanded(true)
								},
								viewMore = {}
							) { itemExpanded(false) }
						}
					}
					movies.loadStateUi(
						posterType = PosterType.ComingSoon,
						largeBoxItemModifier = Modifier.animatePagerItem(page, pagerState)
					) { error ->
						errorMessage.value = error
						error?.let {
							val actionLabel = stringResource(R.string.retry)
							scope.launch {
								snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
							}
						}
					}
				}
			}
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
		ComingSoonScreen(ComingSoonUiState(movieData), {}) {}
	}
}