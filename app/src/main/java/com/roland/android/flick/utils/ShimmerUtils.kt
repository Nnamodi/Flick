package com.roland.android.flick.utils

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_LARGE
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_SMALL
import com.roland.android.flick.utils.Constants.POSTER_HEIGHT_X_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_LARGE
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_MEDIUM
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_SMALL
import com.roland.android.flick.utils.Constants.POSTER_WIDTH_X_LARGE

@Composable
fun RowItems(
	header: String,
	isLoading: Boolean,
	content: @Composable () -> Unit = { SmallBoxItem(isLoading) }
) {
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
			repeat(10) { content() }
		}
	}
}

@Composable
fun EpisodeLoadingUi(isLoading: Boolean) {
	Row(Modifier.horizontalScroll(rememberScrollState())) {
		Spacer(Modifier.width(PADDING_WIDTH))
		repeat(10) {
			ShimmerBoxItem(
				isLoading = isLoading,
				modifier = Modifier
					.padding(end = 12.dp)
					.size(180.dp, 130.dp)
			)
		}
		Spacer(Modifier.width(PADDING_WIDTH - 12.dp))
	}
}

@Composable
fun CircleItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier.width(120.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(Modifier
			.size(100.dp)
			.clip(CircleShape)
			.shimmerModifier(isLoading)
		)
		Spacer(
			modifier = Modifier
				.padding(vertical = 8.dp)
				.size(70.dp, 16.dp)
				.shimmerModifier(isLoading)
		)
		Spacer(
			modifier = Modifier
				.size(55.dp, 14.dp)
				.alpha(0.8f)
				.shimmerModifier(isLoading)
		)
	}
}

@Composable
fun ComingSoonBoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier,
) {
	ShimmerBoxItem(
		isLoading = isLoading,
		modifier = modifier.dynamicPageSize(POSTER_WIDTH_X_LARGE, POSTER_HEIGHT_X_LARGE)
	)
}

@Composable
fun LargeBoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier,
) {
	ShimmerBoxItem(
		isLoading = isLoading,
		modifier = modifier.dynamicPageSize(POSTER_WIDTH_LARGE, POSTER_HEIGHT_LARGE)
	)
}

@Composable
fun MediumBoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier
) {
	ShimmerBoxItem(
		isLoading = isLoading,
		modifier = modifier.size(POSTER_WIDTH_MEDIUM, POSTER_HEIGHT_MEDIUM)
	)
}

@Composable
fun SmallBoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier
) {
	ShimmerBoxItem(
		isLoading = isLoading,
		modifier = modifier
			.padding(end = 12.dp)
			.size(POSTER_WIDTH_SMALL, POSTER_HEIGHT_SMALL)
	)
}

@Composable
private fun ShimmerBoxItem(
	isLoading: Boolean,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.clip(MaterialTheme.shapes.large)
			.then(Modifier.shimmerModifier(isLoading))
	)
}

fun Modifier.painterPlaceholder(state: AsyncImagePainter.State): Modifier = composed {
	if (state is AsyncImagePainter.State.Loading) {
		background(rememberAnimatedShimmerBrush())
	} else {
		drawBehind {
			drawRect(Color.LightGray.copy(alpha = 0.6f))
		}
	}
}

fun Modifier.shimmerModifier(isLoading: Boolean): Modifier = composed {
	if (isLoading) {
		background(rememberAnimatedShimmerBrush())
	} else {
		background(Color.LightGray.copy(alpha = 0.6f))
	}
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