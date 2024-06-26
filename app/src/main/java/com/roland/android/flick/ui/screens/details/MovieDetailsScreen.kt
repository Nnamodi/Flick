package com.roland.android.flick.ui.screens.details

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import com.roland.android.domain.entity.Cast
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.MovieDetails
import com.roland.android.domain.entity.Series
import com.roland.android.domain.entity.Video
import com.roland.android.domain.entity.auth_response.Response
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
import com.roland.android.flick.ui.components.OpenWithButton
import com.roland.android.flick.ui.components.PosterType
import com.roland.android.flick.ui.components.RatingBar
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.SnackbarDuration
import com.roland.android.flick.ui.components.VideoList
import com.roland.android.flick.ui.components.showToast
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.screens.details.Buttons.Rate
import com.roland.android.flick.ui.screens.details.Buttons.Watchlist
import com.roland.android.flick.ui.screens.details.loading.MovieDetailsLoadingUi
import com.roland.android.flick.ui.screens.details.sheets.CastDetailsSheet
import com.roland.android.flick.ui.screens.details.sheets.RateMediaDialog
import com.roland.android.flick.ui.screens.details.sheets.SeasonSelectionSheet
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.IMDB_BASE_URL
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.SERIES
import com.roland.android.flick.utils.Constants.YEAR
import com.roland.android.flick.utils.Extensions.dateFormat
import com.roland.android.flick.utils.Extensions.getTrailer
import com.roland.android.flick.utils.Extensions.getTrailerKey
import com.roland.android.flick.utils.Extensions.isReleased
import com.roland.android.flick.utils.Extensions.refine
import com.roland.android.flick.utils.WindowType.Portrait
import com.roland.android.flick.utils.bounceClickable
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MovieDetailsScreen(
	uiState: MovieDetailsUiState,
	isMovie: Boolean,
	request: (DetailsRequest) -> Unit,
	action: (MovieDetailsActions?) -> Unit,
	navigate: (Screens) -> Unit
) {
	val scrollState = rememberScrollState()
	val snackbarMessage = remember { mutableStateOf<String?>(null) }
	val requestToLogin = remember { mutableStateOf(false) }

	CommonScaffold { paddingValues ->
		CommonScreen(
			state = if (isMovie) uiState.movieDetails else uiState.tvShowDetails,
			paddingValues, loadingScreen = { error ->
				Box(contentAlignment = Alignment.BottomStart) {
					MovieDetailsLoadingUi(scrollState, isLoading = error == null, navigate)
					error?.let {
						Snackbar(
							message = it,
							paddingValues = paddingValues,
							actionLabel = stringResource(R.string.retry),
							action = { request(DetailsRequest.Retry) },
							duration = SnackbarDuration.Indefinite
						)
					}
				}
			}
		) { details ->
			val screenHeight = LocalConfiguration.current.screenWidthDp.dp
			val windowSize = rememberWindowSize()
			val inPortraitMode by remember(windowSize.width) {
				derivedStateOf { windowSize.width == Portrait }
			}
			val videoHeightDivisor = if (inPortraitMode) 0.6f else 0.45f

			if (isMovie) {
				MoviesDetails(
					movie = details as MovieDetailsModel,
					uiState = uiState,
					inPortraitMode = inPortraitMode,
					scrollState = scrollState,
					paddingValues = paddingValues,
					screenHeight = screenHeight,
					videoHeightDivisor = videoHeightDivisor,
					logInRequest = { requestToLogin.value = true },
					request = request,
					action = action,
					navigate = navigate
				)
			} else {
				ShowDetails(
					show = details as TvShowDetailsModel,
					uiState = uiState,
					inPortraitMode = inPortraitMode,
					scrollState = scrollState,
					paddingValues = paddingValues,
					screenHeight = screenHeight,
					videoHeightDivisor = videoHeightDivisor,
					logInRequest = { requestToLogin.value = true },
					request = request,
					action = action,
					navigate = navigate
				)
			}

			// pops up custom snackbar to show response message returned from watchlist, favorite or rate actions.
			LaunchedEffect(uiState.response) {
				snackbarMessage.value = when (uiState.response) {
					null -> null
					is State.Error -> uiState.response.errorMessage.refine()
					is State.Success -> uiState.response.data.statusMessage
				}
			}
		}
		if (snackbarMessage.value != null) {
			Snackbar(
				message = snackbarMessage.value!!,
				paddingValues = paddingValues,
				onDismiss = { action(null) }
			)
		}
		if (requestToLogin.value) {
			Snackbar(
				message = stringResource(R.string.sign_up_message),
				paddingValues = paddingValues,
				actionLabel = stringResource(R.string.sign_up),
				action = { navigate(Screens.AccountScreen) },
				onDismiss = { requestToLogin.value = false }
			)
		}
	}
}

@Composable
private fun MoviesDetails(
	movie: MovieDetailsModel,
	uiState: MovieDetailsUiState,
	inPortraitMode: Boolean,
	scrollState: ScrollState,
	paddingValues: PaddingValues,
	screenHeight: Dp,
	videoHeightDivisor: Float,
	logInRequest: () -> Unit,
	request: (DetailsRequest) -> Unit,
	action: (MovieDetailsActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val screenModifier = if (!inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier
	val columnModifier = if (inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier

	Column(
		modifier = screenModifier.padding(
			bottom = paddingValues.calculateBottomPadding()
		)
	) {
		VideoPlayer(
			trailerKey = movie.details.videos.getTrailerKey(),
			thumbnail = movie.details.backdropPath,
			modifier = Modifier.height(screenHeight * videoHeightDivisor),
			autoPlay = uiState.autoStreamTrailer,
			navigateUp = navigate
		)
		Details(
			movie = movie.details,
			casts = movie.details.credits.cast,
			castDetails = uiState.castDetails,
			recommendedMovies = movie.recommendedMovies,
			similarMovies = movie.similarMovies,
			genres = arrayOf(movie.movieGenres, movie.seriesGenres),
			videos = movie.details.videos,
			modifier = columnModifier,
			userIsLoggedIn = uiState.userIsLoggedIn,
			actionHandled = uiState.response != null,
			response = uiState.response,
			logInRequest = logInRequest,
			castDetailsRequest = request,
			detailsAction = action,
			navigate = navigate
		)
	}
}

@Composable
private fun ShowDetails(
	uiState: MovieDetailsUiState,
	show: TvShowDetailsModel,
	inPortraitMode: Boolean,
	scrollState: ScrollState,
	paddingValues: PaddingValues,
	screenHeight: Dp,
	videoHeightDivisor: Float,
	logInRequest: () -> Unit,
	request: (DetailsRequest) -> Unit,
	action: (MovieDetailsActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val screenModifier = if (!inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier
	val columnModifier = if (inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier

	Box(
		modifier = Modifier.padding(
			bottom = paddingValues.calculateBottomPadding()
		)
	) {
		val openSeasonSelectionSheet = rememberSaveable { mutableStateOf(false) }

		Column(screenModifier) {
			VideoPlayer(
				trailerKey = show.details.videos.getTrailerKey(),
				thumbnail = show.details.backdropPath,
				modifier = Modifier.height(screenHeight * videoHeightDivisor),
				autoPlay = uiState.autoStreamTrailer,
				enabled = !openSeasonSelectionSheet.value,
				navigateUp = navigate
			)
			Details(
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
				userIsLoggedIn = uiState.userIsLoggedIn,
				actionHandled = uiState.response != null,
				response = uiState.response,
				logInRequest = logInRequest,
				openSeasonSelectionSheet = { openSeasonSelectionSheet.value = true },
				castDetailsRequest = request,
				detailsAction = action,
				navigate = navigate
			)
		}

		SeasonSelectionSheet(
			showSheet = openSeasonSelectionSheet.value,
			seriesId = show.details.id,
			selectedSeasonNumber = uiState.selectedSeasonNumber,
			numberOfSeasons = show.details.seasons.size,
			specialSeasonAvailable = show.details.seasons.size > show.details.numberOfSeasons,
			onSeasonSelected = request,
			closeSheet = { openSeasonSelectionSheet.value = false }
		)
	}
}

@Composable
private fun Details(
	movie: MovieDetails? = null,
	series: Series? = null,
	casts: List<Cast>,
	seasonDetails: State<SeasonDetailsModel>? = null,
	castDetails: State<CastDetailsModel>? = null,
	response: State<Response>?,
	recommendedMovies: MutableStateFlow<PagingData<Movie>>,
	similarMovies: MutableStateFlow<PagingData<Movie>>,
	genres: Array<List<Genre>>,
	selectedSeasonNumber: Int? = null,
	videos: List<Video>,
	modifier: Modifier,
	userIsLoggedIn: Boolean,
	actionHandled: Boolean,
	logInRequest: () -> Unit,
	openSeasonSelectionSheet: () -> Unit = {},
	castDetailsRequest: (DetailsRequest) -> Unit,
	detailsAction: (MovieDetailsActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val openCastDetailsSheet = remember { mutableStateOf(false) }
	val openRateMediaDialog = remember { mutableStateOf(false) }
	val moreVideos = videos.filterNot { it == videos.getTrailer() }

	Column(modifier) {
		Info(movie, series)
		ActionButtonsRow(
			mediaId = movie?.id ?: series?.id ?: 0,
			mediaType = if (movie == null) SERIES else MOVIES,
			imdbId = movie?.imdbId ?: series?.externalIds?.imdbId,
			trailerKey = videos.getTrailerKey(),
			userIsLoggedIn = userIsLoggedIn,
			actionHandled = actionHandled,
			mediaIsReleased = (movie?.releaseDate ?: series?.firstAirDate).isReleased(),
			onClick = detailsAction,
			openRateMediaDialog = { openRateMediaDialog.value = true },
			logInRequest = logInRequest
		)
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
				numberOfSeasons = it.seasons.size,
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

	if (openRateMediaDialog.value) {
		RateMediaDialog(
			movie = movie,
			series = series,
			response = response,
			onMediaRated = detailsAction,
			closeSheet = { openRateMediaDialog.value = false }
		)
	}
}

@Composable
private fun Info(
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
		val mediaReleaseDate = movie?.releaseDate ?: series?.firstAirDate
		val releaseDate = mediaReleaseDate?.dateFormat(YEAR)
		val lastAirDate = series?.lastAirDate?.dateFormat(YEAR)

		releaseDate?.let {
			Text(
				text = if (mediaReleaseDate.isReleased()) it else stringResource(R.string.coming_soon),
				modifier = Modifier.alpha(0.8f),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.Light
			)
		}
		lastAirDate?.let {
			if (!series.inProduction && (lastAirDate != releaseDate) && mediaReleaseDate.isReleased()) {
				Text(
					text = "-$lastAirDate",
					modifier = Modifier.alpha(0.8f),
					fontSize = 12.sp,
					fontStyle = FontStyle.Italic,
					fontWeight = FontWeight.Light
				)
			}
		}
		if (series?.inProduction == true && mediaReleaseDate.isReleased()) {
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
		movie?.let {
			Text(
				text = "${it.runtime / 60}h ${it.runtime % 60}min",
				modifier = Modifier.alpha(0.8f),
				fontSize = 12.sp,
				fontStyle = FontStyle.Italic,
				fontWeight = FontWeight.Light
			)
			DotSeparator()
		}
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
			color = colorScheme.surfaceTint,
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
fun ActionButtonsRow(
	mediaId: Int,
	mediaType: String,
	imdbId: String?,
	trailerKey: String?,
	userIsLoggedIn: Boolean,
	actionHandled: Boolean,
	inDetailsScreen: Boolean = true,
	mediaIsReleased: Boolean = false,
	onClick: (MovieDetailsActions) -> Unit,
	openRateMediaDialog: () -> Unit = {},
	logInRequest: () -> Unit
) {
	val context = LocalContext.current
	val buttons = Buttons.values().toMutableSet()
	if (!inDetailsScreen || !mediaIsReleased) buttons.remove(Rate)
	val rowModifier = if (inDetailsScreen) Modifier.fillMaxWidth() else Modifier
	val windowSize = rememberWindowSize()
	val horizontalArrangement = when {
		!inDetailsScreen -> Arrangement.Start
		windowSize.width == Portrait -> Arrangement.spacedBy(20.dp)
		else -> Arrangement.SpaceAround
	}

	Row(
		modifier = rowModifier
			.padding(vertical = if (inDetailsScreen) 12.dp else 0.dp)
			.horizontalScroll(rememberScrollState()),
		horizontalArrangement = horizontalArrangement,
		verticalAlignment = Alignment.CenterVertically
	) {
		if (inDetailsScreen) Spacer(Modifier.width(8.dp))
		buttons.forEach { button ->
			ActionButton(
				nameRes = button.nameRes,
				defaultIcon = button.defaultIcon,
				modifier = Modifier.scale(if (inDetailsScreen) 1f else 0.75f),
				userIsLoggedIn = userIsLoggedIn,
				requestDone = actionHandled,
				onClick = {
					if (button == Rate) {
						openRateMediaDialog(); return@ActionButton
					}
					val action = when (button) {
						Watchlist -> MovieDetailsActions.AddToWatchlist(mediaId, mediaType)
						else -> MovieDetailsActions.FavoriteMedia(mediaId, mediaType)
					}
					onClick(action)
				},
				logInRequest = logInRequest
			)
		}
		if (inDetailsScreen) {
			OpenWithButton(imdbId, trailerKey)
			ActionButton(
				nameRes = R.string.share,
				defaultIcon = Icons.Rounded.Share,
				onClick = {
					imdbId?.let {
						onClick(MovieDetailsActions.Share(IMDB_BASE_URL + it, context))
					} ?: kotlin.run {
						showToast(context)
					}
				}
			)
			Spacer(Modifier.width(PADDING_WIDTH))
		}
	}
}

@Composable
private fun ActionButton(
	@StringRes nameRes: Int,
	defaultIcon: ImageVector,
	modifier: Modifier = Modifier,
	userIsLoggedIn: Boolean = true,
	requestDone: Boolean = false,
	onClick: () -> Unit,
	logInRequest: () -> Unit = {}
) {
	val requestLoading = rememberSaveable { mutableStateOf(false) }
	val actionHandled = rememberSaveable(requestDone) { mutableStateOf(requestDone) }

	Column(
		modifier = modifier
			.padding(horizontal = 10.dp)
			.bounceClickable(!requestLoading.value) {
				if (!userIsLoggedIn) {
					logInRequest()
					return@bounceClickable
				}
				onClick()
				if (nameRes in setOf(R.string.rate, R.string.share)) return@bounceClickable
				requestLoading.value = true
			},
		verticalArrangement = Arrangement.spacedBy(4.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (requestLoading.value) {
			CircularProgressIndicator(
				modifier = Modifier
					.padding(bottom = 4.dp)
					.size(20.dp),
				strokeWidth = 2.dp
			)
		} else {
			Icon(
				imageVector = defaultIcon,
				contentDescription = stringResource(nameRes)
			)
		}
		Text(stringResource(nameRes))
	}

	LaunchedEffect(actionHandled) {
		if (!actionHandled.value) return@LaunchedEffect
		requestLoading.value = false
		actionHandled.value = false
	}
}

private enum class Buttons(
	@StringRes val nameRes: Int,
	val defaultIcon: ImageVector,
	val doneIcon: ImageVector
) {
	Watchlist(R.string.my_list, Icons.Rounded.Add, Icons.Rounded.Done),
	Favorite(R.string.favorite, Icons.Rounded.FavoriteBorder, Icons.Rounded.Favorite),
	Rate(R.string.rate, Icons.Rounded.StarBorder, Icons.Rounded.Star)
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
			request = {},
			action = {}
		) {}
	}
}
