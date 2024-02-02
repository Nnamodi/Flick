package com.roland.android.flick.ui.screens.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.roland.android.flick.ui.components.ChipSet
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.MediumBoxItem
import com.roland.android.flick.utils.PosterContainer
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun LoadingListUi(
	scrollState: LazyGridState,
	paddingValues: PaddingValues,
	isLoading: Boolean,
	isSearchScreen: Boolean = false
) {
	val windowSize = rememberWindowSize()
	val dynamicGridSize = if (windowSize.width == WindowType.Landscape) 150.dp else 120.dp
	val chipModifier = if (windowSize.width == WindowType.Portrait) {
		Modifier.fillMaxWidth()
	} else Modifier.fillMaxHeight()

	PosterContainer(Modifier.padding(paddingValues)) {
		if (isSearchScreen) {
			ChipSet(
				modifier = chipModifier,
				selectedCategory = null,
				onValueChanged = {}
			)
		}

		LazyVerticalGrid(
			columns = GridCells.Adaptive(dynamicGridSize),
			modifier = Modifier.padding(horizontal = 6.dp),
			state = scrollState,
			contentPadding = PaddingValues(bottom = 78.dp)
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
}

@Preview
@Composable
fun LoadingListUiPreview() {
	FlickTheme(true) {
		LoadingListUi(
			scrollState = rememberLazyGridState(),
			paddingValues = PaddingValues(0.dp),
			isLoading = true,
			isSearchScreen = true
		)
	}
}