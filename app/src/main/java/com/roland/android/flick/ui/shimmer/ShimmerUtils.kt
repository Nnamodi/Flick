package com.roland.android.flick.ui.shimmer

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_MEDIUM

@Composable
fun RowItems(header: String, isLoading: Boolean) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 12.dp)
	) {
		Row(Modifier.padding(PADDING_WIDTH)) {
			Header(header)
		}
		Row(
			modifier = Modifier
				.horizontalScroll(rememberScrollState())
				.padding(bottom = 16.dp)
		) {
			Spacer(Modifier.width(PADDING_WIDTH))
			repeat(10) {
				MediumBoxItem(isLoading, Modifier.padding(end = 12.dp))
			}
		}
	}
}

@Composable
fun LargeBoxItem(isLoading: Boolean) {
	val backgroundModifier = if (isLoading) {
		Modifier.background(rememberAnimatedShimmerBrush())
	} else Modifier.background(Color.LightGray.copy(alpha = 0.6f))

	Box(
		modifier = Modifier
			.size(POSTER_WIDTH_LARGE, 370.dp)
			.padding(end = 14.dp)
			.clip(MaterialTheme.shapes.large)
			.then(backgroundModifier)
	)
}

@Composable
fun MediumBoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier
) {
	val backgroundModifier = if (isLoading) {
		Modifier.background(rememberAnimatedShimmerBrush())
	} else Modifier.background(Color.LightGray.copy(alpha = 0.6f))

	Box(
		modifier = modifier
			.size(POSTER_WIDTH_MEDIUM, 180.dp)
			.clip(MaterialTheme.shapes.large)
			.then(backgroundModifier)
	)
}

@Composable
private fun rememberAnimatedShimmerBrush(): Brush {
	val shimmerColors = listOf(
		Color.LightGray.copy(alpha = 0.6f),
		Color.LightGray.copy(alpha = 0.2f),
		Color.LightGray.copy(alpha = 0.6f)
	)

	val transition = rememberInfiniteTransition(label = "shimmer transition")
	val translateAnim = transition.animateFloat(
		initialValue = 0f,
		targetValue = 1000f,
		animationSpec = infiniteRepeatable(
			animation = tween(
				durationMillis = 1000,
				easing = FastOutLinearInEasing
			),
			repeatMode = RepeatMode.Reverse
		), label = "shimmer animation"
	)

	return Brush.linearGradient(
		colors = shimmerColors,
		start = Offset.Zero,
		end = Offset(x = translateAnim.value, y = translateAnim.value)
	)
}