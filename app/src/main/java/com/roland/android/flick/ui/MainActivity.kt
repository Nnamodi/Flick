package com.roland.android.flick.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.viewModel.MoviesViewModel
import com.roland.android.flick.viewModel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			FlickTheme {
				// A surface container using the 'background' color from the theme
				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.background)
				) {
					val moviesViewModel: MoviesViewModel = hiltViewModel()
					val tvShowsViewModel: TvShowsViewModel = hiltViewModel()
					Greeting("${moviesViewModel.moviesUiState}")
					Greeting("${tvShowsViewModel.itemDetailsUiState}")
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	FlickTheme {
		Greeting("Android")
	}
}