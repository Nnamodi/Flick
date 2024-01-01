package com.roland.android.flick.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieList
import com.roland.android.flick.R
import com.roland.android.flick.models.MoviesModel
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.HomeTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.LargeItemPoster
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	moviesState: State<MoviesModel>?
) {
	Scaffold(
		topBar = { HomeTopBar() }
	) { paddingValues ->
		CommonScreen(state = moviesState) { data ->
			val pagerState = rememberPagerState()

			Column(
				modifier = Modifier
					.padding(paddingValues)
					.verticalScroll(rememberScrollState())
			) {
				Text(
					text = stringResource(R.string.trending_movies),
					modifier = Modifier.padding(start = PADDING_WIDTH, bottom = 24.dp)
				)
				HorizontalPager(
					pageCount = 10,
					state = pagerState,
					contentPadding = PaddingValues(horizontal = PADDING_WIDTH),
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 16.dp),
					pageSpacing = 14.dp,
					flingBehavior = PagerDefaults.flingBehavior(
						pagerState,
						PagerSnapDistance.atMost(3)
					),
					pageSize = PageSize.Fixed(POSTER_WIDTH_LARGE)
				) { page ->
					val trendingMovies = data.trendingMovies.results.take(10)

					LargeItemPoster(movie = trendingMovies[page], onClick = {})
				}

				HorizontalPosters(
					movieList = data.popularMovies,
					header = stringResource(R.string.most_popular_movies),
					seeAll = {}
				)

				HorizontalPosters(
					movieList = data.topRated,
					header = stringResource(R.string.top_rated_movies),
					seeAll = {}
				)
			}
		}
	}
}

@Preview
@Composable
fun HomeScreenPreview() {
	FlickTheme {
		val movies = MovieList(results = listOf(Movie(), Movie(), Movie()))
		val moviesState = State.Success(MoviesModel(trendingMovies = movies))
		HomeScreen(moviesState = moviesState)
	}
}