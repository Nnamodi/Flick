package com.roland.android.flick.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.auth_response.AccountDetails
import com.roland.android.domain.usecase.Category
import com.roland.android.domain.usecase.Category.FAVORITED_MOVIES
import com.roland.android.domain.usecase.Category.FAVORITED_SERIES
import com.roland.android.domain.usecase.Category.RATED_MOVIES
import com.roland.android.domain.usecase.Category.RATED_SERIES
import com.roland.android.domain.usecase.Category.WATCHLISTED_MOVIES
import com.roland.android.domain.usecase.Category.WATCHLISTED_SERIES
import com.roland.android.flick.R
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.AccountTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.components.SnackbarDuration
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScaffold
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.RowItems
import com.roland.android.flick.utils.WindowType.Portrait
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun AccountScreen(
	uiState: AccountUiState,
	action: (AccountActions?) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (accountDetails, favoritedMedia, watchlistedMedia, ratedMedia, response) = uiState
	val loadingErrorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val actionResponseMessage = remember { mutableStateOf<String?>(null) }
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
	val windowSize = rememberWindowSize()
	val seeMore: (Category) -> Unit = {
		navigate(Screens.MovieListScreen(it.name))
	}
	val onMovieClick: (Movie) -> Unit = {
		clickedMovieItem.value = it
	}
	val onCancelResult: (String?) -> Unit = {
		actionResponseMessage.value = it
		action(null) // nullify response so it doesn't trigger snackbar again due to recomposition
	}
	val onLoadError: (String?) -> Unit = {
		loadingErrorMessage.value = it
	}

	CommonScaffold(
		topBar = { AccountTopBar(navigate) }
	) { paddingValues ->
		CommonScreen(
			favoritedMedia, watchlistedMedia, ratedMedia,
			paddingValues, loadingScreen = { error ->
				AccountLoadingUi(
					name = accountDetails.name ?: accountDetails.username,
					username = accountDetails.username,
					paddingValues = paddingValues,
					isLoading = error == null
				)
				onLoadError(error)
			}
		) { favorited, watchlisted, rated ->
			LazyColumn(
				modifier = Modifier.padding(paddingValues),
				contentPadding = PaddingValues(bottom = 50.dp + (if (windowSize.width == Portrait) NavigationBarHeight else 0.dp))
			) {
				item { UserInfo(accountDetails) }

				item {
					HorizontalPosters(
						moviesData = favorited.movies,
						showsData = favorited.shows,
						header = stringResource(R.string.favorites),
						response = response,
						onMovieClick = onMovieClick,
						onCancel = { mediaId, mediaType ->
							action(AccountActions.UnFavoriteMedia(mediaId, mediaType))
						},
						onCancelled = onCancelResult,
						onLoadError = onLoadError
					) { seeMore(if (it == MOVIES) FAVORITED_MOVIES else FAVORITED_SERIES) }
				}

				item {
					HorizontalPosters(
						moviesData = watchlisted.movies,
						showsData = watchlisted.shows,
						header = stringResource(R.string.watchlist),
						response = response,
						onMovieClick = onMovieClick,
						onCancel = { mediaId, mediaType ->
							action(AccountActions.RemoveFromWatchlist(mediaId, mediaType))
						},
						onCancelled = onCancelResult,
						onLoadError = onLoadError
					) { seeMore(if (it == MOVIES) WATCHLISTED_MOVIES else WATCHLISTED_SERIES) }
				}

				item {
					HorizontalPosters(
						moviesData = rated.movies,
						showsData = rated.shows,
						header = stringResource(R.string.rated),
						response = response,
						showUserRating = true,
						onMovieClick = onMovieClick,
						onCancel = { mediaId, mediaType ->
							action(AccountActions.DeleteMediaRating(mediaId, mediaType))
						},
						onCancelled = onCancelResult,
						onLoadError = onLoadError
					) { seeMore(if (it == MOVIES) RATED_MOVIES else RATED_SERIES) }
				}

			}

			if (clickedMovieItem.value != null) {
				val itemIsMovie = clickedMovieItem.value!!.title != null

				MovieDetailsSheet(
					movie = clickedMovieItem.value!!,
					genreList = if (itemIsMovie) watchlisted.movieGenres else favorited.showGenres,
					viewMore = navigate,
					closeSheet = { clickedMovieItem.value = null }
				)
			}
		}

		val snackbarPadding = if (windowSize.width == Portrait) NavigationBarHeight else 0.dp
		if (actionResponseMessage.value != null) {
			Snackbar(
				message = actionResponseMessage.value!!,
				modifier = Modifier.padding(bottom = snackbarPadding),
				paddingValues = paddingValues,
				onDismiss = { actionResponseMessage.value = null }
			)
		}
		if (loadingErrorMessage.value != null) {
			Snackbar(
				message = loadingErrorMessage.value!!,
				modifier = Modifier.padding(bottom = snackbarPadding),
				paddingValues = paddingValues,
				actionLabel = stringResource(R.string.retry),
				action = { action(AccountActions.ReloadMedia) },
				duration = SnackbarDuration.Indefinite
			)
		}

		LaunchedEffect(Unit) {
			if (uiState.watchlistedMedia is State.Error) return@LaunchedEffect
			loadingErrorMessage.value = null
		}
	}

	DisposableEffect(Unit) {
		// prevents snackbar from popping again when navigating back to this screen
		onDispose { action(null) }
	}
}

@Composable
private fun UserInfo(accountDetails: AccountDetails) {
	Row(
		modifier = Modifier.padding(20.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			imageVector = Icons.Rounded.AccountCircle,
			contentDescription = stringResource(R.string.profile_image),
			modifier = Modifier.size(60.dp),
			tint = Color.Blue
		)
		Column(Modifier.padding(start = 20.dp)) {
			Text(
				text = accountDetails.name ?: accountDetails.username,
				style = MaterialTheme.typography.headlineSmall
			)
			accountDetails.name?.let {
				Text(
					text = accountDetails.username,
					modifier = Modifier.alpha(0.5f),
					style = MaterialTheme.typography.bodyLarge
				)
			}
		}
	}
}

@Composable
private fun AccountLoadingUi(
	name: String,
	username: String,
	paddingValues: PaddingValues,
	isLoading: Boolean
) {
	Column(
		Modifier
			.padding(paddingValues)
			.verticalScroll(rememberScrollState())
	) {
		val windowSize = rememberWindowSize()

		UserInfo(AccountDetails(name = name, username = username))

		RowItems(stringResource(R.string.favorites), isLoading)

		RowItems(stringResource(R.string.watchlist), isLoading)

		RowItems(stringResource(R.string.rated), isLoading)

		Spacer(Modifier.padding(bottom = 50.dp + (if (windowSize.width == Portrait) NavigationBarHeight else 0.dp)))

	}
}