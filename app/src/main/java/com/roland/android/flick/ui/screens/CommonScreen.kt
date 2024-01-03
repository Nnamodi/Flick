package com.roland.android.flick.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roland.android.flick.state.State

@Composable
fun <T: Any>CommonScreen(
	state: State<T>?,
	loadingScreen: @Composable () -> Unit = { LoadingScreen() },
	successScreen: @Composable (T) -> Unit,
) {
	when (state) {
		null -> {
			loadingScreen()
		}
		is State.Loading -> {
			loadingScreen()
		}
		is State.Error -> {
			ErrorScreen(state.errorMessage)
		}
		is State.Success -> {
			successScreen(state.data)
		}
	}
}

@Composable
fun <T: Any, R: Any>CommonScreen(
	state1: State<T>?,
	state2: State<R>?,
	loadingScreen: @Composable () -> Unit = { LoadingScreen() },
	successScreen: @Composable (T, R) -> Unit,
) {
	when {
		state1 == null || state2 == null -> {
			loadingScreen()
		}
		state1 is State.Loading || state2 is State.Loading -> {
			loadingScreen()
		}
		state1 is State.Error -> {
			ErrorScreen(state1.errorMessage)
		}
		state2 is State.Error -> {
			ErrorScreen(state2.errorMessage)
		}
		state1 is State.Success && state2 is State.Success -> {
			successScreen(state1.data, state2.data)
		}
	}
}

@Composable
private fun ErrorScreen(errorMessage: String) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = errorMessage, Modifier.padding(20.dp))
	}
}

@Composable
private fun LoadingScreen() {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = "Loading", Modifier.padding(20.dp))
	}
}