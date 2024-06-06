package com.roland.android.flick.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.entity.Movie
import com.roland.android.domain.entity.auth_response.Response
import com.roland.android.flick.R
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.models.SampleData.movie5
import com.roland.android.flick.state.State
import com.roland.android.flick.ui.components.ItemBackdropPoster
import com.roland.android.flick.ui.components.Snackbar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.coming_soon.ItemDetails
import com.roland.android.flick.ui.screens.details.MovieDetailsActions
import com.roland.android.flick.ui.screens.details.MovieDetailsViewModel
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.DynamicContainer
import com.roland.android.flick.utils.Extensions.refine
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun MovieDetailsSheet(
	movie: Movie,
	genreList: List<Genre>,
	viewModel: MovieDetailsViewModel = hiltViewModel(),
	viewMore: (Screens) -> Unit,
	closeSheet: () -> Unit
) {
	val uiState = viewModel.movieDetailsUiState

	MovieDetailsSheetVisuals(
		movie = movie,
		genreList = genreList,
		userIsLoggedIn = uiState.userIsLoggedIn,
		response = uiState.response,
		detailsAction = viewModel::detailsAction,
		viewMore = viewMore,
		closeSheet = closeSheet
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsSheetVisuals(
	movie: Movie,
	genreList: List<Genre>,
	userIsLoggedIn: Boolean,
	response: State<Response>?,
	detailsAction: (MovieDetailsActions?) -> Unit,
	viewMore: (Screens) -> Unit,
	closeSheet: () -> Unit
) {
	val sheetState = rememberModalBottomSheetState(true)
	val screenHeight = LocalConfiguration.current.screenHeightDp
	val screenWidth = LocalConfiguration.current.screenWidthDp
	val windowSize = rememberWindowSize()
	val posterModifier = if (windowSize.width == WindowType.Portrait) {
		Modifier.size(screenWidth.dp, 210.dp)
	} else {
		Modifier.size((screenWidth / 2).dp, screenHeight.dp)
	}
	val snackbarMessage = remember { mutableStateOf<String?>(null) }
	val requestToLogin = remember { mutableStateOf(false) }

	ModalBottomSheet(
		onDismissRequest = closeSheet,
		modifier = Modifier.padding(horizontal = 12.dp),
		sheetState = sheetState,
		shape = RoundedCornerShape(28.dp),
		dragHandle = {}
	) {
		Box(contentAlignment = Alignment.BottomStart) {
			DynamicContainer(
				modifier = Modifier
					.clip(BottomSheetDefaults.ExpandedShape)
					.padding(12.dp)
					.heightIn(100.dp, (screenHeight / 1.5).dp)
			) {
				ItemBackdropPoster(
					movie = movie,
					modifier = posterModifier
						.clip(BottomSheetDefaults.ExpandedShape)
						.padding(end = if (windowSize.width == WindowType.Landscape) PADDING_WIDTH else 0.dp)
				)
				ItemDetails(
					movie = movie,
					genreList = genreList,
					inBottomSheet = true,
					userIsLoggedIn = userIsLoggedIn,
					actionHandled = response != null,
					logInRequest = { requestToLogin.value = true },
					detailsAction = detailsAction,
					viewMore = { closeSheet(); viewMore(it) }
				)
			}

			if (snackbarMessage.value != null) {
				Snackbar(
					message = snackbarMessage.value!!,
					paddingValues = PaddingValues(),
					onDismiss = { detailsAction(null) }
				)
			}
			if (requestToLogin.value) {
				Snackbar(
					message = stringResource(R.string.sign_up_message),
					paddingValues = PaddingValues(),
					actionLabel = stringResource(R.string.sign_up),
					action = { viewMore(Screens.AccountScreen) },
					onDismiss = { requestToLogin.value = false }
				)
			}
		}
	}

	// pops up custom snackbar to show response message returned from watchlist, favorite or rate actions.
	LaunchedEffect(response) {
		snackbarMessage.value = when (response) {
			null -> null
			is State.Error -> response.errorMessage.refine()
			is State.Success -> response.data.statusMessage
		}
	}
	LaunchedEffect(Unit) { detailsAction(null) }
}

@Preview
@Composable
fun MovieDetailsSheetPreview() {
	FlickTheme {
		var movie by remember { mutableStateOf<Movie?>(null) }

		Column(
			Modifier
				.fillMaxSize()
				.clickable { movie = movie5 }
		) {
			if (movie != null) {
				MovieDetailsSheetVisuals(
					movie = movie!!,
					genreList = genreList,
					userIsLoggedIn = false,
					response = null,
					detailsAction = {},
					viewMore = {}
				) { movie = null }
			}
		}
	}
}