package com.roland.android.flick.ui.navigation

import android.util.Log
import androidx.navigation.NavHostController

class NavActions(private val navController: NavHostController) {

	fun navigate(screen: Screens) {
		when (screen) {
			Screens.HomeScreenScreen -> navigateToHomeScreen()
			Screens.ComingSoonScreen -> navigateToComingSoonScreen()
			is Screens.MovieListScreen -> navigateToMovieListScreen(screen.categoryName)
			Screens.SearchScreen -> navigateToSearchScreen()
			is Screens.MovieDetailsScreen -> navigateToMovieDetailsScreen(screen.isMovie, screen.movieId)
			Screens.Back -> navController.navigateUp()
		}
	}

	private fun navigateToHomeScreen() {
		navController.navigate(AppRoute.HomeScreen.route)
	}

	private fun navigateToComingSoonScreen() {
		navController.navigate(AppRoute.ComingSoonScreen.route)
	}

	private fun navigateToMovieListScreen(category: String) {
		navController.navigate(
			AppRoute.MovieListScreen.routeWithCategory(category)
		)
	}

	private fun navigateToSearchScreen() {
		navController.navigate(AppRoute.SearchScreen.route)
	}

	private fun navigateToMovieDetailsScreen(
		isMovie: Boolean,
		movieId: Int
	) {
		navController.navigate(
			AppRoute.MovieDetailsScreen.routeWithInfo(isMovie, movieId)
		)
		Log.i("NavigationInfo", "isMovie: $isMovie | movieId: $movieId")
	}

}

sealed class AppRoute(val route: String) {
	object StartScreens: AppRoute("start_screens")
	object HomeScreen: AppRoute("home_screen")
	object ComingSoonScreen: AppRoute("coming_soon_screen")
	object MovieListScreen: AppRoute("movie_list_screen/{category}") {
		fun routeWithCategory(category: String) = String.format("movie_list_screen/%s", category)
	}
	object SearchScreen: AppRoute("search_screen")
	object MovieDetailsScreen: AppRoute("movie_details_screen/{isMovie}/{movieId}") {
		fun routeWithInfo(
			isMovie: Boolean,
			movieId: Int
		) = String.format("movie_details_screen/%b/%d", isMovie, movieId)
	}
}

sealed class Screens {
	object HomeScreenScreen : Screens()
	object ComingSoonScreen : Screens()
	data class MovieListScreen(val categoryName: String) : Screens()
	object SearchScreen : Screens()
	data class MovieDetailsScreen(
		val isMovie: Boolean,
		val movieId: Int
	) : Screens()
	object Back : Screens()
}