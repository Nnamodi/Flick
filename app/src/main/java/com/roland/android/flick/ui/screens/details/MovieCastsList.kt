package com.roland.android.flick.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
		modifier = modifier
			.width(120.dp)
			.bounceClickable { onClick(cast.id) },
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		CastPoster(cast)
		Text(
			text = cast.name,
			modifier = Modifier.padding(vertical = 8.dp),
			overflow = TextOverflow.Ellipsis,
			softWrap = false
		)
		Text(
			text = cast.character,
			modifier = Modifier.alpha(0.8f),
			fontSize = 14.sp,
			fontWeight = FontWeight.Light,
			overflow = TextOverflow.Ellipsis,
			softWrap = false
		)
	}
}