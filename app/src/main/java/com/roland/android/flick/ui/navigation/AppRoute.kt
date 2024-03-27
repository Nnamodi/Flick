package com.roland.android.flick.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.roland.android.domain.usecase.Category
import com.roland.android.flick.ui.screens.category_selection.CategorySelectionScreen
import com.roland.android.flick.ui.screens.category_selection.CategorySelectionViewModel
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
import com.roland.android.flick.utils.Constants.MOVIES
import com.roland.android.flick.utils.animatedComposable

@Composable
fun AppRoute(
	navActions: NavActions,
	navController: NavHostController,
	homeViewModel: HomeViewModel = hiltViewModel(),
	comingSoonViewModel: ComingSoonViewModel = hiltViewModel(),
	movieListViewModel: MovieListViewModel = hiltViewModel(),
	searchViewModel: SearchViewModel = hiltViewModel(),
	categorySelectionViewModel: CategorySelectionViewModel = hiltViewModel(),
	movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel(),
	inFullScreen: (Boolean) -> Unit,
) {
	NavHost(
		navController = navController,
		startDestination = AppRoute.StartScreens.route
	) {
		startScreensRoute(
			navActions = navActions,
			inFullScreen = inFullScreen,
			homeViewModel = homeViewModel,
			comingSoonViewModel = comingSoonViewModel
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
			route = AppRoute.CategorySelectionScreen.route,
			animationDirection = AnimationDirection.UpDown
		) {
			CategorySelectionScreen(
				uiState = categorySelectionViewModel.categorySelectionUiState,
				action = categorySelectionViewModel::categorySelectionActions,
				navigate = navActions::navigate
			)
		}
		animatedComposable(
			route = AppRoute.MovieDetailsScreen.route,
			animationDirection = AnimationDirection.UpDown
		) { backStackEntry ->
			val movieType = backStackEntry.arguments?.getString("movieType") ?: ""
			val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
			val isMovie = movieType == MOVIES
			LaunchedEffect(true) {
				movieDetailsViewModel.detailsRequest(
					if (isMovie) {
						DetailsRequest.GetMovieDetails(movieId.toInt())
					} else DetailsRequest.GetTvShowDetails(movieId.toInt())
				)
			}

			MovieDetailsScreen(
				uiState = movieDetailsViewModel.movieDetailsUiState,
				isMovie = isMovie,
				request = movieDetailsViewModel::detailsRequest,
				action = movieDetailsViewModel::detailsAction,
				navigate = navActions::navigate
			)
		}
	}
}
