package com.wongislandd.dailydoodle

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wongislandd.dailydoodle.drawingboard.DrawingBoardScreen
import com.wongislandd.dailydoodle.explore.ExploreScreen
import com.wongislandd.dailydoodle.home.HomeScreen
import com.wongislandd.dailydoodle.prompting.PromptScreen
import com.wongislandd.navigation.NavigationItemKey
import com.wongislandd.navigation.PROMPT_ARG
import com.wongislandd.navigation.supportedNavigationItems
import com.wongislandd.nexus.navigation.LocalNavHostController

@Composable
fun DailyDoodleAppNavHost(
    modifier: Modifier = Modifier,
    startDestination: NavigationItemKey = NavigationItemKey.DRAWING_BOARD
) {
    val navController = LocalNavHostController.current
    val startingDestination = supportedNavigationItems[startDestination]
        ?: throw IllegalStateException("Couldn't find registered start destination!")
    val pageTurnEnterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(700)
    )

    val pageTurnExitTransition = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(700)
    )

    val pageReturnEnterTransition = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(700)
    )

    val pageReturnExitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(700)
    )

    NavHost(
        navController = navController,
        startDestination = startingDestination.completeRoute,
        enterTransition = {
            pageTurnEnterTransition
        },
        exitTransition = {
            pageTurnExitTransition
        },
        popEnterTransition = {
            pageReturnEnterTransition
        },
        popExitTransition = {
            pageReturnExitTransition
        },
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
                    composable(route = navigationItem.completeRoute,
                        arguments = listOf(
                            navArgument(PROMPT_ARG) { type = NavType.StringType; nullable = true }
                        )) {
                        val prompt = it.arguments?.getString(PROMPT_ARG)
                        DrawingBoardScreen(prompt = prompt)
                    }
                }

                NavigationItemKey.EXPLORE -> {
                    composable(route = navigationItem.completeRoute) {
                        ExploreScreen()
                    }
                }

                NavigationItemKey.PROMPT_GENERATION -> {
                    composable(route = navigationItem.completeRoute) {
                        PromptScreen()
                    }
                }
            }
        }
    }
}