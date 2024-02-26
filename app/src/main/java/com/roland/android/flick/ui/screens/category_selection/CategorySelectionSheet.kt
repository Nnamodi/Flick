package com.roland.android.flick.ui.screens.category_selection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.domain.entity.Genre
import com.roland.android.domain.usecase.Collection
import com.roland.android.domain.usecase.Collection.MOVIES
import com.roland.android.domain.usecase.Collection.SERIES
import com.roland.android.flick.R
import com.roland.android.flick.models.SampleData.genreList
import com.roland.android.flick.ui.components.CategorySelectionTopBar
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.category_selection.CategorySelectionActions.LoadMovieList
import com.roland.android.flick.ui.theme.FlickTheme
import com.roland.android.flick.utils.bounceClickable

@Composable
fun CategorySelectionSheet(
	showSheet: Boolean,
	movieGenres: List<Genre>,
	seriesGenres: List<Genre>,
	selectedGenreIds: List<String>,
	selectedCollection: Collection,
	onCategoriesSelected: (CategorySelectionActions) -> Unit,
	closeSheet: () -> Unit,
	navigateUp: (Screens) -> Unit
) {
	AnimatedVisibility(
		visible = showSheet,
		enter = slideInVertically(tween(400)) { it },
		exit = slideOutVertically(tween(300)) { it },
	) {
		SelectionSheet(
			movieGenres = movieGenres,
			seriesGenres = seriesGenres,
			selectedGenreIds = selectedGenreIds,
			selectedCollection = selectedCollection,
			onCategoriesSelected = onCategoriesSelected,
			closeSheet = closeSheet,
			navigateUp = navigateUp
		)
	}

	if (showSheet) {
		BackHandler {
			if (selectedGenreIds.isEmpty()) {
				navigateUp(Screens.Back)
			} else closeSheet()
		}
	}
}

@Composable
private fun SelectionSheet(
	movieGenres: List<Genre>,
	seriesGenres: List<Genre>,
	selectedGenreIds: List<String>,
	selectedCollection: Collection,
	onCategoriesSelected: (CategorySelectionActions) -> Unit,
	closeSheet: () -> Unit,
	navigateUp: (Screens) -> Unit
) {
	var selectedGenres by rememberSaveable { mutableStateOf(selectedGenreIds) }

	Scaffold(
		topBar = {
			CategorySelectionTopBar(
				clearButtonEnabled = selectedGenres.isNotEmpty(),
				clearSelection = { selectedGenres = emptyList() },
				closeSheet = {
					if (selectedGenreIds.isEmpty()) {
						navigateUp(Screens.Back)
					} else closeSheet()
				}
			)
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			verticalArrangement = Arrangement.Bottom,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			var collectionName by rememberSaveable { mutableStateOf(selectedCollection.name) }
			val newCollection = Collection.valueOf(collectionName)
			val genres = if (newCollection == MOVIES) movieGenres else seriesGenres

			LazyColumn(
				modifier = Modifier
					.weight(1f)
					.fillMaxWidth(),
				contentPadding = PaddingValues(vertical = 50.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				items(genres.size) { index ->
					val genre = genres[index]
					val selected = genre.id in selectedGenres.map { it.toInt() }

					GenreItem(
						genre = genre,
						modifier = Modifier.padding(10.dp),
						selected = selected,
						onClick = {
							if (selected) selectedGenres -= it else selectedGenres += it
						}
					)
				}
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 6.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				CollectionTabs(
					selectedCollection = newCollection,
					modifier = Modifier.weight(1f),
					onCollectionSelected = { collectionName = it }
				)

				val newSelectedGenres = selectedGenres.filter { genreId ->
					genreId in genres.map { "${it.id}" }
				}
				val newCategoriesSelected = (selectedGenreIds != newSelectedGenres) || (selectedCollection != newCollection)
				Button(
					onClick = {
						val action = LoadMovieList(newSelectedGenres, newCollection)
						onCategoriesSelected(action); closeSheet()
					},
					modifier = Modifier.padding(vertical = 10.dp),
					enabled = newSelectedGenres.isNotEmpty() && newCategoriesSelected
				) {
					Text(stringResource(R.string.get_results), Modifier.padding(horizontal = 10.dp))
				}
			}
		}
	}
}

@Composable
private fun CollectionTabs(
	selectedCollection: Collection,
	modifier: Modifier,
	onCollectionSelected: (String) -> Unit
) {
	ScrollableTabRow(
		selectedTabIndex = if (selectedCollection == MOVIES) 0 else 1,
		modifier = modifier,
		edgePadding = 0.dp,
		divider = {}
	) {
		Collection.values().forEachIndexed { index, collection ->
			Tab(
				selected = collection == selectedCollection,
				onClick = {
					val newCollection = if (index == 0) MOVIES else SERIES
					onCollectionSelected(newCollection.name)
				},
				unselectedContentColor = MaterialTheme.colorScheme.outline,
				content = {
					val collectionName = if (collection == MOVIES) R.string.movies else R.string.series
					Text(stringResource(collectionName), Modifier.padding(vertical = 12.dp))
				}
			)
		}
	}
}

@Composable
private fun GenreItem(
	genre: Genre,
	modifier: Modifier = Modifier,
	selected: Boolean = true,
	onClick: (String) -> Unit = {}
) {
	Row(
		modifier = modifier
			.clip(MaterialTheme.shapes.extraLarge)
			.border(
				width = 1.dp,
				color = if (selected) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.outline,
				shape = MaterialTheme.shapes.extraLarge
			)
			.padding(8.dp, 6.dp)
			.bounceClickable { onClick("${genre.id}") }
			.animateContentSize(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = genre.name,
			color = if (selected) MaterialTheme.colorScheme.surfaceTint else Color.Unspecified,
			fontSize = 20.sp,
		)
		if (selected) {
			Icon(
				imageVector = Icons.Rounded.Done,
				contentDescription = stringResource(coil.compose.base.R.string.selected),
				tint = MaterialTheme.colorScheme.surfaceTint
			)
		}
	}
}

@Preview
@Composable
fun CategorySelectionSheetPreview() {
	FlickTheme(true) {
		SelectionSheet(
			movieGenres = genreList,
			seriesGenres = genreList,
			selectedGenreIds = listOf("16", "15", "24"),
			selectedCollection = MOVIES,
			onCategoriesSelected = {},
			closeSheet = {}
		) {}
	}
}