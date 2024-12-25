package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.nexus.events.UiEvent
import kotlinx.coroutines.launch

object SaveCanvas : UiEvent

class CanvasSavingSlice(
    private val canvasStateAdapter: CanvasStateAdapter,
    private val canvasRepository: CanvasRepository
) :
    CanvasViewModelSlice() {

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
            canvasRepository.saveCanvas(currentCanvas)
        }
    }
}