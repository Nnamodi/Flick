package com.roland.android.flick.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonViewModel
import com.roland.android.flick.ui.screens.details.DetailsRequest
import com.roland.android.flick.ui.screens.details.MovieDetailsScreen
import com.roland.android.flick.ui.screens.details.MovieDetailsViewModel
import com.roland.android.flick.ui.screens.home.HomeViewModel
import com.roland.android.flick.ui.screens.list.MovieListActions
import com.roland.android.flick.ui.screens.list.MovieListScreen
import com.roland.android.flick.ui.screens.list.MovieListViewModel
import com.roland.android.flick.ui.screens.search.SearchScreen
import com.roland.android.flick.ui.screens.search.SearchViewModel
import com.roland.android.flick.utils.AnimationDirection
import com.roland.android.flick.utils.animatedComposable

@Composable
fun AppRoute(
	navActions: NavActions,
	navController: NavHostController,
	homeViewModel: HomeViewModel = hiltViewModel(),
	comingSoonViewModel: ComingSoonViewModel = hiltViewModel(),
	movieListViewModel: MovieListViewModel = hiltViewModel(),
	searchViewModel: SearchViewModel = hiltViewModel(),
	movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel(),
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
		animatedComposable(AppRoute.MovieListScreen.route) { backStackEntry ->
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
		animatedComposable(AppRoute.SearchScreen.route) {
			SearchScreen(
				uiState = searchViewModel.searchUiState,
				action = searchViewModel::searchActions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(
			route = AppRoute.MovieDetailsScreen.route,
			animationDirection = AnimationDirection.UpDown
		) { backStackEntry ->
			val isMovie = backStackEntry.arguments?.getBoolean("isMovie") ?: true
			val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
			LaunchedEffect(true) {
				Log.i("NavigationInfo", "isMovie: $isMovie | movieId: $movieId")
				movieDetailsViewModel.detailsRequest(
					if (isMovie) {
						DetailsRequest.GetMovieDetails(movieId)
					} else DetailsRequest.GetTvShowDetails(movieId)
				)
			}

			MovieDetailsScreen(
				uiState = movieDetailsViewModel.movieDetailsUiState,
				isMovie = isMovie,
				request = movieDetailsViewModel::detailsRequest,
				navigate = navActions::navigate
			)
		}
	}
}
