package com.wongislandd.dailydoodle.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wongislandd.navigation.NavigationItemKey
import com.wongislandd.navigation.supportedNavigationItems
import com.wongislandd.nexus.navigation.GlobalTopAppBar

@Composable
fun DailyDoodleTopAppBar(
    title: String? = null,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit) = {},
    content: @Composable () -> Unit
) {
    Scaffold(topBar = {
        GlobalTopAppBar(
            title = title, homeDestination =
            supportedNavigationItems[NavigationItemKey.HOME]?.completeRoute ?: throw IllegalStateException("Home destination not found"),
            actions = actions
        )
    }, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}