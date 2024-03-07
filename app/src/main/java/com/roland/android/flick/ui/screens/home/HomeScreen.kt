package com.roland.android.flick.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.Category.BOLLYWOOD_MOVIES
import com.roland.android.domain.usecase.Category.BOLLYWOOD_SERIES
import com.roland.android.domain.usecase.Category.IN_THEATRES
import com.roland.android.domain.usecase.Category.KOREAN_MOVIES
import com.roland.android.domain.usecase.Category.K_DRAMA
import com.roland.android.domain.usecase.Category.NEW_RELEASES
import com.roland.android.domain.usecase.Category.NOLLYWOOD_MOVIES
import com.roland.android.domain.usecase.Category.NOLLYWOOD_SERIES
import com.roland.android.domain.usecase.Category.POPULAR_MOVIES
import com.roland.android.domain.usecase.Category.POPULAR_SERIES
import com.roland.android.domain.usecase.Category.TOP_RATED_MOVIES
import com.roland.android.domain.usecase.Category.TOP_RATED_SERIES
import com.roland.android.flick.R
import com.roland.android.flick.models.MoviesByRegionModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.models.SampleData.bollywoodMovies
import com.roland.android.flick.models.SampleData.bollywoodShows
import com.roland.android.flick.models.SampleData.trendingMovies
import com.roland.android.flick.models.SampleData.trendingShows
import com.roland.android.flick.models.TvShowsByRegionModel
import com.roland.android.flick.models.TvShowsModel
import com.roland.android.flick.state.HomeUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.ui.components.HomeTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.LargeItemPoster
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.ToggleButton
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.animatePagerItem
import com.roland.android.flick.utils.dynamicPageWidth
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
	uiState: HomeUiState,
	action: (HomeActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (movies, furtherMovies, shows, furtherShows, selectedCategory) = uiState
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val scrollState = rememberScrollState()
	val windowSize = rememberWindowSize()
	val seeMore: (Category) -> Unit = {
		navigate(Screens.MovieListScreen(it.name))
	}

	Scaffold(
		topBar = { HomeTopBar(navigate) },
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
									onClick = { action(HomeActions.Retry) }
								) { Text(it) }
							}
						},
						dismissAction = {
							IconButton(onClick = { errorMessage.value = null }) {
								Icon(Icons.Rounded.Close, stringResource(R.string.close))
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
			movies, furtherMovies, shows, furtherShows,
			paddingValues, loadingScreen = { error ->
				HomeLoadingUi(paddingValues, scrollState, isLoading = error == null)
				errorMessage.value = error
				error?.let {
					val actionLabel = stringResource(R.string.retry)
					scope.launch {
						snackbarHostState.showSnackbar(it, actionLabel, true, Indefinite)
					}
				}
			}
		) { movieData1, movieData2, showData1, showData2 ->
			val trendingMovies = (if (selectedCategory == MOVIES)
				movieData1.trending else showData1.trending
			).collectAsLazyPagingItems()
			val moviesPagerState = rememberPagerState { 20 }
			val seriesPagerState = rememberPagerState { 20 }
			val pagerState = if (selectedCategory == MOVIES) moviesPagerState else seriesPagerState
			val paddingTargetValue = remember(pagerState.currentPage) {
				derivedStateOf {
					if (pagerState.currentPage == 0) PADDING_WIDTH else 40.dp
				}
			}
			val startPaddingValue by animateDpAsState(
				targetValue = paddingTargetValue.value,
				label = "padding width value"
			)

			Column(
				modifier = Modifier
					.padding(bottom = paddingValues.calculateBottomPadding())
					.verticalScroll(scrollState),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Spacer(Modifier.height(paddingValues.calculateTopPadding()))
				ToggleButton(
					selectedOption = selectedCategory,
					modifier = Modifier.padding(bottom = 6.dp),
					onClick = action
				)
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(PADDING_WIDTH),
					horizontalArrangement = Arrangement.Start
				) {
					Header(stringResource(R.string.trending))
				}
				HorizontalPager(
					state = pagerState,
					contentPadding = PaddingValues(
						start = startPaddingValue,
						end = PADDING_WIDTH
					),
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 16.dp),
					pageSpacing = 14.dp,
					flingBehavior = PagerDefaults.flingBehavior(
						state = pagerState,
						pagerSnapDistance = PagerSnapDistance.atMost(20)
					),
					pageSize = PageSize.Fixed(dynamicPageWidth(POSTER_WIDTH_LARGE))
				) { page ->
					if (trendingMovies.itemCount > 0) {
						trendingMovies[page]?.let { movie ->
							LargeItemPoster(
								movie = movie,
								modifier = Modifier.animatePagerItem(page, pagerState),
								onClick = {
									if (page != pagerState.currentPage) scope.launch {
										pagerState.animateScrollToPage(
											page = page,
											animationSpec = tween(durationMillis = 1000)
										)
									}
									clickedMovieItem.value = it
								}
							)
						}
					}
					trendingMovies.loadStateUi(
						posterType = PosterType.Large,
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

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData1.nowPlaying else showData1.airingToday,
					header = stringResource(if (selectedCategory == MOVIES) R.string.in_theatres else R.string.new_releases),
					onMovieClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) IN_THEATRES else NEW_RELEASES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData1.topRated else showData1.topRated,
					header = stringResource(R.string.top_rated),
					onMovieClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) TOP_RATED_MOVIES else TOP_RATED_SERIES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData2.nollywood else showData2.nollywood,
					header = stringResource(R.string.nollywood),
					onMovieClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) NOLLYWOOD_MOVIES else NOLLYWOOD_SERIES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData2.korean else showData2.kDrama,
					header = stringResource(if (selectedCategory == MOVIES) R.string.korean else R.string.k_drama),
					onMovieClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) KOREAN_MOVIES else K_DRAMA) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData2.bollywood else showData2.bollywood,
					header = stringResource(R.string.bollywood),
					onMovieClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) BOLLYWOOD_MOVIES else BOLLYWOOD_SERIES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData1.popular else showData1.popular,
					header = stringResource(R.string.most_popular),
					onMovieClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) POPULAR_MOVIES else POPULAR_SERIES) }

				Spacer(Modifier.height(
					50.dp + (if (windowSize.width == WindowType.Portrait) NavigationBarHeight else 0.dp)
				))
			}

			if (clickedMovieItem.value != null) {
				val clickedItemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (clickedItemIsMovie) movieData1.genres else showData1.genres,
					viewMore = navigate,
					closeSheet = { clickedMovieItem.value = null }
				)
			}
		}
	}
}

@Preview
@Composable
fun HomeScreenPreview() {
	FlickTheme {
		val movies = State.Success(MoviesModel(trending = trendingMovies))
		val furtherMovies = State.Success(MoviesByRegionModel(bollywood = bollywoodMovies))
		val shows = State.Success(TvShowsModel(trending = trendingShows))
		val furtherShows = State.Success(TvShowsByRegionModel(bollywood = bollywoodShows))
		val uiState = HomeUiState(movies, furtherMovies, shows, furtherShows)
		HomeScreen(uiState, {}) {}
	}
}