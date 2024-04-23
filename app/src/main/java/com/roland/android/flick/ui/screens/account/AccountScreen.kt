package com.roland.android.flick.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.R
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.ui.components.AccountTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.RowItems
import com.roland.android.flick.utils.WindowType.Portrait
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
	uiState: AccountUiState,
	action: (AccountActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()
	val snackbarMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val windowSize = rememberWindowSize()

	Scaffold(
		topBar = { AccountTopBar() },
		snackbarHost = {
			snackbarMessage.value?.let {
				SnackbarHost(
					hostState = snackbarHostState,
					modifier = Modifier
						.padding(bottom = if (windowSize.width == Portrait) NavigationBarHeight else 0.dp)
				)
			}
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.verticalScroll(rememberScrollState())
		) {
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
				Text(
					text = uiState.accountDetails.username,
					modifier = Modifier.padding(start = 20.dp),
					style = MaterialTheme.typography.headlineSmall
				)
			}
			MediaRows(
				uiState = uiState,
				paddingValues = paddingValues,
				snackbarHostState = snackbarHostState,
				action = action,
				onError = { snackbarMessage.value = it },
				onCancelResult = {
					scope.launch {
						snackbarMessage.value = it
						if (snackbarHostState.showSnackbar(it) == Dismissed) {
							snackbarMessage.value = null
						}
					}
				},
				navigate = navigate
			)
		}
	}
}

@Composable
private fun MediaRows(
	uiState: AccountUiState,
	paddingValues: PaddingValues,
	snackbarHostState: SnackbarHostState,
	action: (AccountActions) -> Unit,
	onError: (String?) -> Unit,
	onCancelResult: (String) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (_, movies, shows, response) = uiState
	val scope = rememberCoroutineScope()
	val windowSize = rememberWindowSize()
	val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }

	CommonScreen(
		movies, shows, paddingValues,
		loadingScreen = { error ->
			AccountLoadingUi(isLoading = error == null)
			onError(error)
			error?.let {
				val actionLabel = stringResource(R.string.retry)
				scope.launch {
					when (snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)) {
						ActionPerformed -> action(AccountActions.ReloadMedia)
						Dismissed -> {}
					}
				}
			}
		}
	) { movieData, showData -> onError(null)
		Column {
			HorizontalPosters(
				moviesData = movieData.favoriteList,
				showsData = showData.favoriteList,
				header = stringResource(R.string.favorites),
				response = response,
				onMovieClick = { clickedMovieItem.value = it },
				onCancel = { mediaId, mediaType ->
					action(AccountActions.UnFavoriteMedia(mediaId, mediaType))
				},
				onCancelled = onCancelResult,
				onError = onCancelResult
			)

			HorizontalPosters(
				moviesData = movieData.watchlist,
				showsData = showData.watchlist,
				header = stringResource(R.string.watchlist),
				response = response,
				onMovieClick = { clickedMovieItem.value = it },
				onCancel = { mediaId, mediaType ->
					action(AccountActions.RemoveFromWatchlist(mediaId, mediaType))
				},
				onCancelled = onCancelResult,
				onError = onCancelResult
			)

			HorizontalPosters(
				moviesData = movieData.ratedList,
				showsData = showData.ratedList,
				header = stringResource(R.string.rated),
				response = response,
				onMovieClick = { clickedMovieItem.value = it },
				onCancel = { mediaId, mediaType ->
					action(AccountActions.DeleteMediaRating(mediaId, mediaType))
				},
				onCancelled = onCancelResult,
				onError = onCancelResult
			)

			Spacer(
				Modifier.height(
					50.dp + (if (windowSize.width == Portrait) NavigationBarHeight else 0.dp)
				)
			)
		}

		if (clickedMovieItem.value != null) {
			val itemIsMovie = clickedMovieItem.value!!.title != null

			MovieDetailsSheet(
				movie = clickedMovieItem.value!!,
				genreList = if (itemIsMovie) movieData.genres else showData.genres,
				viewMore = navigate,
				closeSheet = { clickedMovieItem.value = null }
			)
		}
	}
}

@Composable
private fun AccountLoadingUi(isLoading: Boolean) {
	Column {

		RowItems(stringResource(R.string.favorites), isLoading)

		RowItems(stringResource(R.string.watchlist), isLoading)

		RowItems(stringResource(R.string.rated), isLoading)

		Spacer(Modifier.height(50.dp + NavigationBarHeight))

	}
}