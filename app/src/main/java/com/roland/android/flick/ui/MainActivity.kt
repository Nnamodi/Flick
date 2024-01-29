package com.roland.android.flick.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.roland.android.flick.ui.components.BottomBar
import com.roland.android.flick.ui.components.BottomBarItems
import com.roland.android.flick.ui.navigation.AppRoute
import com.roland.android.flick.ui.navigation.NavActions
import com.roland.android.flick.ui.theme.FlickTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
	override fun onCreate(savedInstanceState: Bundle?) {
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
			FlickTheme(darkTheme = true) {
				val navController = rememberNavController()
				val navActions = NavActions(navController)
				val navBackStackEntry = navController.currentBackStackEntryAsState()
				val currentDestination = navBackStackEntry.value?.destination?.route
				val currentScreenIsStartScreens = BottomBarItems.values().any {
					currentDestination == it.route
				}
				var inFullScreen by rememberSaveable { mutableStateOf(false) }

				Scaffold(
					bottomBar = {
						BottomBar(
							expanded = currentScreenIsStartScreens && !inFullScreen,
							navController = navController
						)
					}
				) { _ ->
					AppRoute(
						navActions = navActions,
						navController = navController
					) { inFullScreen = it }
				}
			}
		}
	}
}