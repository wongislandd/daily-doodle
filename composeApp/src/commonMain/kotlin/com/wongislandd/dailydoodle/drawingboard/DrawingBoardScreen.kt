package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.nexus.util.Resource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun DrawingBoardScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<DrawingBoardViewModel>()
    val screenState by viewModel.drawingBoardScreenStateSlice.screenState.collectAsState()
    DailyDoodleTopAppBar(title = "Drawing Board") {
        val canvasState = screenState.canvasState
        when (canvasState) {
            is Resource.Success -> {
                PathsCanvas(canvasState.data.pathState, viewModel.uiEventBus, modifier = modifier)
            }

            is Resource.Error -> {
                // Handle error
            }

            is Resource.Loading -> {
                // Handle loading
            }
        }
    }
}