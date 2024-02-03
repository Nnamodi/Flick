package com.roland.android.flick.ui.screens.details

import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.Season
import com.roland.android.domain.entity.Series
import com.roland.android.flick.R
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.SampleData.movieDetails
import com.roland.android.flick.models.SampleData.recommendedMovies
import com.roland.android.flick.models.SampleData.similarMovies
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.state.MovieDetailsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.DotSeparator
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.MovieDetailsPoster
import com.roland.android.flick.ui.components.MovieDetailsTopBar
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.RatingBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonDetailsScreen
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.bounceClickable
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MovieDetailsScreen(
	uiState: MovieDetailsUiState,
	isMovie: Boolean,
	request: (DetailsRequest) -> Unit,
	navigate: (Screens) -> Unit
) {
	val scrollState = rememberScrollState()

	Scaffold(
		topBar = { MovieDetailsTopBar(navigate) }
	) { paddingValues ->
		CommonDetailsScreen(
			uiState.movieDetails, uiState.tvShowDetails,
			uiState.seasonDetails, uiState.castDetails,
			loadingScreen = {},
			castDetailsSheet = {}
		) { movie, show, seasonDetails ->
			Column(
				modifier = Modifier.padding(
					bottom = paddingValues.calculateBottomPadding()
				)
			) {
				val screenHeight = LocalConfiguration.current.screenWidthDp.dp
				val backdropPath = if (isMovie) movie?.details?.backdropPath else show?.details?.backdropPath

				MovieDetailsPoster(
					backdropPath = backdropPath ?: "",
					modifier = Modifier.height(screenHeight * 0.6f)
				)
				when {
					isMovie && movie != null -> {
						MovieDetailsScreen(
							movie = movie.details,
							casts = movie.details.credits.cast,
							recommendedMovies = movie.recommendedMovies,
							similarMovies = movie.similarMovies,
							onCastClick = {},
							onMovieClick = {},
							scrollState = scrollState
						)
					}
					!isMovie && show != null && seasonDetails != null -> {
						ShowDetails(
							series = show.details,
							casts = show.details.credits.cast,
							recommendedMovies = show.recommendedShows,
							similarMovies = show.similarShows,
							seasonDetails = seasonDetails.season
						)
					}
				}
			}
		}
	}
}

@Composable
private fun MovieDetailsScreen(
	movie: MovieDetails,
	casts: List<Cast>,
	recommendedMovies: MutableStateFlow<PagingData<Movie>>,
	similarMovies: MutableStateFlow<PagingData<Movie>>,
	onCastClick: (Int) -> Unit,
	onMovieClick: (Movie) -> Unit,
	scrollState: ScrollState
) {
	var expandOverview by remember { mutableStateOf(false) }
	var selectedCategory by remember { mutableIntStateOf(1) }

	Column(Modifier.verticalScroll(scrollState)) {
		Text(
			text = movie.title ?: "",
			modifier = Modifier.padding(PADDING_WIDTH, 10.dp),
			fontSize = 22.sp,
			fontWeight = FontWeight.Bold
		)
		Row(
			modifier = Modifier.padding(horizontal = PADDING_WIDTH),
			verticalAlignment = Alignment.CenterVertically
		) {
			movie.releaseDate?.let { date ->
				Text(
					text = date.take(4),
					modifier = Modifier.alpha(0.8f),
					fontSize = 12.sp,
					fontStyle = FontStyle.Italic,
					fontWeight = FontWeight.Light
				)
				DotSeparator()
			}
			// for series details only
//			movie.tvName?.let {
//				Text(
//					text = stringResource(R.string.number_of_seasons, 2),
//					modifier = Modifier.alpha(0.8f),
//					fontSize = 12.sp,
//					fontStyle = FontStyle.Italic,
//					fontWeight = FontWeight.Light
//				)
//			}
			RatingBar(
				posterType = PosterType.BackdropPoster,
				voteAverage = movie.voteAverage,
				fillMaxWidth = false
			)
		}
		Text(
			text = movie.genres.joinToString(", ") { it.name },
			modifier = Modifier
				.padding(horizontal = PADDING_WIDTH)
				.horizontalScroll(rememberScrollState()),
			color = MaterialTheme.colorScheme.surfaceTint,
			fontSize = 14.sp,
			softWrap = false
		)
		Text(
			text = movie.overview,
			modifier = Modifier
				.padding(14.dp)
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null
				) { expandOverview = !expandOverview },
			overflow = TextOverflow.Ellipsis,
			maxLines = if (expandOverview) Int.MAX_VALUE else 4
		)
		ActionButtonsRow()
		MovieCastList(
			castList = casts,
			onCastClick = onCastClick
		)
		HorizontalPosters(
			pagingData = if (selectedCategory == 1) recommendedMovies else similarMovies,
			header = stringResource(R.string.recommended),
			header2 = stringResource(R.string.similar),
			selectedHeader = selectedCategory,
			onHeaderClick = { selectedCategory = it },
			onItemClick = onMovieClick,
			seeMore = {}
		)
		Spacer(Modifier.height(50.dp))
	}
}

@Composable
private fun ShowDetails(
	series: Series,
	casts: List<Cast>,
	recommendedMovies: MutableStateFlow<PagingData<Movie>>,
	similarMovies: MutableStateFlow<PagingData<Movie>>,
	seasonDetails: Season
) {}

@Composable
private fun ActionButtonsRow() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp),
		horizontalArrangement = Arrangement.SpaceAround
	) {
		ActionButtons.values().forEach { button ->
			val buttonName = stringResource(button.buttonName)

			Column(
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.bounceClickable { button.action },
				verticalArrangement = Arrangement.spacedBy(4.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Icon(imageVector = button.icon, contentDescription = buttonName)
				Text(buttonName)
			}
		}
	}
}

private enum class ActionButtons(
	@StringRes val buttonName: Int,
	val icon: ImageVector,
	val action: () -> Unit
) {
	AddToList(
		buttonName = R.string.add,
		icon = Icons.Rounded.Add,
		action = {}
	),
	Favorite(
		buttonName = R.string.favorite,
		icon = Icons.Rounded.FavoriteBorder,
		action = {}
	),
	Share(
		buttonName = R.string.share,
		icon = Icons.Rounded.Share,
		action = {}
	)
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
	FlickTheme(true) {
		val movieDetails = State.Success(
			MovieDetailsModel(
				movieDetails,
				recommendedMovies,
				similarMovies
			)
		)
		val tvShowDetails = State.Success(TvShowDetailsModel())
		val seasonDetails = State.Success(SeasonDetailsModel())
		val castDetails = State.Success(CastDetailsModel())

		MovieDetailsScreen(
			uiState = MovieDetailsUiState(movieDetails, tvShowDetails, seasonDetails, castDetails),
			isMovie = true,
			request = {}
		) {}
	}
}
