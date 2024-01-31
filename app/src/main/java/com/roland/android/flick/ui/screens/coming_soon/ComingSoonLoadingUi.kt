package com.roland.android.flick.ui.screens.coming_soon

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.ComingSoonBoxItem
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.dynamicPageWidth
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun ComingSoonLoadingUi(scrollState: ScrollState, isLoading: Boolean) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center
	) {
		val windowSize = rememberWindowSize()
		val inPortraitMode = remember(windowSize.width) {
			derivedStateOf { windowSize.width == WindowType.Portrait }
		}
		val screenWidth = LocalConfiguration.current.screenWidthDp
		val itemWidth = dynamicPageWidth(POSTER_WIDTH_X_LARGE)
		val horizontalPadding = (screenWidth.dp - itemWidth) / 2

		Row(Modifier.horizontalScroll(scrollState)) {
			Spacer(Modifier.width(horizontalPadding))
			repeat(10) {
				ComingSoonBoxItem(
					isLoading = isLoading,
					modifier = Modifier.padding(
						top = if (inPortraitMode.value) 0.dp else 64.dp,
						end = 14.dp,
						bottom = if (inPortraitMode.value) 16.dp else 0.dp
					)
				)
			}
			Spacer(Modifier.width(max(14.dp, horizontalPadding - 14.dp)))
		}
	}
}

@Preview(showBackground = true)
@Composable
fun ComingSoonLoadingUiPreview() {
	FlickTheme {
		ComingSoonLoadingUi(scrollState = rememberScrollState(), isLoading = true)
	}
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape", showBackground = true)
@Composable
fun ComingSoonLoadingLandscapeUiPreview() {
	ComingSoonLoadingUiPreview()
}