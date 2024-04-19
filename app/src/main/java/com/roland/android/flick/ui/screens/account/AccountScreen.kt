package com.roland.android.flick.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.roland.android.flick.ui.screens.CommonScreen
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun AccountScreen(
	uiState: AccountUiState
) {
	val (accountDetails, movieData, showData) = uiState

	Scaffold(
		topBar = { AccountTopBar() }
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
				paddingValues = paddingValues
			)
		}
	}
}

@Composable
private fun MediaRows(
	movies: State<AccountMediaModel>?,
	shows: State<AccountMediaModel>?,
	paddingValues: PaddingValues
) {
	CommonScreen(
		movies, shows, paddingValues,
		loadingScreen = {
			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) { Text("Loading...") }
		}
	) { movieData, showData ->
		val clickedMovieItem = remember { mutableStateOf<Movie?>(null) }
		val windowSize = rememberWindowSize()

		Column {
			val favoriteMediaType by rememberSaveable { mutableStateOf(MOVIES) }
			HorizontalPosters(
				pagingData = if (favoriteMediaType == MOVIES) movieData.favoriteList else showData.favoriteList,
				header = stringResource(R.string.favorites),
				onMovieClick = { clickedMovieItem.value = it }
			) {}

			val watchlistMediaType by rememberSaveable { mutableStateOf(MOVIES) }
			HorizontalPosters(
				pagingData = if (watchlistMediaType == MOVIES) movieData.watchlist else showData.watchlist,
				header = stringResource(R.string.watchlist),
				onMovieClick = { clickedMovieItem.value = it }
			) {}

			val ratedMediaType by rememberSaveable { mutableStateOf(MOVIES) }
			HorizontalPosters(
				pagingData = if (ratedMediaType == MOVIES) movieData.ratedList else showData.ratedList,
				header = stringResource(R.string.rated),
				onMovieClick = { clickedMovieItem.value = it }
			) {}

			Spacer(
				Modifier.height(
					50.dp + (if (windowSize.width == WindowType.Portrait) NavigationBarHeight else 0.dp)
				)
			)
		}
	}
}