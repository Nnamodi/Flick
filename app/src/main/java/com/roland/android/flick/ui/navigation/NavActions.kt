package com.roland.android.flick.ui.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {
	fun navigateToMovieListScreen(category: String) {
		navController.navigate(
			AppRoute.MovieListScreen.routeWithCategory(category)
		)
	}
}

sealed class AppRoute(val route: String) {
	object HomeScreen: AppRoute("home_screen")
	object MovieListScreen: AppRoute("movie_list_screen/{category}") {
		fun routeWithCategory(category: String) = String.format("movie_list_screen/%s", category)
	}
}