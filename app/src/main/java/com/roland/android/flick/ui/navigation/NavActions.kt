package com.roland.android.flick.ui.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

	fun navigate(screen: Screens) {
		when (screen) {
			is Screens.MovieListScreen -> navigateToMovieListScreen(screen.categoryName)
			Screens.SearchScreen -> navigateToSearchScreen()
			Screens.Back -> navController.navigateUp()
		}
	}

	private fun navigateToMovieListScreen(category: String) {
		navController.navigate(
			AppRoute.MovieListScreen.routeWithCategory(category)
		)
	}

	private fun navigateToSearchScreen() {
		navController.navigate(AppRoute.SearchScreen.route)
	}

}

sealed class AppRoute(val route: String) {
	object HomeScreen: AppRoute("home_screen")
	object MovieListScreen: AppRoute("movie_list_screen/{category}") {
		fun routeWithCategory(category: String) = String.format("movie_list_screen/%s", category)
	}
	object SearchScreen: AppRoute("search_screen")
}

sealed class Screens {
	data class MovieListScreen(val categoryName: String) : Screens()
	object SearchScreen : Screens()
	object Back : Screens()
}