package com.wongislandd.dailydoodle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wongislandd.dailydoodle.drawingboard.DrawingBoardScreen
import com.wongislandd.dailydoodle.home.HomeScreen
import com.wongislandd.navigation.NavigationItemKey
import com.wongislandd.navigation.supportedNavigationItems
import com.wongislandd.nexus.navigation.LocalNavHostController

@Composable
fun DailyDoodleAppNavHost(
    modifier: Modifier = Modifier,
    startDestination: NavigationItemKey = NavigationItemKey.HOME
) {
    val navController = LocalNavHostController.current
    val startingDestination = supportedNavigationItems[startDestination]
            ?: throw IllegalStateException("Couldn't find registered start destination!")
    NavHost(
        navController = navController,
        startDestination = startingDestination.completeRoute,
        modifier = modifier
    ) {
        supportedNavigationItems.map { (_, navigationItem) ->
            when (NavigationItemKey.valueOf(navigationItem.navigationKey)) {
                NavigationItemKey.HOME -> {
                    composable(route = navigationItem.completeRoute) {
                        HomeScreen()
                    }
                }

                NavigationItemKey.DRAWING_BOARD -> {
                    composable(route = navigationItem.completeRoute) {
                        DrawingBoardScreen()
                    }
                }
            }
        }
    }
}