package com.wongislandd.dailydoodle.explore

import com.wongislandd.dailydoodle.CanvasMetadata
import com.wongislandd.dailydoodle.drawingboard.CanvasRepository
import com.wongislandd.dailydoodle.drawingboard.NetworkCanvasStateAdapter
import com.wongislandd.dailydoodle.drawingboard.PathData
import com.wongislandd.dailydoodle.util.BoundingBox
import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.viewmodel.ViewModelSlice
import kotlinx.coroutines.launch

data class CanvasPreview(
    val paths: List<PathData>,
    val canvasMetadata: CanvasMetadata,
    val boundingBox: BoundingBox
)

data class HistoryResolutionUpdate(val savedCanvasesRes: Resource<List<CanvasPreview>>) :
    BackChannelEvent

class ExploreHistoryResolutionSlice(
    private val canvasRepository: CanvasRepository,
    private val canvasPreviewTransformer: CanvasPreviewTransformer,
    private val networkCanvasStateAdapter: NetworkCanvasStateAdapter
) : ViewModelSlice() {

    override fun afterInit() {
        sliceScope.launch {
            val result = canvasRepository.getCanvas().map { savedCanvases ->
                savedCanvases.map {
                    canvasPreviewTransformer.transform(networkCanvasStateAdapter.transform(it))
                }
            }
            backChannelEvents.sendEvent(HistoryResolutionUpdate(result))
        }
    }
}