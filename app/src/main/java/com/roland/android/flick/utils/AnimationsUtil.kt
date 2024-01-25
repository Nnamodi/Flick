package com.roland.android.flick.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.animatePagerItem(
	itemPage: Int,
	pagerState: PagerState
): Modifier {
	return graphicsLayer {
		val pageOffset = (
			(pagerState.currentPage - itemPage) + pagerState.currentPageOffsetFraction
		).absoluteValue.coerceIn(0f, 1f)
		scaleX = 1f - (pageOffset * 0.1f)
		scaleY = 1f - (pageOffset * 0.1f)
		alpha = lerp(
			start = Dp(0.5f),
			stop = Dp(1f),
			fraction = 1f - pageOffset
		).value
	}
}

fun Modifier.bounceClickable(
	enabled: Boolean = true,
	onClick: () -> Unit
): Modifier = composed {
	val interactionSource = remember { MutableInteractionSource() }
	val buttonPressed = interactionSource.collectIsPressedAsState()
	val scale by animateFloatAsState(
		targetValue = if (buttonPressed.value) 0.9f else 1f,
		label = "click animation"
	)

	this
		.graphicsLayer {
			if (enabled) {
				scaleX = scale
				scaleY = scale
			}
		}
		.clickable(
			interactionSource = interactionSource,
			indication = null,
			enabled = enabled
		) { onClick() }
}
