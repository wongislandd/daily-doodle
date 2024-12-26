package com.wongislandd.dailydoodle.explore

import com.wongislandd.dailydoodle.drawingboard.CanvasRepository
import com.wongislandd.dailydoodle.drawingboard.CanvasState
import com.wongislandd.dailydoodle.drawingboard.NetworkCanvasStateAdapter
import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.viewmodel.ViewModelSlice
import kotlinx.coroutines.launch

data class HistoryResolutionUpdate(val savedCanvasesRes: Resource<List<CanvasState>>): BackChannelEvent

class ExploreHistoryResolutionSlice(
    private val canvasRepository: CanvasRepository,
    private val networkCanvasStateAdapter: NetworkCanvasStateAdapter
) : ViewModelSlice() {

    override fun afterInit() {
        sliceScope.launch {
            val result = canvasRepository.getCanvas().map { savedCanvases ->
                savedCanvases.map {
                    networkCanvasStateAdapter.transform(it)
                }
            }
            backChannelEvents.sendEvent(HistoryResolutionUpdate(result))
        }
    }
}