package com.roland.android.flick.ui.screens.coming_soon

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.ComingSoonBoxItem
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE
import com.roland.android.flick.utils.DynamicContainer
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.dynamicPageWidth
import com.roland.android.flick.utils.rememberWindowSize
import com.roland.android.flick.utils.shimmerModifier

@Composable
fun ComingSoonLoadingUi(
	expanded: Boolean,
	topPadding: Dp,
	scrollState: ScrollState,
	isLoading: Boolean
) {
	if (expanded) {
		ExpandedLoadingUi(topPadding, isLoading)
	} else {
		DefaultLoadingUi(scrollState, isLoading)
	}
}

@Composable
private fun ExpandedLoadingUi(topPadding: Dp, isLoading: Boolean) {
	val windowSize = rememberWindowSize()
	val inPortraitMode by remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}

	DynamicContainer(
		Modifier
			.fillMaxSize()
			.padding(horizontal = PADDING_WIDTH)
			.padding(top = topPadding, bottom = PADDING_WIDTH)
	) {
		ComingSoonBoxItem(
			modifier = Modifier
				.padding(end = if (inPortraitMode) 0.dp else PADDING_WIDTH)
				.then(
					if (inPortraitMode) {
						Modifier
							.fillMaxWidth()
							.fillMaxHeight(0.43f)
					} else {
						Modifier
							.fillMaxWidth(0.5f)
							.fillMaxHeight()
					}
				),
			isLoading = isLoading
		)
		Column {
			Spacer(
				Modifier
					.padding(vertical = 10.dp)
					.size(200.dp, 22.dp)
					.shimmerModifier(isLoading)
			)
			Row {
				repeat(4) {
					Spacer(
						Modifier
							.size(56.dp, 36.dp)
							.padding(end = 10.dp, bottom = 10.dp)
							.clip(CircleShape)
							.shimmerModifier(isLoading)
					)
				}
			}
			Spacer(
				Modifier
					.alpha(0.8f)
					.size(150.dp, 12.dp)
					.shimmerModifier(isLoading)
			)
			Spacer(Modifier.height(14.dp))
			repeat(4) {
				Spacer(
					Modifier
						.padding(vertical = 4.dp)
						.fillMaxWidth()
						.height(14.dp)
						.shimmerModifier(isLoading)
				)
			}
		}
	}
}

@Composable
private fun DefaultLoadingUi(scrollState: ScrollState, isLoading: Boolean) {
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
		ComingSoonLoadingUi(
			expanded = true,
			topPadding = PADDING_WIDTH,
			scrollState = rememberScrollState(),
			isLoading = true
		)
	}
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape", showBackground = true)
@Composable
fun ComingSoonLoadingLandscapeUiPreview() {
	ComingSoonLoadingUiPreview()
}