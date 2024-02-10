package com.roland.android.flick.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.domain.entity.GenreList
import com.roland.android.domain.entity.Movie
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.models.SampleData.movie5
import com.roland.android.flick.ui.components.ItemBackdropPoster
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.coming_soon.ItemDetails
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.Constants.PADDING_WIDTH
import com.roland.android.flick.utils.PosterContainer
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsSheet(
	movie: Movie,
	genreList: GenreList,
	viewMore: (Screens) -> Unit,
	closeSheet: () -> Unit
) {
	val sheetState = rememberModalBottomSheetState(true)
	val screenHeight = LocalConfiguration.current.screenHeightDp
	val screenWidth = LocalConfiguration.current.screenWidthDp
	val windowSize = rememberWindowSize()
	val posterModifier = if (windowSize.width == WindowType.Portrait) {
		Modifier.size(screenWidth.dp, 210.dp)
	} else {
		Modifier.size((screenWidth / 2).dp, screenHeight.dp)
	}

	ModalBottomSheet(
		onDismissRequest = closeSheet,
		modifier = Modifier.padding(horizontal = 12.dp),
		sheetState = sheetState,
		shape = RoundedCornerShape(28.dp),
		dragHandle = {}
	) {
		PosterContainer(
			modifier = Modifier
				.clip(BottomSheetDefaults.ExpandedShape)
				.padding(12.dp)
				.heightIn(100.dp, (screenHeight / 1.5).dp)
		) {
			ItemBackdropPoster(
				movie = movie,
				modifier = posterModifier
					.clip(BottomSheetDefaults.ExpandedShape)
					.padding(end = if (windowSize.width == WindowType.Landscape) PADDING_WIDTH else 0.dp)
			)
			ItemDetails(
				movie = movie,
				genreList = genreList,
				inBottomSheet = true,
				viewMore = { closeSheet(); viewMore(it) }
			)
		}
	}
}

@Preview
@Composable
fun MovieDetailsSheetPreview() {
	FlickTheme {
		var movie by remember { mutableStateOf<Movie?>(null) }

		Column(
			Modifier
				.fillMaxSize()
				.clickable { movie = movie5 }
		) {
			if (movie != null) {
				MovieDetailsSheet(
					movie = movie!!,
					genreList = genreList,
					viewMore = {}
				) { movie = null }
			}
		}
	}
}