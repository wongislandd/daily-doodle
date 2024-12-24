package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.graphics.Color
import com.wongislandd.nexus.events.UiEvent
import kotlinx.coroutines.launch

data class ColorSelected(val color: Color) : UiEvent

class ColorSlice : CanvasViewModelSlice() {

    override fun handleUiEvent(event: UiEvent) {
        super.handleUiEvent(event)
        when (event) {
            is ColorSelected -> {
                canvas.updateSelectedColor(color = event.color)
                sliceScope.launch {
                    uiEvents.sendEvent(DismissColorPicker)
                }
            }
        }
    }
}