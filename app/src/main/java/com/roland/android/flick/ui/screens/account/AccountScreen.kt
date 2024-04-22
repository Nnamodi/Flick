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
import androidx.compose.material3.ButtonDefaults
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
import com.roland.android.flick.models.AccountMediaModel
import com.roland.android.flick.state.AccountUiState
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.AccountTopBar
import com.roland.android.flick.ui.components.HorizontalPosters
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.ui.sheets.MovieDetailsSheet
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.RowItems
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
	uiState: AccountUiState,
	action: (AccountActions) -> Unit,
	navigate: (Screens) -> Unit
) {
	val (accountDetails, movieData, showData) = uiState
	val snackbarHostState = remember { SnackbarHostState() }
	val errorMessage = rememberSaveable { mutableStateOf<String?>(null) }
	val windowSize = rememberWindowSize()

	Scaffold(
		topBar = { AccountTopBar() },
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
									onClick = { action(AccountActions.ReloadMedia) },
									colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)
								) { Text(it) }
							}
						}
					) {
						Text(data.visuals.message)
					}
				}
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
					text = accountDetails.username,
					modifier = Modifier.padding(start = 20.dp),
					style = MaterialTheme.typography.headlineSmall
				)
			}
			MediaRows(
				movies = movieData,
				shows = showData,
				paddingValues = paddingValues,
				snackbarHostState = snackbarHostState,
				onError = { errorMessage.value = it },
				navigate = navigate
			)
		}
	}
}

@Composable
private fun MediaRows(
	movies: State<AccountMediaModel>?,
	shows: State<AccountMediaModel>?,
	paddingValues: PaddingValues,
	snackbarHostState: SnackbarHostState,
	onError: (String?) -> Unit,
	navigate: (Screens) -> Unit
) {
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
					snackbarHostState.showSnackbar(it, actionLabel, duration = Indefinite)
				}
			}
		}
	) { movieData, showData ->
		Column {
			HorizontalPosters(
				moviesData = movieData.favoriteList,
				showsData = showData.favoriteList,
				header = stringResource(R.string.favorites),
				onMovieClick = { clickedMovieItem.value = it }
			)

			HorizontalPosters(
				moviesData = movieData.watchlist,
				showsData = showData.watchlist,
				header = stringResource(R.string.watchlist),
				onMovieClick = { clickedMovieItem.value = it }
			)

			HorizontalPosters(
				moviesData = movieData.ratedList,
				showsData = showData.ratedList,
				header = stringResource(R.string.rated),
				onMovieClick = { clickedMovieItem.value = it }
			)

			Spacer(
				Modifier.height(
					50.dp + (if (windowSize.width == WindowType.Portrait) NavigationBarHeight else 0.dp)
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