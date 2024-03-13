package com.roland.android.flick.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailDefaults.ContainerColor
import androidx.compose.material3.NavigationRailDefaults.windowInsets
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.roland.android.flick.R
import com.roland.android.flick.ui.components.NavBarItems.ComingSoon
import com.roland.android.flick.ui.components.NavBarItems.Home
import com.roland.android.flick.ui.navigation.AppRoute
import com.roland.android.flick.utils.Constants.NavigationRailWidth
import com.roland.android.flick.utils.WindowType
import com.roland.android.flick.utils.rememberWindowSize

@Composable
fun NavBar(
	inFullScreen: Boolean,
	navController: NavHostController
) {
	val windowSize = rememberWindowSize()
	val navBackStackEntry = navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry.value?.destination?.route
	val currentlyInStartScreen = NavBarItems.values().any {
		currentDestination == it.route
	}
	val inPortraitMode by remember(windowSize.width) {
		derivedStateOf { windowSize.width == WindowType.Portrait }
	}
	val showBottomBar by remember(
		currentlyInStartScreen,
		inFullScreen,
		currentDestination
	) {
		derivedStateOf {
			inPortraitMode && currentlyInStartScreen &&
					(!inFullScreen || (currentDestination == Home.route))
		}
	}

	AnimatedVisibility(
		visible = if (inPortraitMode) showBottomBar else true,
		enter = slideInVertically(
			animationSpec = tween(durationMillis = 350, delayMillis = 1000),
			initialOffsetY = { it }
		),
		exit = ExitTransition.None
	) {
		when {
			!inPortraitMode -> SideNavRail(navController)
			showBottomBar -> BottomNavBar(navController)
		}
	}
}

@Composable
fun SideNavRail(navController: NavHostController) {
	Surface(
		color = ContainerColor,
		contentColor = contentColorFor(ContainerColor)
	) {
		Column(
			modifier = Modifier
				.fillMaxHeight()
				.windowInsetsPadding(windowInsets)
				.widthIn(min = NavigationRailWidth)
				.padding(vertical = 4.dp)
				.selectableGroup(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			val navBars = NavBarItems.values()
			navBars.forEach { item ->
				val backStackEntries = navController.currentBackStack.value.map { it.destination.route }
				val destinationInBackStack = item.route in backStackEntries
				val backStackState = navController.currentBackStackEntryAsState()
				val currentDestination = backStackState.value?.destination?.route
				val selected = when (item) {
					Home -> destinationInBackStack && ComingSoon.route !in backStackEntries
					ComingSoon -> destinationInBackStack
				}
				val itemIsNotLast = item != navBars.last()

				NavigationRailItem(
					selected = selected,
					onClick = {
						val shouldSaveState = when {
							ComingSoon.route in backStackEntries && item.route != ComingSoon.route-> true
							destinationInBackStack && item == Home -> false
							currentDestination == item.route -> false
							else -> true
						}
						navController.navigate(item.route) {
							popUpTo(navController.graph.findStartDestination().id) {
								saveState = shouldSaveState
							}
							launchSingleTop = true
							restoreState = true
						}
					},
					icon = { Icon(item.icon, null) },
					modifier = Modifier.padding(bottom = if (itemIsNotLast) 4.dp else 0.dp),
					label = { Text(stringResource(item.title)) },
					alwaysShowLabel = true
				)
			}
		}
	}
}

@Composable
private fun BottomNavBar(navController: NavHostController) {
	NavigationBar(
		containerColor = NavigationBarDefaults.containerColor.copy(alpha = 0.9f)
	) {
		NavBarItems.values().forEach { item ->
			val navBackStackEntry = navController.currentBackStackEntryAsState()
			val currentDestination = navBackStackEntry.value?.destination?.route
			val selected = currentDestination == item.route

			NavigationBarItem(
				selected = selected,
				onClick = {
					navController.navigate(item.route) {
						popUpTo(navController.graph.findStartDestination().id) {
							saveState = item.route != currentDestination
						}
						launchSingleTop = true
						restoreState = true
					}
				},
				icon = { Icon(item.icon, null) },
				label = { Text(stringResource(item.title)) },
				alwaysShowLabel = true
			)
		}
	}
}

private enum class NavBarItems(
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