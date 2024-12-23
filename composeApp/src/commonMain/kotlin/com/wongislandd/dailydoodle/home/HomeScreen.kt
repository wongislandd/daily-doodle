package com.wongislandd.dailydoodle.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wongislandd.dailydoodle.LocalAppViewModel
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.navigation.NavigationItemKey
import com.wongislandd.nexus.navigation.LocalNavHostController

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val navController = LocalNavHostController.current
    val appViewModel = LocalAppViewModel.current
    DailyDoodleTopAppBar(title = "Home") {
        Box(modifier = modifier.fillMaxSize()) {
            Button(onClick = {
                appViewModel.navigate(
                    navController,
                    NavigationItemKey.DRAWING_BOARD
                )
            }, modifier = Modifier.align(Alignment.Center)) {
                Text("Go to Drawing Board")
            }
        }
    }
}