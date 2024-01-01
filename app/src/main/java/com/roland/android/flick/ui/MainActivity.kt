package com.roland.android.flick.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.roland.android.flick.ui.screens.HomeScreen
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.viewModel.HomeViewModel
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
					val homeViewModel: HomeViewModel = hiltViewModel()
					HomeScreen(moviesState = homeViewModel.moviesFlow)
				}
			}
		}
	}
}