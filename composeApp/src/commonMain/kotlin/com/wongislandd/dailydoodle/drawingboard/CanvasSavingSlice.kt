package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SaveCanvas : UiEvent

data class SaveStateUpdate(val saveState: SaveState) : BackChannelEvent

class CanvasSavingSlice(
    private val canvasStateAdapter: CanvasStateAdapter,
    private val canvasRepository: CanvasRepository
) : CanvasViewModelSlice() {

    override fun handleUiEvent(event: UiEvent) {
        super.handleUiEvent(event)
        if (event is SaveCanvas) {
            saveCanvas()
        }
    }

    private fun saveCanvas() {
        // Save the canvas
        val currentCanvas = canvasStateAdapter.transform(canvas.state.value)
        sliceScope.launch {
            backChannelEvents.sendEvent(SaveStateUpdate(SaveState.SAVING))
            val result = canvasRepository.saveCanvas(currentCanvas)
            when (result) {
                is Resource.Success -> {
                    backChannelEvents.sendEvent(SaveStateUpdate(SaveState.SAVED))
                    withContext(Dispatchers.Default) {
                        // Delay the save state to show the saved state for a while
                        kotlinx.coroutines.delay(5000)
                        backChannelEvents.sendEvent(SaveStateUpdate(SaveState.AVAILABLE))
                    }
                }
                is Resource.Loading -> {
                    backChannelEvents.sendEvent(SaveStateUpdate(SaveState.SAVING))
                }
                else -> {
                    backChannelEvents.sendEvent(SaveStateUpdate(SaveState.FAILED))
                    withContext(Dispatchers.Default) {
                        // Delay the save state to show the saved state for a while
                        kotlinx.coroutines.delay(5000)
                        backChannelEvents.sendEvent(SaveStateUpdate(SaveState.AVAILABLE))
                    }
                }
            }
        }
    }
}