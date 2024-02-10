package com.roland.android.flick.ui.screens.details.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.flick.R
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.RowItems
import com.roland.android.flick.utils.shimmerModifier

@Composable
fun CastDetailsLoadingUi(isLoading: Boolean) {
	val screenHeight = LocalConfiguration.current.screenHeightDp

	Column(Modifier.heightIn(100.dp, (screenHeight - 80).dp)) {
		Row(
			modifier = Modifier
				.padding(horizontal = PADDING_WIDTH)
				.padding(top = PADDING_WIDTH),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(Modifier
				.size(140.dp, 190.dp)
				.clip(MaterialTheme.shapes.large)
				.shimmerModifier(isLoading)
			)
			Column(Modifier.padding(start = PADDING_WIDTH)) {
				Spacer(Modifier
					.padding(bottom = 10.dp)
					.size(130.dp, 18.dp)
					.shimmerModifier(isLoading)
				)
				Spacer(Modifier
					.size(100.dp, 14.dp)
					.shimmerModifier(isLoading)
				)
			}
		}
		Spacer(Modifier
			.padding(start = PADDING_WIDTH, top = 14.dp, bottom = 12.dp)
			.size(100.dp, 18.dp)
			.shimmerModifier(isLoading)
		)
		repeat(2) {
			Spacer(Modifier
				.fillMaxWidth()
				.padding(horizontal = PADDING_WIDTH, vertical = 2.dp)
				.height(16.dp)
				.shimmerModifier(isLoading)
			)
		}
		RowItems(stringResource(R.string.filmography), isLoading)
	}
}