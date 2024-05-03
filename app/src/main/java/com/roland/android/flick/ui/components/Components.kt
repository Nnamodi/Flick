package com.roland.android.flick.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.ButtonDefaults.textButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.flick.R
import com.roland.android.flick.ui.screens.home.HomeActions
import com.roland.android.flick.utils.Extensions.roundOff
import com.roland.android.flick.utils.bounceClickable
import kotlinx.coroutines.delay

@Composable
fun RatingBar(
	posterType: PosterType,
	voteAverage: Double,
	fillMaxWidth: Boolean = true
) {
	Row {
		if (fillMaxWidth) Spacer(Modifier.weight(1f))
		Row(
			modifier = Modifier
				.padding(6.dp)
				.clip(MaterialTheme.shapes.large)
				.background(Color.Black.copy(alpha = 0.5f)),
			verticalAlignment = Alignment.CenterVertically
		) {
			val posterIsVeryLarge = posterType == PosterType.BackdropPoster || posterType == PosterType.FullScreen
			if (posterIsVeryLarge) {
				Icon(
					imageVector = Icons.Rounded.StarRate,
					contentDescription = null,
					modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
					tint = colorScheme.surfaceTint
				)
			}
			Text(
				text = voteAverage.roundOff(),
				modifier = Modifier.padding(
					start = if (posterIsVeryLarge) 4.dp else 10.dp,
					top = 2.dp,
					end = 10.dp,
					bottom = 2.dp
				),
				color = Color.White,
				fontSize = if (posterType == PosterType.Large) 16.sp else 14.sp
			)
		}
	}
}

@Composable
fun Snackbar(
	message: String,
	modifier: Modifier = Modifier,
	paddingValues: PaddingValues,
	actionLabel: String? = null,
	action: () -> Unit = {},
	duration: SnackbarDuration = SnackbarDuration.Short,
	onDismiss: () -> Unit = {}
) {
	val snackbarMessage = rememberSaveable(message) { mutableStateOf<String?>(message) }

	AnimatedVisibility(
		visible = snackbarMessage.value != null,
		enter = slideInVertically(tween(200)) { it },
		exit = slideOutVertically(tween(100)) { it },
	) {
		SnackbarVisuals(
			message = message,
			modifier = modifier.padding(bottom = paddingValues.calculateBottomPadding()),
			actionLabel = actionLabel,
			action = {
				snackbarMessage.value = null
				action()
			}
		)
	}

	LaunchedEffect(message) {
		delay(duration.millis)
		snackbarMessage.value = null
		onDismiss()
	}

	DisposableEffect(Unit) {
		onDispose {
			if (duration == SnackbarDuration.Indefinite) return@onDispose
			snackbarMessage.value = null
			onDismiss()
		}
	}
}

@Composable
private fun SnackbarVisuals(
	message: String,
	modifier: Modifier,
	actionLabel: String?,
	action: () -> Unit
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(16.dp)
			.clip(MaterialTheme.shapes.medium)
			.background(colorScheme.onBackground),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = message,
			modifier = Modifier.padding(20.dp),
			color = colorScheme.background,
			style = MaterialTheme.typography.bodyMedium
		)
		Spacer(Modifier.weight(1f))
		actionLabel?.let { label ->
			TextButton(
				onClick = action,
				modifier = Modifier.padding(end = 10.dp),
				colors = textButtonColors(contentColor = colorScheme.inversePrimary)
			) {
				Text(label)
			}
		}
	}
}

@Suppress("unused")
enum class SnackbarDuration(val millis: kotlin.Long) {
	Short(4000L),
	Long(10000L),
	Indefinite(kotlin.Long.MAX_VALUE)
}

@Composable
fun Header(
	header: String,
	modifier: Modifier = Modifier,
	header2: String? = null,
	selectedHeader: Int? = null,
	onHeaderClick: (Int) -> Unit = {}
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		Divider(
			modifier = Modifier
				.padding(vertical = 8.dp)
				.size(4.dp, 18.dp)
				.clip(MaterialTheme.shapes.medium),
			color = colorScheme.surfaceTint
		)
		Text(
			text = header,
			modifier = Modifier
				.padding(start = 4.dp)
				.bounceClickable(header2 != null) { onHeaderClick(0) },
			color = if (selectedHeader == 0) colorScheme.surfaceTint else Color.Unspecified,
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp
		)
		header2?.let {
			DotSeparator()
			Text(
				text = header2,
				modifier = Modifier.bounceClickable { onHeaderClick(1) },
				color = if (selectedHeader == 1) colorScheme.surfaceTint else Color.Unspecified,
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)
		}
	}
}

@Composable
fun Header(
	header: String,
	modifier: Modifier = Modifier,
	selectedMediaType: String,
	showSeeMore: Boolean,
	onMediaTypeChange: (String) -> Unit,
	seeMore: (String) -> Unit
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		Divider(
			modifier = Modifier
				.padding(vertical = 8.dp)
				.size(4.dp, 18.dp)
				.clip(MaterialTheme.shapes.medium),
			color = colorScheme.surfaceTint
		)
		Row(
			modifier = Modifier
				.padding(start = 4.dp)
				.clip(MaterialTheme.shapes.small)
				.clickable(showSeeMore) { seeMore(selectedMediaType) }
				.padding(6.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = header,
				modifier = Modifier.padding(horizontal = 4.dp),
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)
			if (showSeeMore) {
				Icon(Icons.Rounded.KeyboardArrowRight, stringResource(R.string.more))
			}
		}
		Spacer(Modifier.weight(1f))
		ToggleButton(
			selectedOption = selectedMediaType,
			maximumSize = false,
			onClick = { action ->
				val mediaType = (action as HomeActions.ToggleCategory).category
				onMediaTypeChange(mediaType)
			}
		)
	}
}

@Composable
fun DotSeparator() {
	Box(
		modifier = Modifier
			.padding(horizontal = 12.dp)
			.size(8.dp)
			.clip(CircleShape)
			.background(colorScheme.outline)
	)
}