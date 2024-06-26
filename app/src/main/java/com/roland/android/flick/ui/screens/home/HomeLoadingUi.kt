package com.roland.android.flick.ui.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.Header
import com.roland.android.flick.ui.components.ToggleButton
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.NavigationBarHeight
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.LargeBoxItem
import com.roland.android.flick.utils.RowItems

@Composable
fun HomeLoadingUi(
	paddingValues: PaddingValues,
	isLoading: Boolean
) {
	Column(
		modifier = Modifier
			.padding(bottom = paddingValues.calculateBottomPadding())
			.verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Spacer(Modifier.height(paddingValues.calculateTopPadding()))
		ToggleButton("", Modifier.padding(bottom = 6.dp)) {}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(PADDING_WIDTH),
			horizontalArrangement = Arrangement.Start
		) {
			Header(stringResource(R.string.trending))
		}
		Row(
			modifier = Modifier
				.horizontalScroll(rememberScrollState())
				.padding(bottom = 16.dp)
		) {
			Spacer(Modifier.width(PADDING_WIDTH))
			repeat(10) {
				LargeBoxItem(isLoading, Modifier.padding(end = 14.dp))
			}
		}

		RowItems(stringResource(R.string.in_theatres), isLoading)

		RowItems(stringResource(R.string.top_rated), isLoading)

		RowItems(stringResource(R.string.anime), isLoading)

		RowItems(stringResource(R.string.bollywood), isLoading)

		RowItems(stringResource(R.string.most_popular), isLoading)

		Spacer(Modifier.height(50.dp + NavigationBarHeight))
	}
}

@Preview(showBackground = true)
@Composable
fun HomeLoadingUiPreview() {
	FlickTheme {
		HomeLoadingUi(
			paddingValues = PaddingValues(0.dp),
			isLoading = true
		)
	}
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun HomeLoadingLandscapeUiPreview() {
	HomeLoadingUiPreview()
}