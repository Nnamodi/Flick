package com.roland.android.flick.ui.screens.details

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.Video
import com.roland.android.flick.R
import com.roland.android.flick.models.CastDetailsModel
import com.roland.android.flick.models.MovieDetailsModel
import com.roland.android.flick.models.SampleData.movieDetails
import com.roland.android.flick.models.SampleData.recommendedMovies
import com.roland.android.flick.models.SampleData.recommendedShows
import com.roland.android.flick.models.SampleData.seasonDetails
import com.roland.android.flick.models.SampleData.showDetails
import com.roland.android.flick.models.SampleData.similarMovies
import com.roland.android.flick.models.SampleData.similarShows
import com.roland.android.flick.models.SeasonDetailsModel
import com.roland.android.flick.models.TvShowDetailsModel
import com.roland.android.flick.state.MovieDetailsUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.DotSeparator
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.RatingBar
import com.roland.android.flick.ui.components.VideoList
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.screens.details.loading.MovieDetailsLoadingUi
import com.roland.android.flick.ui.screens.details.sheets.CastDetailsSheet
import com.roland.android.flick.ui.screens.details.sheets.SeasonSelectionSheet
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.YEAR
import com.roland.android.flick.utils.Extensions.dateFormat
import com.roland.android.flick.utils.Extensions.getTrailer
import com.roland.android.flick.utils.Extensions.getTrailerKey
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.bounceClickable
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsScreen(
	uiState: MovieDetailsUiState,
	isMovie: Boolean,
	request: (DetailsRequest) -> Unit,
	navigate: (Screens) -> Unit
) {
	val scrollState = rememberScrollState()
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }

	Scaffold(
		snackbarHost = {
			SnackbarHost(snackbarHostState) { data ->
				errorMessage.value?.let {
					Snackbar(
						modifier = Modifier.padding(16.dp),
						action = {
							data.visuals.actionLabel?.let { label ->
								TextButton(
									onClick = { request(DetailsRequest.Retry) }
								) { Text(label) }
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
			state = if (isMovie) uiState.movieDetails else uiState.tvShowDetails,
			paddingValues, loadingScreen = { error ->
				MovieDetailsLoadingUi(scrollState, isLoading = error == null, navigate)
				errorMessage.value = error
				error?.let {
					val actionLabel = stringResource(R.string.retry)
					scope.launch {
						snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
					}
				}
			}
		) { details ->
			val screenHeight = LocalConfiguration.current.screenWidthDp.dp
			val windowSize = rememberWindowSize()
			val inPortraitMode by remember(windowSize.width) {
				derivedStateOf { windowSize.width == WindowType.Portrait }
			}
			val screenModifier = if (!inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier
			val columnModifier = if (inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier
			val videoHeightDivisor = if (inPortraitMode) 0.6f else 0.45f

			if (isMovie) {
				Column(
					modifier = screenModifier.padding(
						bottom = paddingValues.calculateBottomPadding()
					)
				) {
					val movie = details as MovieDetailsModel

					VideoPlayer(
						trailerKey = movie.details.videos.getTrailerKey(),
						modifier = Modifier.height(screenHeight * videoHeightDivisor),
						navigateUp = navigate
					)
					MovieDetails(
						movie = movie.details,
						casts = movie.details.credits.cast,
						castDetails = uiState.castDetails,
						recommendedMovies = movie.recommendedMovies,
						similarMovies = movie.similarMovies,
						genres = arrayOf(movie.movieGenres, movie.seriesGenres),
						videos = movie.details.videos,
						modifier = columnModifier,
						castDetailsRequest = request,
						navigate = navigate
					)
				}
			} else {
				Box(
					modifier = Modifier.padding(
						bottom = paddingValues.calculateBottomPadding()
					)
				) {
					val show = details as TvShowDetailsModel
					val openSeasonSelectionSheet = rememberSaveable { mutableStateOf(false) }

					Column(screenModifier) {
						VideoPlayer(
							trailerKey = show.details.videos.getTrailerKey(),
							modifier = Modifier.height(screenHeight * videoHeightDivisor),
							enabled = !openSeasonSelectionSheet.value,
							navigateUp = navigate
						)
						MovieDetails(
							series = show.details,
							casts = show.details.credits.cast,
							seasonDetails = uiState.seasonDetails,
							castDetails = uiState.castDetails,
							recommendedMovies = show.recommendedShows,
							similarMovies = show.similarShows,
							genres = arrayOf(show.movieGenres, show.seriesGenres),
							selectedSeasonNumber = uiState.selectedSeasonNumber,
							videos = show.details.videos,
							modifier = columnModifier,
							openSeasonSelectionSheet = { openSeasonSelectionSheet.value = true },
							castDetailsRequest = request,
							navigate = navigate
						)
					}

					SeasonSelectionSheet(
						showSheet = openSeasonSelectionSheet.value,
						seriesId = show.details.id,
						selectedSeasonNumber = uiState.selectedSeasonNumber,
						numberOfSeasons = show.details.numberOfSeasons,
						onSeasonSelected = request,
						closeSheet = { openSeasonSelectionSheet.value = false }
					)
				}
			}
		}
	}
}

@Composable
private fun MovieDetails(
	movie: MovieDetails? = null,
	series: Series? = null,
	casts: List<Cast>,
	seasonDetails: State<SeasonDetailsModel>? = null,
	castDetails: State<CastDetailsModel>? = null,
	recommendedMovies: MutableStateFlow<PagingData<Movie>>,
	similarMovies: MutableStateFlow<PagingData<Movie>>,
	genres: Array<List<Genre>>,
	selectedSeasonNumber: Int? = null,
	videos: List<Video>,
	modifier: Modifier,
	openSeasonSelectionSheet: () -> Unit = {},
	castDetailsRequest: (DetailsRequest) -> Unit,
	navigate: (Screens) -> Unit
) {
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val openCastDetailsSheet = remember { mutableStateOf(false) }
	val moreVideos = videos.filterNot { it == videos.getTrailer() }

	Column(modifier) {
		Details(movie, series)
		ActionButtonsRow()
		MovieCastList(
			castList = casts,
			onCastClick = {
				val request = DetailsRequest.GetCastDetails(it)
				castDetailsRequest(request)
				openCastDetailsSheet.value = true
			}
		)
		series?.let {
			SeasonDetails(
				seasonUiState = seasonDetails,
				numberOfSeasons = it.numberOfSeasons,
				selectedSeasonNumber = selectedSeasonNumber!!,
				openSeasonSelectionSheet = openSeasonSelectionSheet
			)
		}
		HorizontalPosters(
			pagingData = recommendedMovies,
			pagingData2 = similarMovies,
			header = stringResource(R.string.recommended),
			header2 = stringResource(R.string.similar),
			onMovieClick = { clickedMovieItem.value = it }
		)
		VideoList(moreVideos)
		Spacer(Modifier.height(50.dp))
	}

	if (clickedMovieItem.value != null) {
		val itemIsMovie = clickedMovieItem.value!!.title != null

		MovieDetailsSheet(
			movie = clickedMovieItem.value!!,
			genreList = if (itemIsMovie) genres[0] else genres[1],
			viewMore = { navigate(it); openCastDetailsSheet.value = false },
			closeSheet = { clickedMovieItem.value = null }
		)
	}

	if (openCastDetailsSheet.value) {
		CastDetailsSheet(
			uiState = castDetails,
			onMovieClick = { clickedMovieItem.value = it },
			closeSheet = { openCastDetailsSheet.value = false }
		)
	}
}

@Composable
private fun Details(
	movie: MovieDetails?,
	series: Series?
) {
	var expandOverview by remember { mutableStateOf(false) }

	Text(
		text = movie?.title ?: series?.name ?: "",
		modifier = Modifier.padding(PADDING_WIDTH, 10.dp),
		fontSize = 22.sp,
		fontWeight = FontWeight.Bold
	)
	Row(
		modifier = Modifier.padding(horizontal = PADDING_WIDTH),
		verticalAlignment = Alignment.CenterVertically
	) {
		val releaseDate = (movie?.releaseDate ?: series?.firstAirDate)?.dateFormat(YEAR)
		val lastAirDate = series?.lastAirDate?.dateFormat(YEAR)

		releaseDate?.let {
			Text(
				text = releaseDate,
				modifier = Modifier.alpha(0.8f),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.Light
			)
		}
		lastAirDate?.let {
			if (!series.inProduction && (lastAirDate != releaseDate)) {
				Text(
					text = "-$lastAirDate",
					modifier = Modifier.alpha(0.8f),
					fontSize = 12.sp,
					fontStyle = FontStyle.Italic,
					fontWeight = FontWeight.Light
				)
			}
		}
		if (series?.inProduction == true) {
			Text(
				text = stringResource(R.string.in_production),
				modifier = Modifier
					.padding(start = 2.dp)
					.alpha(0.8f),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.Light
			)
		}
		DotSeparator()
		series?.let {
			Text(
				text = pluralStringResource(R.plurals.number_of_seasons, series.numberOfSeasons, series.numberOfSeasons),
				modifier = Modifier.alpha(0.8f),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.Light
			)
			DotSeparator()
		}
		RatingBar(
			posterType = PosterType.BackdropPoster,
			voteAverage = movie?.voteAverage ?: series?.voteAverage ?: 0.0,
			fillMaxWidth = false
		)
	}
	if ((movie?.genres ?: series?.genres)?.isEmpty() == false) {
		Text(
			text = (movie?.genres ?: series?.genres)
				?.joinToString(", ") { it.name } ?: "",
			modifier = Modifier
				.padding(horizontal = PADDING_WIDTH)
				.horizontalScroll(rememberScrollState()),
			color = MaterialTheme.colorScheme.surfaceTint,
			fontSize = 14.sp,
			softWrap = false
		)
	}
	Text(
		text = movie?.overview ?: series?.overview ?: "",
		modifier = Modifier
			.padding(14.dp)
			.animateContentSize()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null
			) { expandOverview = !expandOverview },
		overflow = TextOverflow.Ellipsis,
		maxLines = if (expandOverview) Int.MAX_VALUE else 4
	)
}

@Composable
private fun ActionButtonsRow() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp),
		horizontalArrangement = Arrangement.SpaceAround
	) {
		val context = LocalContext.current

		ActionButtons.values().forEach { button ->
			val buttonName = stringResource(button.buttonName)

			Column(
				modifier = Modifier
					.padding(horizontal = 10.dp)
					.bounceClickable {
						Toast
							.makeText(
								context,
								context.getString(R.string.coming_soon),
								Toast.LENGTH_SHORT
							)
							.show()
						button.action
					},
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
private fun MovieDetailsScreenPreview() {
	FlickTheme(true) {
		val movieDetails = State.Success(
			MovieDetailsModel(
				movieDetails,
				recommendedMovies,
				similarMovies
			)
		)
		val tvShowDetails = State.Success(
			TvShowDetailsModel(
				showDetails,
				recommendedShows,
				similarShows
			)
		)
		val seasonDetails = State.Success(
			SeasonDetailsModel(seasonDetails)
		)

		MovieDetailsScreen(
			uiState = MovieDetailsUiState(movieDetails, tvShowDetails, seasonDetails),
			isMovie = false,
			request = {}
		) {}
	}
}
