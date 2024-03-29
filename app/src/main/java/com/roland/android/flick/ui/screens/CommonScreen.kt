package com.roland.android.flick.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.roland.android.flick.state.State
import com.roland.android.flick.utils.Extensions.refine

@Composable
fun <T: Any>CommonScreen(
	state: State<T>?,
	paddingValues: PaddingValues = PaddingValues(0.dp),
	loadingScreen: @Composable (String?) -> Unit,
	successScreen: @Composable (T) -> Unit,
) {
	val layoutDirection = LocalLayoutDirection.current

	Box(
		Modifier.padding(
			start = paddingValues.calculateStartPadding(layoutDirection),
			end = paddingValues.calculateEndPadding(layoutDirection)
		)
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
}

@Composable
fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, T6: Any>CommonScreen(
	state1: State<T1>?,
	state2: State<T2>?,
	state3: State<T3>?,
	state4: State<T4>?,
	state5: State<T5>?,
	state6: State<T6>?,
	paddingValues: PaddingValues = PaddingValues(0.dp),
	loadingScreen: @Composable (String?) -> Unit,
	successScreen: @Composable (T1, T2, T3, T4, T5, T6) -> Unit
) {
	val layoutDirection = LocalLayoutDirection.current

	Box(
		Modifier.padding(
			start = paddingValues.calculateStartPadding(layoutDirection),
			end = paddingValues.calculateEndPadding(layoutDirection)
		)
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
			state5 is State.Error -> {
				loadingScreen(state5.errorMessage.refine())
			}
			state6 is State.Error -> {
				loadingScreen(state6.errorMessage.refine())
			}
			(state1 is State.Success) && (state2 is State.Success) &&
					(state3 is State.Success) && (state4 is State.Success &&
					(state5 is State.Success) && (state6 is State.Success)) -> {
				successScreen(state1.data, state2.data, state3.data, state4.data, state5.data, state6.data)
			}
		}
	}
}
