package com.roland.android.flick.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.roland.android.flick.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
	TopAppBar(
		title = {
			Text(
				text = stringResource(R.string.app_name),
				fontWeight = FontWeight.Bold
			)
		},
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
		)
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListTopBar(
	title: String,
	navigateUp: () -> Unit
) {
	TopAppBar(
		title = { Text(title) },
		navigationIcon = {
			IconButton(onClick = navigateUp) {
				Icon(Icons.Rounded.ArrowBackIos, stringResource(R.string.back))
			}
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsTopBar() {
	TopAppBar(
		title = {},
		colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)
	)
}