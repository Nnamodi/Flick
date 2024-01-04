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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.HomeScreenActions

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	uiState: HomeUiState,
	action: (HomeScreenActions) -> Unit
) {
	val (movies, furtherMovies, _, selectedCategory) = uiState

	Scaffold(
		topBar = { HomeTopBar() }
	) { paddingValues ->
		CommonScreen(movies, furtherMovies) { data1, data2 ->
			val pagerState = rememberPagerState()
			var horizontalPaddingValue by remember { mutableStateOf(PADDING_WIDTH) }

			Column(
				modifier = Modifier
					.padding(paddingValues)
					.verticalScroll(rememberScrollState())
			) {
				ToggleButton(
					selectedOption = selectedCategory,
					onClick = action
				)
				Text(
					text = stringResource(R.string.trending_movies),
					modifier = Modifier.padding(start = PADDING_WIDTH, bottom = 20.dp),
					fontWeight = FontWeight.Bold
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
					val trendingMovies = data1.trendingMovies.results.take(20)

					LargeItemPoster(movie = trendingMovies[page], onClick = {})
				}

				HorizontalPosters(
					movieList = data1.popularMovies,
					header = stringResource(R.string.most_popular_movies),
					seeAll = {}
				)

				HorizontalPosters(
					movieList = data1.topRated,
					header = stringResource(R.string.top_rated_movies),
					seeAll = {}
				)

				HorizontalPosters(
					movieList = data2.animeCollection,
					header = stringResource(R.string.anime_collection),
					seeAll = {}
				)

				HorizontalPosters(
					movieList = data2.bollywoodMovies,
					header = stringResource(R.string.bollywood_movies),
					seeAll = {}
				)

				HorizontalPosters(
					movieList = data1.nowPlayingMovies,
					header = stringResource(R.string.in_theatres),
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
}

@Preview
@Composable
fun HomeScreenPreview() {
	FlickTheme {
		val movieList = MovieList(results = listOf(Movie(), Movie(), Movie()))
		val movies = State.Success(MoviesModel(trendingMovies = movieList))
		val furtherMovies = State.Success(FurtherMoviesModel(animeCollection = movieList))
		val uiState = HomeUiState(movies, furtherMovies)
		HomeScreen(uiState) {}
	}
}