package com.roland.android.flick.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.flick.utils.Extensions.roundOff
import com.roland.android.flick.utils.bounceClickable

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
					tint = MaterialTheme.colorScheme.surfaceTint
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
fun Header(
	header: String,
	modifier: Modifier = Modifier,
	header2: String? = null,
	selectedHeader: Int = 0,
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
			color = MaterialTheme.colorScheme.surfaceTint
		)
		Text(
			text = header,
			modifier = Modifier
				.padding(start = 4.dp)
				.bounceClickable(header2 != null) { onHeaderClick(1) },
			color = if (selectedHeader == 1) MaterialTheme.colorScheme.surfaceTint else Color.Unspecified,
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp
		)
		header2?.let {
			DotSeparator()
			Text(
				text = header2,
				modifier = Modifier.bounceClickable { onHeaderClick(2) },
				color = if (selectedHeader == 2) MaterialTheme.colorScheme.surfaceTint else Color.Unspecified,
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)
		}
	}
}

@Composable
fun DotSeparator() {
	Box(
		modifier = Modifier
			.padding(horizontal = 12.dp)
			.size(8.dp)
			.clip(CircleShape)
			.background(MaterialTheme.colorScheme.outline)
	)
}