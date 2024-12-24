package com.wongislandd.dailydoodle.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wongislandd.dailydoodle.LocalAppViewModel
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.nexus.navigation.LocalNavHostController

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = LocalNavHostController.current
    val appViewModel = LocalAppViewModel.current
    DailyDoodleTopAppBar(title = "Home") {
        Box(modifier = modifier.fillMaxSize()) {
        }
    }
}