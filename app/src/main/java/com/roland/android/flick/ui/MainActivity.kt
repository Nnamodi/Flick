package com.roland.android.flick.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.roland.android.flick.ui.navigation.AppRoute
import com.roland.android.flick.ui.navigation.NavActions
import com.roland.android.flick.ui.theme.FlickTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			FlickTheme {
				Surface(
					modifier = Modifier.fillMaxSize()
				) {
					val navController = rememberNavController()
					val navActions = NavActions(navController)

					AppRoute(
						navActions = navActions,
						navController = navController
					)
				}
			}
		}
	}
}