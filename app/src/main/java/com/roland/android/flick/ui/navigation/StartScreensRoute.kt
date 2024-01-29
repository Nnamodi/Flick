package com.roland.android.flick.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonScreen
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonViewModel
import com.roland.android.flick.ui.screens.home.HomeScreen
import com.roland.android.flick.ui.screens.home.HomeViewModel
import com.roland.android.flick.ui.screens.list.MovieListActions
import com.roland.android.flick.ui.screens.list.MovieListViewModel

fun NavGraphBuilder.startScreensRoute(
	navActions: NavActions,
	inFullScreen: (Boolean) -> Unit,
	homeViewModel: HomeViewModel,
	comingSoonViewModel: ComingSoonViewModel,
	movieListViewModel: MovieListViewModel
) {
	navigation(
		startDestination = AppRoute.HomeScreen.route,
		route = AppRoute.StartScreens.route
	) {
		composable(AppRoute.HomeScreen.route) {
			HomeScreen(
				uiState = homeViewModel.homeUiState,
				action = homeViewModel::homeActions,
				navigate = { // clear movie-list screen data before navigating
					movieListViewModel.movieListActions(MovieListActions.PrepareScreen)
					navActions.navigate(it)
				}
			)
		}
		composable(AppRoute.ComingSoonScreen.route) {
			ComingSoonScreen(
				uiState = comingSoonViewModel.comingSoonUiState,
				action = comingSoonViewModel::comingSoonActions,
				inFullScreen = inFullScreen
			)
		}
	}
}