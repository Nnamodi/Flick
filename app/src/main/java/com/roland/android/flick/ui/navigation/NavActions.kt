package com.roland.android.flick.ui.navigation

import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

	fun navigate(screen: Screens) {
		when (screen) {
			Screens.HomeScreenScreen -> navigateToHomeScreen()
			Screens.ComingSoonScreen -> navigateToComingSoonScreen()
			Screens.AccountScreen -> navigateToAccountScreen()
			is Screens.MovieListScreen -> navigateToMovieListScreen(screen.categoryName)
			Screens.SearchScreen -> navigateToSearchScreen()
			Screens.CategorySelectionScreen -> navigateToCategorySelectionScreen()
			is Screens.MovieDetailsScreen -> navigateToMovieDetailsScreen(screen.movieType, screen.movieId)
			Screens.SettingsScreen -> navigateToSettingsScreen()
			Screens.Back -> navController.navigateUp()
		}
	}

	private fun navigateToHomeScreen() {
		navController.navigate(AppRoute.HomeScreen.route)
	}

	private fun navigateToComingSoonScreen() {
		navController.navigate(AppRoute.ComingSoonScreen.route)
	}

	private fun navigateToAccountScreen() {
		navController.navigate(AppRoute.AccountScreen.route)
	}

	private fun navigateToMovieListScreen(category: String) {
		navController.navigate(
			AppRoute.MovieListScreen.routeWithCategory(category)
		)
	}

	private fun navigateToSearchScreen() {
		navController.navigate(AppRoute.SearchScreen.route)
	}

	private fun navigateToCategorySelectionScreen() {
		navController.navigate(AppRoute.CategorySelectionScreen.route)
	}

	private fun navigateToMovieDetailsScreen(
		movieType: String,
		movieId: String
	) {
		navController.navigate(
			AppRoute.MovieDetailsScreen.routeWithInfo(movieType, movieId)
		)
	}

	private fun navigateToSettingsScreen() {
		navController.navigate(AppRoute.SettingsScreen.route)
	}

}

sealed class AppRoute(val route: String) {
	object StartScreens: AppRoute("start_screens")
	object HomeScreen: AppRoute("home_screen")
	object ComingSoonScreen: AppRoute("coming_soon_screen")
	object AccountScreen: AppRoute("account_screen")
	object MovieListScreen: AppRoute("movie_list_screen/{category}") {
		fun routeWithCategory(category: String) = String.format("movie_list_screen/%s", category)
	}
	object SearchScreen: AppRoute("search_screen")
	object CategorySelectionScreen: AppRoute("category_selection_screen")
	object MovieDetailsScreen: AppRoute("movie_details_screen/{movieType}/{movieId}") {
		fun routeWithInfo(
			movieType: String,
			movieId: String
		) = String.format("movie_details_screen/%s/%s", movieType, movieId)
	}
	object SettingsScreen: AppRoute("settings_screen")
}

sealed class Screens {
	object HomeScreenScreen : Screens()
	object ComingSoonScreen : Screens()
	object AccountScreen : Screens()
	data class MovieListScreen(val categoryName: String) : Screens()
	object SearchScreen : Screens()
	object CategorySelectionScreen : Screens()
	data class MovieDetailsScreen(
		val movieType: String,
		val movieId: String
	) : Screens()
	object SettingsScreen : Screens()
	object Back : Screens()
}