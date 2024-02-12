package com.roland.android.flick.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roland.android.flick.utils.Constants.MOVIE_IMAGE_BASE_URL_W500
import com.roland.android.flick.utils.Constants.MOVIE_IMAGE_BASE_URL_W780
import com.roland.android.flick.utils.Constants.SCREEN_HEIGHT_DIVISOR
import com.roland.android.flick.utils.Constants.SCREEN_WIDTH_DIVISOR

data class WindowSize(
	val width: WindowType,
	val height: WindowType
)

enum class WindowType { Portrait, Landscape }

@Composable
fun rememberWindowSize(): WindowSize {
	val configuration = LocalConfiguration.current
	val screenWidth = remember(configuration) {
		mutableIntStateOf(configuration.screenWidthDp)
	}
	val screenHeight = remember(configuration) {
		mutableIntStateOf(configuration.screenHeightDp)
	}

	return WindowSize(
		width = getScreenWidth(screenWidth.intValue),
		height = getScreenHeight(screenHeight.intValue)
	)
}

private fun getScreenWidth(width: Int): WindowType = when {
	width < 600 -> WindowType.Portrait
	else -> WindowType.Landscape
}

private fun getScreenHeight(height: Int): WindowType = when {
	height < 480 -> WindowType.Portrait
	else -> WindowType.Landscape
}

fun Modifier.dynamicPageSize(
	portraitWidth: Dp,
	portraitHeight: Dp
): Modifier = composed {
	val windowSize = rememberWindowSize()
	val itemHeight = LocalConfiguration.current.screenHeightDp * SCREEN_HEIGHT_DIVISOR
	val itemWidth = LocalConfiguration.current.screenWidthDp * SCREEN_WIDTH_DIVISOR

	if (windowSize.width == WindowType.Landscape) {
		size(itemWidth.dp, itemHeight.dp)
	} else {
		size(portraitWidth, portraitHeight)
	}
}

@Composable
fun dynamicPageWidth(portraitWidth: Dp): Dp {
	val windowSize = rememberWindowSize()
	val pageWidth = LocalConfiguration.current.screenWidthDp * SCREEN_WIDTH_DIVISOR

	return if (windowSize.width == WindowType.Landscape) {
		pageWidth.dp
	} else {
		portraitWidth
	}
}

@Composable
fun dynamicPageHeight(portraitHeight: Dp): Dp {
	val windowSize = rememberWindowSize()
	val pageHeight = LocalConfiguration.current.screenHeightDp * SCREEN_HEIGHT_DIVISOR

	return if (windowSize.width == WindowType.Landscape) {
		pageHeight.dp
	} else {
		portraitHeight
	}
}

@Composable
fun String?.getPoster(isBackdrop: Boolean = false): String {
	val windowSize = rememberWindowSize()
	return buildString {
		append(
			when {
				isBackdrop -> MOVIE_IMAGE_BASE_URL_W780
				windowSize.width == WindowType.Portrait -> MOVIE_IMAGE_BASE_URL_W500
				else -> MOVIE_IMAGE_BASE_URL_W780
			}
		)
		append(this@getPoster)
	}
}

@Composable
fun DynamicContainer(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	val windowSize = rememberWindowSize()
	if (windowSize.width == WindowType.Portrait) {
		Column(modifier) { content() }
	} else Row(modifier) { content() }
}
