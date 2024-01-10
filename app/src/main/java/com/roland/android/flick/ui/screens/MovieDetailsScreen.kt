package com.roland.android.flick.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.roland.android.flick.state.MovieDetailsUiState
import com.roland.android.flick.ui.components.MovieDetailsTopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetailsScreen(
	uiState: MovieDetailsUiState
) {
	val (movieDetails, tvShowDetails) = uiState

	Scaffold(
		topBar = { MovieDetailsTopBar() }
	) { _ ->
		CommonScreen(movieDetails, tvShowDetails) { movieDetails, showDetails ->
		}
	}
}