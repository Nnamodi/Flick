package com.roland.android.flick.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.roland.android.flick.ui.screens.account.AccountScreen
import com.roland.android.flick.ui.screens.account.AccountViewModel
import com.roland.android.flick.ui.screens.auth.AuthViewModel
import com.roland.android.flick.ui.screens.auth.SignUpScreen
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonScreen
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonViewModel
import com.roland.android.flick.ui.screens.home.HomeScreen
import com.roland.android.flick.ui.screens.home.HomeViewModel

fun NavGraphBuilder.startScreensRoute(
	navActions: NavActions,
	inFullScreen: (Boolean) -> Unit,
	homeViewModel: HomeViewModel,
	comingSoonViewModel: ComingSoonViewModel,
	accountViewModel: AccountViewModel,
	authViewModel: AuthViewModel
) {
	navigation(
		startDestination = AppRoute.HomeScreen.route,
		route = AppRoute.StartScreens.route
	) {
		composable(AppRoute.HomeScreen.route) {
			HomeScreen(
				uiState = homeViewModel.homeUiState,
				action = homeViewModel::homeActions,
				navigate = navActions::navigate
			)
		}
		composable(AppRoute.ComingSoonScreen.route) {
			ComingSoonScreen(
				uiState = comingSoonViewModel.comingSoonUiState,
				action = comingSoonViewModel::comingSoonActions,
				navigate = navActions::navigate,
				inFullScreen = inFullScreen
			)
		}
		composable(AppRoute.AccountScreen.route) {
			if (accountViewModel.userLoggedIn) {
				AccountScreen(
					uiState = accountViewModel.accountUiState
				)
			} else {
				SignUpScreen(
					uiState = authViewModel.authUiState,
					authAction = authViewModel::authActions
				)
			}
		}
	}
}