package com.roland.android.flick.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.roland.android.flick.R
import com.roland.android.flick.ui.navigation.AppRoute

@Composable
fun BottomBar(navController: NavHostController) {
	NavigationBar(
		containerColor = NavigationBarDefaults.containerColor.copy(alpha = 0.9f)
	) {
		BottomBarItems.values().forEach { item ->
			val navBackStackEntry = navController.currentBackStackEntryAsState()
			val currentDestination = navBackStackEntry.value?.destination?.route
			val selected = currentDestination == item.route

			NavigationBarItem(
				selected = selected,
				onClick = {
					navController.navigate(item.route) {
						popUpTo(navController.graph.findStartDestination().id)
						launchSingleTop = true
					}

				},
				icon = { Icon(item.icon, null) },
				label = { Text(stringResource(item.title)) },
				alwaysShowLabel = selected
			)
		}
	}
}

enum class BottomBarItems(
	@StringRes val title: Int,
	val icon: ImageVector,
	val route: String
) {
	Home(
		title = R.string.home,
		icon = Icons.Rounded.Home,
		route = AppRoute.HomeScreen.route
	),
	ComingSoon(
		title = R.string.coming_soon,
		icon = Icons.Rounded.Upcoming,
		route = AppRoute.ComingSoonScreen.route
	)
}