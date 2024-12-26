package com.wongislandd.dailydoodle.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wongislandd.dailydoodle.drawingboard.CanvasState
import com.wongislandd.dailydoodle.drawingboard.PathsCanvas
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.nexus.events.EventBus
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.util.Resource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ExploreScreen() {
    val viewModel = koinViewModel<ExploreViewModel>()
    val screenState by viewModel.exploreScreenStateSlice.exploreScreenState.collectAsState()
    DailyDoodleTopAppBar(title = "Explore") {
        when (val canvasState = screenState.history) {
            is Resource.Success -> {
                ExploreScreenContent(
                    savedCanvases = canvasState.data,
                    uiEventBus = viewModel.uiEventBus
                )
            }

            is Resource.Loading -> {
                // handle loading
            }

            is Resource.Error -> {
                // handle error
            }
        }
    }
}

@Composable
private fun ExploreScreenContent(
    savedCanvases: List<CanvasState>,
    uiEventBus: EventBus<UiEvent>,
    modifier: Modifier = Modifier
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        items(savedCanvases.size) { index ->
            val canvasState = savedCanvases[index]
            CanvasPreview(canvasState, uiEventBus)
        }
    }
}

@Composable
private fun CanvasPreview(
    canvasState: CanvasState,
    uiEventBus: EventBus<UiEvent>,
    modifier: Modifier = Modifier
) {
    PathsCanvas(
        pathState = canvasState.pathState,
        uiEventBus = uiEventBus,
        modifier = modifier.size(200.dp)
    )
}