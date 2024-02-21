package com.roland.android.flick.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.flick.R
import com.roland.android.flick.state.SearchUiState
import com.roland.android.flick.ui.navigation.Screens
import com.roland.android.flick.ui.screens.coming_soon.ComingSoonActions
import com.roland.android.flick.ui.screens.search.SearchActions
import com.roland.android.flick.utils.Constants.MOVIES

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navigate: (Screens) -> Unit) {
	TopAppBar(
		title = {
			Text(
				text = stringResource(R.string.app_name),
				fontWeight = FontWeight.Bold
			)
		},
		actions = {
			IconButton(onClick = { navigate(Screens.SearchScreen) }) {
				Icon(Icons.Rounded.Search, stringResource(R.string.search))
			}
			IconButton(onClick = { navigate(Screens.CategorySelectionScreen) }) {
				Icon(Icons.Rounded.Category, stringResource(R.string.select_categories))
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
			actionIconContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
		)
	)
}

@Composable
fun ComingSoonTopBar(
	selectedCategory: String,
	expanded: Boolean,
	action: (ComingSoonActions) -> Unit,
	minimize: () -> Unit
) {
	AnimatedContent(
		targetState = expanded,
		label = "top bar animation"
	) { itemExpanded ->
		if (itemExpanded) {
			MovieDetailsTopBar { minimize() }
		} else {
			MinimizedComingSoonTopBar(selectedCategory, action)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MinimizedComingSoonTopBar(
	selectedCategory: String,
	onCategorySelected: (ComingSoonActions) -> Unit
) {
	TopAppBar(
		title = {
			Text(
				text = stringResource(R.string.coming_soon),
				fontWeight = FontWeight.Bold
			)
		},
		actions = {
			val nextCategory = if (selectedCategory == MOVIES) Chips.TvShows else Chips.Movies

			ChipSet(
				modifier = Modifier.padding(end = 12.dp),
				selectedCategory = null,
				chips = arrayOf(nextCategory),
				onValueChanged = { onCategorySelected(ComingSoonActions.ToggleCategory) }
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
	)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CategorySelectionTopBar(
	clearButtonEnabled: Boolean,
	clearSelection: () -> Unit,
	closeSheet: () -> Unit
) {
	CenterAlignedTopAppBar(
		title = { Text(stringResource(R.string.select_categories)) },
		navigationIcon = {
			IconButton(onClick = closeSheet) {
				Icon(Icons.Rounded.Close, stringResource(R.string.close))
			}
		},
		actions = {
			TextButton(
				onClick = clearSelection,
				enabled = clearButtonEnabled
			) {
				Text(stringResource(R.string.clear))
			}
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListTopBar(
	title: String,
	categoryScreen: Boolean = false,
	openSelectionSheet: () -> Unit = {},
	navigateUp: (Screens) -> Unit
) {
	TopAppBar(
		title = { Text(title) },
		navigationIcon = {
			IconButton(onClick = { navigateUp(Screens.Back) }) {
				Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
			}
		},
		actions = {
			if (categoryScreen) {
				IconButton(onClick = openSelectionSheet) {
					Icon(Icons.Rounded.FilterList, stringResource(R.string.select_categories))
				}
			}
		}
	)
}

@OptIn(
	ExperimentalComposeUiApi::class,
	ExperimentalFoundationApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun SearchTopBar(
	uiState: SearchUiState,
	onSearch: (SearchActions) -> Unit,
	navigateUp: (Screens) -> Unit
) {
	var query by remember { mutableStateOf(uiState.searchQuery) }
	val keyboard = LocalSoftwareKeyboardController.current

	TopAppBar(
		title = {
			OutlinedTextField(
				value = query,
				onValueChange = { query = it },
				modifier = Modifier
					.fillMaxWidth()
					.height(52.dp),
				textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
				placeholder = {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(Icons.Rounded.Search, null, Modifier.padding(end = 4.dp))
						Text(
							text = stringResource(R.string.search),
							modifier = Modifier.basicMarquee()
						)
					}
				},
				trailingIcon = {
					if (query.isNotEmpty()) {
						IconButton(onClick = {
							query = ""
							keyboard?.show()
						}) {
							Icon(Icons.Rounded.Cancel, stringResource(R.string.clear))
						}
					}
				},
				keyboardOptions = KeyboardOptions(
					capitalization = KeyboardCapitalization.Sentences,
					imeAction = ImeAction.Search
				),
				keyboardActions = KeyboardActions(
					onSearch = {
						keyboard?.hide()
						if (query.isEmpty() || query == uiState.searchQuery) return@KeyboardActions
						onSearch(SearchActions.Search(query, uiState.searchCategory))
					}
				),
				singleLine = true,
				shape = MaterialTheme.shapes.large
			)
		},
		navigationIcon = {
			IconButton(onClick = { navigateUp(Screens.Back) }) {
				Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
			}
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsTopBar(navigateUp: (Screens) -> Unit) {
	TopAppBar(
		title = {},
		navigationIcon = {
			IconButton(onClick = { navigateUp(Screens.Back) }) {
				Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
			}
		},
		colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
	)
}