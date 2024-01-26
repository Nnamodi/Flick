package com.roland.android.flick.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.flick.R
import com.roland.android.flick.state.ComingSoonUiState
import com.roland.android.flick.ui.components.ComingSoonItemPoster
import com.roland.android.flick.ui.components.ComingSoonTopBar
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.shimmer.HomeLoadingUi
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.ComingSoonActions
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.animatePagerItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComingSoonScreen(
	uiState: ComingSoonUiState,
	action: (ComingSoonActions) -> Unit
) {
	val (movieData, selectedCategory) = uiState
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }

	Scaffold(
		topBar = { ComingSoonTopBar() },
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				errorMessage.value?.let {
					Snackbar(
						modifier = Modifier.padding(16.dp),
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
				HomeLoadingUi(paddingValues, rememberScrollState(), isLoading = error == null)
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
			val pagerState = rememberPagerState { 60 }
			val screenWidth = LocalConfiguration.current.screenWidthDp
			val startPadding = (screenWidth.dp - POSTER_WIDTH_X_LARGE) / 2

			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center
			) {
				HorizontalPager(
					state = pagerState,
					contentPadding = PaddingValues(
						start = startPadding,
						end = PADDING_WIDTH
					),
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 16.dp),
					pageSpacing = 14.dp,
					flingBehavior = PagerDefaults.flingBehavior(
						state = pagerState,
						pagerSnapDistance = PagerSnapDistance.atMost(5)
					),
					pageSize = PageSize.Fixed(POSTER_WIDTH_X_LARGE)
				) { page ->
					if (movies.itemCount > 0) {
						movies[page]?.let { movie ->
							ComingSoonItemPoster(
								movie = movie,
								modifier = Modifier.animatePagerItem(page, pagerState),
								onClick = {
									if (page != pagerState.currentPage) scope.launch {
										pagerState.animateScrollToPage(
											page = page,
											animationSpec = tween(durationMillis = 1000)
										)
									}
								}
							)
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
}

@Preview
@Composable
private fun ComingSoonScreenPreview() {
	FlickTheme {
		ComingSoonScreen(uiState = ComingSoonUiState()) {}
	}
}