package com.roland.android.flick.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.Category.ANIME
import com.roland.android.domain.usecase.Category.ANIME_SERIES
import com.roland.android.domain.usecase.Category.BOLLYWOOD_MOVIES
import com.roland.android.domain.usecase.Category.BOLLYWOOD_SERIES
import com.roland.android.domain.usecase.Category.IN_THEATRES
import com.roland.android.domain.usecase.Category.NEW_RELEASES
import com.roland.android.domain.usecase.Category.POPULAR_MOVIES
import com.roland.android.domain.usecase.Category.POPULAR_SERIES
import com.roland.android.domain.usecase.Category.TOP_RATED_MOVIES
import com.roland.android.domain.usecase.Category.TOP_RATED_SERIES
import com.roland.android.flick.R
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.HomeUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.ui.components.HomeTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.LargeItemPoster
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.ToggleButton
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.shimmer.HomeLoadingUi
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Extensions.loadStateUi
import com.roland.android.flick.utils.HomeActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
	val seeMore: (Category) -> Unit = {
		navigate(Screens.MovieListScreen(it.name))
	}

	Scaffold(
		topBar = { HomeTopBar(navigate) },
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				errorMessage.value?.let {
					Snackbar(
						modifier = Modifier.padding(16.dp),
						action = {
							data.visuals.actionLabel?.let {
								TextButton(
									onClick = { action(HomeActions.Retry) }
								) { Text(it) }
							}
						}
					) {
						Text(data.visuals.message)
					}
				}
			}
		}
	) { _ ->
		CommonScreen(
			movies, furtherMovies, shows, furtherShows,
			loadingScreen = { error ->
				HomeLoadingUi(scrollState, error == null)
				errorMessage.value = error
				error?.let {
					val actionLabel = stringResource(R.string.retry)
					scope.launch {
						snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
					}
				}
			}
		) { movieData1, movieData2, showData1, showData2 ->
			val pagerState = rememberPagerState { 20 }
			var horizontalPaddingValue by remember { mutableStateOf(PADDING_WIDTH) }

			Column(
				modifier = Modifier.verticalScroll(scrollState),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Spacer(Modifier.height(64.dp))
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
						start = horizontalPaddingValue,
						end = PADDING_WIDTH
					),
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 16.dp),
					pageSpacing = 14.dp,
					flingBehavior = PagerDefaults.flingBehavior(
						state = pagerState,
						pagerSnapDistance = PagerSnapDistance.atMost(3)
					),
					pageSize = PageSize.Fixed(POSTER_WIDTH_LARGE)
				) { page ->
					val trendingMovies = (if (selectedCategory == MOVIES)
						movieData1.trending else showData1.trending
					).collectAsLazyPagingItems()

					if (trendingMovies.itemCount > 0) {
						trendingMovies[page]?.let { movie ->
							LargeItemPoster(
								movie = movie,
								onClick = { clickedMovieItem.value = it }
							)
						}
					}
					trendingMovies.loadStateUi(PosterType.Large) { error ->
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
					onItemClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) IN_THEATRES else NEW_RELEASES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData1.topRated else showData1.topRated,
					header = stringResource(R.string.top_rated),
					onItemClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) TOP_RATED_MOVIES else TOP_RATED_SERIES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData2.anime else showData2.anime,
					header = stringResource(R.string.anime_collection),
					onItemClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) ANIME else ANIME_SERIES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData2.bollywood else showData2.bollywood,
					header = stringResource(R.string.bollywood),
					onItemClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) BOLLYWOOD_MOVIES else BOLLYWOOD_SERIES) }

				HorizontalPosters(
					pagingData = if (selectedCategory == MOVIES) movieData1.popular else showData1.popular,
					header = stringResource(R.string.most_popular),
					onItemClick = { clickedMovieItem.value = it }
				) { seeMore(if (selectedCategory == MOVIES) POPULAR_MOVIES else POPULAR_SERIES) }

				Spacer(Modifier.height(50.dp))
			}

			if (clickedMovieItem.value != null) {
				val clickedItemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (clickedItemIsMovie) movieData2.genres else showData2.genres,
					viewMore = {},
					closeSheet = { clickedMovieItem.value = null }
				)
			}

			LaunchedEffect(pagerState) {
				snapshotFlow { pagerState.currentPage }.collect { page ->
					horizontalPaddingValue = if (page == 0) PADDING_WIDTH else 40.dp
				}
			}
		}
	}
}

@Preview
@Composable
fun HomeScreenPreview() {
	FlickTheme {
		val movieList = MutableStateFlow(PagingData.from(listOf(Movie(), Movie(), Movie())))
		val movies = State.Success(MoviesModel(trending = movieList))
		val furtherMovies = State.Success(FurtherMoviesModel(anime = movieList))
		val uiState = HomeUiState(movies, furtherMovies)
		HomeScreen(uiState, {}) {}
	}
}