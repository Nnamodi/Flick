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
import com.roland.android.flick.utils.Extensions.refine

@Composable
fun <T: Any>CommonScreen(
	state: State<T>?,
	loadingScreen: @Composable (String?) -> Unit,
	successScreen: @Composable (T) -> Unit,
) {
	when (state) {
		null -> {
			loadingScreen(null)
		}
		is State.Error -> {
			loadingScreen(state.errorMessage.refine())
		}
		is State.Success -> {
			successScreen(state.data)
		}
	}
}

@Composable
fun <T: Any, R: Any, B: Any, S: Any>CommonScreen(
	state1: State<T>?,
	state2: State<R>?,
	state3: State<B>?,
	state4: State<S>?,
	loadingScreen: @Composable (String?) -> Unit,
	successScreen: @Composable (T, R, B, S) -> Unit
) {
	when {
		(state1 == null) || (state2 == null) ||
				(state3 == null) || (state4 == null) -> {
			loadingScreen(null)
		}
		state1 is State.Error -> {
			loadingScreen(state1.errorMessage.refine())
		}
		state2 is State.Error -> {
			loadingScreen(state2.errorMessage.refine())
		}
		state3 is State.Error -> {
			loadingScreen(state3.errorMessage.refine())
		}
		state4 is State.Error -> {
			loadingScreen(state4.errorMessage.refine())
		}
		(state1 is State.Success) && (state2 is State.Success) &&
				(state3 is State.Success) && (state4 is State.Success) -> {
			successScreen(state1.data, state2.data, state3.data, state4.data)
		}
	}
}

@Composable
fun <T: Any, R: Any, B: Any, S: Any>CommonDetailsScreen(
	state1: State<T>?,
	state2: State<R>?,
	state3: State<B>?,
	state4: State<S>?,
	loadingScreen: @Composable (String?) -> Unit = { LoadingScreen() },
	castDetailsSheet: @Composable (S?) -> Unit,
	successScreen: @Composable (T?, R?, B?) -> Unit,
) {
	when {
		(state1 == null) -> {
			loadingScreen(null)
		}
		state1 is State.Error -> {
			ErrorScreen(state1.errorMessage.refine())
		}
		(state1 is State.Success) -> {
			successScreen(state1.data, null, null)
		}

		(state2 == null) || (state3 == null) -> {
			loadingScreen(null)
		}
		state2 is State.Error -> {
			ErrorScreen(state2.errorMessage.refine())
		}
		state3 is State.Error -> {
			ErrorScreen(state3.errorMessage.refine())
		}
		(state2 is State.Success) && (state3 is State.Success) -> {
			successScreen(null, state2.data, state3.data)
		}

		(state4 == null) -> {
			castDetailsSheet(null)
		}
		state4 is State.Error -> {
			castDetailsSheet(null)
		}
		state4 is State.Success -> {
			castDetailsSheet(state4.data)
		}
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

@Composable
private fun ErrorScreen(error: String?) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(text = error ?: "Error", Modifier.padding(20.dp))
	}
}
