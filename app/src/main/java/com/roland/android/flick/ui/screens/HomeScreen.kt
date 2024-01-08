package com.roland.android.flick.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieList
import com.roland.android.flick.R
import com.roland.android.flick.models.FurtherMoviesModel
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.HomeUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.HomeTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.LargeItemPoster
import com.roland.android.flick.ui.components.ToggleButton
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.HomeScreenActions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
	uiState: HomeUiState,
	action: (HomeScreenActions) -> Unit
) {
	val (movies, furtherMovies, shows, furtherShows, selectedCategory) = uiState
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }

	Scaffold(
		topBar = { HomeTopBar() }
	) { paddingValues ->
		CommonScreen(movies, furtherMovies, shows, furtherShows) { movieData1, movieData2, showData1, showData2 ->
			val pagerState = rememberPagerState()
			var horizontalPaddingValue by remember { mutableStateOf(PADDING_WIDTH) }

			Column(
				modifier = Modifier
					.padding(paddingValues)
					.verticalScroll(rememberScrollState()),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				ToggleButton(
					selectedOption = selectedCategory,
					modifier = Modifier.padding(bottom = 6.dp),
					onClick = action
				)
				Text(
					text = stringResource(R.string.trending_movies),
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = PADDING_WIDTH, bottom = 20.dp),
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Start
				)
				HorizontalPager(
					pageCount = 20,
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
						movieData1.trending.results else showData1.trending.results
					).take(20)

					LargeItemPoster(
						movie = trendingMovies[page],
						onClick = { clickedMovieItem.value = it }
					)
				}

				HorizontalPosters(
					movieList = if (selectedCategory == MOVIES) movieData1.nowPlaying else showData1.airingToday,
					header = stringResource(if (selectedCategory == MOVIES) R.string.in_theatres else R.string.new_releases),
					onItemClick = { clickedMovieItem.value = it },
					seeAll = {}
				)

				HorizontalPosters(
					movieList = if (selectedCategory == MOVIES) movieData1.topRated else showData1.topRated,
					header = stringResource(R.string.top_rated_movies),
					onItemClick = { clickedMovieItem.value = it },
					seeAll = {}
				)

				HorizontalPosters(
					movieList = if (selectedCategory == MOVIES) movieData2.anime else showData2.anime,
					header = stringResource(R.string.anime_collection),
					onItemClick = { clickedMovieItem.value = it },
					seeAll = {}
				)

				HorizontalPosters(
					movieList = if (selectedCategory == MOVIES) movieData2.bollywood else showData2.bollywood,
					header = stringResource(R.string.bollywood_movies),
					onItemClick = { clickedMovieItem.value = it },
					seeAll = {}
				)

				HorizontalPosters(
					movieList = if (selectedCategory == MOVIES) movieData1.popular else showData1.popular,
					header = stringResource(R.string.most_popular_movies),
					onItemClick = { clickedMovieItem.value = it },
					seeAll = {}
				)

				Spacer(Modifier.height(50.dp))
			}

			LaunchedEffect(pagerState) {
				snapshotFlow { pagerState.currentPage }.collect { page ->
					horizontalPaddingValue = if (page == 0) PADDING_WIDTH else 40.dp
				}
			}
		}
	}

	if (clickedMovieItem.value != null) {
		MovieDetailsSheet(
			movie = clickedMovieItem.value!!,
			viewMore = {},
			closeSheet = { clickedMovieItem.value = null }
		)
	}
}

@Preview
@Composable
fun HomeScreenPreview() {
	FlickTheme {
		val movieList = MovieList(results = listOf(Movie(), Movie(), Movie()))
		val movies = State.Success(MoviesModel(trending = movieList))
		val furtherMovies = State.Success(FurtherMoviesModel(anime = movieList))
		val uiState = HomeUiState(movies, furtherMovies)
		HomeScreen(uiState) {}
	}
}