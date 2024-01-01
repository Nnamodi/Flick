package com.roland.android.flick.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.roland.android.flick.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
	TopAppBar(
		title = {
			Text(
				text = stringResource(id = R.string.app_name),
				fontWeight = FontWeight.Bold
			)
		}
	)
}