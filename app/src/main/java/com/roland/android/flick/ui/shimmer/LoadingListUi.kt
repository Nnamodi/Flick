package com.roland.android.flick.ui.shimmer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.flick.ui.theme.FlickTheme

@Composable
fun LoadingListUi(
	scrollState: LazyGridState,
	paddingValues: PaddingValues,
	isLoading: Boolean
) {
	LazyVerticalGrid(
		columns = GridCells.Adaptive(120.dp),
		modifier = Modifier
			.padding(paddingValues)
			.padding(horizontal = 6.dp),
		state = scrollState,
		contentPadding = PaddingValues(bottom = 50.dp)
	) {
		itemsIndexed(
			items = (1..20).toList(),
			key = { _, item -> item }
		) { _, _ ->
			MediumBoxItem(
				isLoading = isLoading,
				modifier = Modifier.padding(6.dp)
			)
		}
	}
}

@Preview
@Composable
fun LoadingListUiPreview() {
	FlickTheme {
		LoadingListUi(
			scrollState = rememberLazyGridState(),
			paddingValues = PaddingValues(0.dp),
			isLoading = true
		)
	}
}