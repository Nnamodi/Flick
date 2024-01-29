package com.roland.android.flick.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonViewModel
import com.roland.android.flick.ui.screens.home.HomeViewModel
import com.roland.android.flick.ui.screens.list.MovieListActions
import com.roland.android.flick.ui.screens.list.MovieListScreen
import com.roland.android.flick.ui.screens.list.MovieListViewModel
import com.roland.android.flick.ui.screens.search.SearchScreen
import com.roland.android.flick.ui.screens.search.SearchViewModel

@Composable
fun AppRoute(
	navActions: NavActions,
	navController: NavHostController,
	homeViewModel: HomeViewModel = hiltViewModel(),
	comingSoonViewModel: ComingSoonViewModel = hiltViewModel(),
	movieListViewModel: MovieListViewModel = hiltViewModel(),
	searchViewModel: SearchViewModel = hiltViewModel(),
	inFullScreen: (Boolean) -> Unit
) {
	NavHost(
		navController = navController,
		startDestination = AppRoute.StartScreens.route
	) {
		startScreensRoute(
			navActions = navActions,
			inFullScreen = inFullScreen,
			homeViewModel = homeViewModel,
			comingSoonViewModel = comingSoonViewModel,
			movieListViewModel = movieListViewModel
		)
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