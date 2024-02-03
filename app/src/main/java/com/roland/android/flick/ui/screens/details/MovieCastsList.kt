package com.roland.android.flick.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.Cast
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.CastPoster
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.bounceClickable

@Composable
fun MovieCastList(
	castList: List<Cast>,
	onCastClick: (Int) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp)
	) {
		Row(Modifier.padding(PADDING_WIDTH)) {
			Header(stringResource(R.string.casts))
		}
		LazyRow(
			contentPadding = PaddingValues(
				start = PADDING_WIDTH,
				end = PADDING_WIDTH - 12.dp
			)
		) {
			items(castList.size) { index ->
				MovieCast(
					cast = castList[index],
					modifier = Modifier.padding(end = 12.dp),
					onClick = onCastClick
				)
			}
		}
	}
}

@Composable
private fun MovieCast(
	cast: Cast,
	modifier: Modifier,
	onClick: (Int) -> Unit
) {
	Column(
		modifier = modifier.bounceClickable { onClick(cast.id) },
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		CastPoster(cast)
		Spacer(Modifier.height(8.dp))
		Text(cast.name)
		Spacer(Modifier.height(8.dp))
		Text(
			text = cast.character,
			modifier = Modifier.alpha(0.8f),
			fontWeight = FontWeight.Light
		)
	}
}