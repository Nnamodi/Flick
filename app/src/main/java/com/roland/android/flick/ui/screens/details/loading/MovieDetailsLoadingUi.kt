package com.roland.android.flick.ui.screens.details.loading

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.DotSeparator
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.CircleItem
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.RowItems
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize
import com.roland.android.flick.utils.shimmerModifier

@Composable
fun MovieDetailsLoadingUi(
	scrollState: ScrollState,
	isLoading: Boolean,
	navigateUp: (Screens) -> Unit
) {
	val screenHeight = LocalConfiguration.current.screenWidthDp.dp
	val windowSize = rememberWindowSize()
	val inPortraitMode by remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}
	val screenModifier = if (!inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier
	val columnModifier = if (inPortraitMode) Modifier.verticalScroll(scrollState) else Modifier
	val videoHeightDivisor = if (inPortraitMode) 0.6f else 0.5f

	Column(screenModifier) {
		Box(Modifier
			.height(screenHeight * videoHeightDivisor)
			.fillMaxWidth()
			.shimmerModifier(isLoading)
		) {
			IconButton(
				onClick = { navigateUp(Screens.Back) },
				modifier = Modifier.padding(start = 2.dp, top = 46.dp)
			) {
				Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
			}
		}

		Column(columnModifier) {
			Column(Modifier.padding(horizontal = PADDING_WIDTH)) {
				Spacer(Modifier
					.padding(vertical = 10.dp)
					.size(200.dp, 22.dp)
					.shimmerModifier(isLoading)
				)
				Row(verticalAlignment = Alignment.CenterVertically) {
					Spacer(Modifier
						.size(20.dp, 14.dp)
						.shimmerModifier(isLoading)
					)
					DotSeparator()
					Spacer(Modifier
						.padding(vertical = 6.dp)
						.size(60.dp, 26.dp)
						.shimmerModifier(isLoading)
					)
				}
				Spacer(Modifier
					.size(150.dp, 14.dp)
					.shimmerModifier(isLoading)
				)
				Spacer(Modifier.height(14.dp))
				repeat(4) {
					Spacer(Modifier
						.padding(vertical = 2.dp)
						.fillMaxWidth()
						.height(16.dp)
						.shimmerModifier(isLoading)
					)
				}
				Spacer(Modifier.height(14.dp))
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 12.dp),
				horizontalArrangement = Arrangement.SpaceAround
			) {
				repeat(3) {
					Column(
						modifier = Modifier.padding(horizontal = 10.dp),
						verticalArrangement = Arrangement.spacedBy(4.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Box(Modifier
							.size(24.dp)
							.clip(CircleShape)
							.shimmerModifier(isLoading)
						)
						Spacer(Modifier
							.size(55.dp, 16.dp)
							.shimmerModifier(isLoading)
						)
					}
				}
			}
			RowItems(
				header = stringResource(R.string.top_casts),
				isLoading = isLoading,
				content = { CircleItem(isLoading) }
			)
			RowItems(stringResource(R.string.recommended), isLoading)
			Spacer(Modifier.height(50.dp))
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsLoadingUiPreview() {
	FlickTheme(true) {
		Surface {
			MovieDetailsLoadingUi(rememberScrollState(), true) {}
		}
	}
}