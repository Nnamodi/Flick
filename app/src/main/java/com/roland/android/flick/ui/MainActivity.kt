package com.roland.android.flick.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.roland.android.domain.repository.ThemeOptions
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.NavBar
import com.roland.android.flick.ui.navigation.AppRoute
import com.roland.android.flick.ui.navigation.AppRoute.MovieDetailsScreen
import com.roland.android.flick.ui.navigation.NavActions
import com.roland.android.flick.ui.screens.auth.AuthViewModel
import com.roland.android.flick.ui.screens.settings.SettingsViewModel
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.NavigationRailWidth
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val authViewModel: AuthViewModel by viewModels()
	private val settingsViewModel: SettingsViewModel by viewModels()

	@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
	override fun onCreate(savedInstanceState: Bundle?) {
		setTheme(R.style.Theme_Flick)
		enableEdgeToEdge(
			statusBarStyle = SystemBarStyle.light(
				Color.TRANSPARENT, Color.TRANSPARENT
			),
			navigationBarStyle = SystemBarStyle.light(
				Color.TRANSPARENT, Color.TRANSPARENT
			)
		)
		super.onCreate(savedInstanceState)
		setContent {
			val theme = when (settingsViewModel.settingsUiState.theme) {
				ThemeOptions.Dark -> true
				ThemeOptions.Light -> false
				ThemeOptions.FollowSystem -> isSystemInDarkTheme()
			}

			FlickTheme(darkTheme = theme) {
				val navController = rememberNavController()
				val navActions = NavActions(navController)
				var inFullScreen by rememberSaveable { mutableStateOf(false) }
				val navBackStackEntry = navController.currentBackStack.collectAsState().value
				val detailsScreenInBackStack = MovieDetailsScreen.route in navBackStackEntry.map { it.destination.route }
				val windowSize = rememberWindowSize()
				val inLandscapeMode by remember(windowSize.width) {
					derivedStateOf { windowSize.width == WindowType.Landscape }
				}

				Scaffold(
					bottomBar = {
						NavBar(
							inFullScreen = inFullScreen || detailsScreenInBackStack,
							navController = navController
						)
					}
				) { _ ->
					Row(
						modifier = Modifier.padding(
							start = if (inLandscapeMode) NavigationRailWidth else 0.dp
						)
					) {
						AppRoute(
							navActions = navActions,
							navController = navController
						) { inFullScreen = it }
					}
				}
			}
		}
	}

	override fun onNewIntent(intent: Intent?) {
		super.onNewIntent(intent)
		authViewModel.onNewIntent(intent?.data)
	}

	override fun onResume() {
		super.onResume()
		authViewModel.onActivityResumed(true)
		settingsViewModel.onActivityResumed(true)
	}

	override fun onPause() {
		super.onPause()
		authViewModel.onActivityResumed(false)
		settingsViewModel.onActivityResumed(false)
	}
}