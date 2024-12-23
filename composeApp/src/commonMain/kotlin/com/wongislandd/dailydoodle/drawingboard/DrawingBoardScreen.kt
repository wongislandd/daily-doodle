package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar

@Composable
fun DrawingBoardScreen(modifier: Modifier = Modifier) {
    DailyDoodleTopAppBar(title = "Drawing Board") {
        Box(
            modifier = modifier.fillMaxSize()
                .background(color = MaterialTheme.colors.surface)
        ) {

        }
    }
}