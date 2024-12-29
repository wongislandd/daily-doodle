package com.wongislandd.dailydoodle.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    appViewModel.navigate(
                        navController,
                        NavigationItemKey.PROMPT_GENERATION
                    )
                }) {
                    Text("Go to Prompt Generation")
                }
                Button(onClick = {
                    appViewModel.navigate(
                        navController,
                        NavigationItemKey.DRAWING_BOARD
                    )
                }) {
                    Text("Go to Drawing Board")
                }
                Button(onClick = {
                    appViewModel.navigate(
                        navController,
                        NavigationItemKey.EXPLORE
                    )
                }) {
                    Text("Go to Explore")
                }
            }
        }

    }
}