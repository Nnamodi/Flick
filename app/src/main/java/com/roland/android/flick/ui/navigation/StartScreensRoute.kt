package com.roland.android.flick.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.roland.android.flick.ui.screens.ComingSoonScreen
import com.roland.android.flick.ui.screens.HomeScreen
import com.roland.android.flick.utils.MovieListActions
import com.roland.android.flick.viewModel.ComingSoonViewModel
import com.roland.android.flick.viewModel.HomeViewModel
import com.roland.android.flick.viewModel.MovieListViewModel

fun NavGraphBuilder.startScreensRoute(
	navActions: NavActions,
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
				action = comingSoonViewModel::comingSoonActions
			)
		}
	}
}