package com.roland.android.flick.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.roland.android.flick.utils.AnimationDirection.LeftRight
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
	indication: Indication? = null,
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
			indication = indication,
			enabled = enabled
		) { onClick() }
}

// navigation animation
fun NavGraphBuilder.animatedComposable(
	route: String,
	animationDirection: AnimationDirection = LeftRight,
	arguments: List<NamedNavArgument> = emptyList(),
	deepLinks: List<NavDeepLink> = emptyList(),
	content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable(
	route = route,
	arguments = arguments,
	deepLinks = deepLinks,
	enterTransition = {
		if (animationDirection == LeftRight) {
			slideInHorizontally(tween(700)) { it }
		} else {
			slideInVertically(tween(700)) { it }
		}
	},
	exitTransition = null,
	popEnterTransition = null,
	popExitTransition = {
		if (animationDirection == LeftRight) {
			slideOutHorizontally(tween(700)) { it }
		} else {
			slideOutVertically(tween(700)) { it }
		}
	},
	content = content
)

enum class AnimationDirection {
	LeftRight, UpDown
}
