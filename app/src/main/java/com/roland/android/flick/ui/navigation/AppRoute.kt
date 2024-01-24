package com.roland.android.flick.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.ui.screens.HomeScreen
import com.roland.android.flick.ui.screens.MovieListScreen
import com.roland.android.flick.ui.screens.SearchScreen
import com.roland.android.flick.utils.MovieListActions
import com.roland.android.flick.viewModel.HomeViewModel
import com.roland.android.flick.viewModel.MovieListViewModel
import com.roland.android.flick.viewModel.SearchViewModel

@Composable
fun AppRoute(
	navActions: NavActions,
	navController: NavHostController,
	homeViewModel: HomeViewModel = hiltViewModel(),
	movieListViewModel: MovieListViewModel = hiltViewModel(),
	searchViewModel: SearchViewModel = hiltViewModel()
) {
	NavHost(
		navController = navController,
		startDestination = AppRoute.HomeScreen.route
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
		composable(AppRoute.MovieListScreen.route) { backStackEntry ->
			val categoryName = backStackEntry.arguments?.getString("category") ?: ""
			val category = Category.valueOf(categoryName)
			LaunchedEffect(true) {
				movieListViewModel.movieListActions(MovieListActions.LoadMovieList(category))
			}

			MovieListScreen(
				uiState = movieListViewModel.movieListUiState,
				category = categoryName,
				action = movieListViewModel::movieListActions,
				navigate = navActions::navigate
			)
		}
		composable(AppRoute.SearchScreen.route) {
			SearchScreen(
				uiState = searchViewModel.searchUiState,
				action = searchViewModel::searchActions,
				navigate = navActions::navigate
			)
		}
	}
}