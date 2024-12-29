package com.wongislandd.dailydoodle.explore

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.wongislandd.dailydoodle.drawingboard.drawPath
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.nexus.events.EventBus
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.util.toDp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
                    canvasPreviews = canvasState.data,
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
    canvasPreviews: List<CanvasPreview>,
    uiEventBus: EventBus<UiEvent>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 16.dp),
        modifier = modifier
    ) {
        items(canvasPreviews.size) { index ->
            val canvasState = canvasPreviews[index]
            CanvasPreview(canvasState)
        }
    }
}

@Composable
private fun CanvasPreview(
    canvasPreview: CanvasPreview,
    modifier: Modifier = Modifier
) {
    val widthDp = (canvasPreview.boundingBox.maxX - canvasPreview.boundingBox.minX).toDp()
    val heightDp = (canvasPreview.boundingBox.maxY - canvasPreview.boundingBox.minY).toDp()
    val publishTime = canvasPreview.canvasMetadata.date.toLocalDateTime(
        TimeZone.currentSystemDefault()
    ).date
    // Calculate translation based on the top-left of the bounding box
    val translationX = -canvasPreview.boundingBox.minX
    val translationY = -canvasPreview.boundingBox.minY
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(widthDp, heightDp).background(Color.White)) {
            withTransform({
                scale(0.5f, 0.5f)
                translate(translationX, translationY)
            }) {
                canvasPreview.paths.fastForEach { pathData ->
                    drawPath(
                        path = pathData.offsets,
                        color = pathData.color,
                        thickness = pathData.thickness
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = canvasPreview.canvasMetadata.title, style = MaterialTheme.typography.h6)
        Text(text = "Created on $publishTime", style = MaterialTheme.typography.body1)
    }
}