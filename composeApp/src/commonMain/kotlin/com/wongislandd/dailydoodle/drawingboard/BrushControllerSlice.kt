package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.wongislandd.dailydoodle.DrawingUtencils
import com.wongislandd.nexus.events.UiEvent
import kotlinx.coroutines.launch

data class ColorSelected(val color: Color) : UiEvent

data class ToolSelected(val tool: DrawingUtencils) : UiEvent

data class ThicknessSelected(val thickness: Dp) : UiEvent

class BrushControllerSlice : CanvasViewModelSlice() {

    override fun handleUiEvent(event: UiEvent) {
        super.handleUiEvent(event)
        when (event) {
            is ColorSelected -> {
                canvas.updateSelectedColor(color = event.color)
                sliceScope.launch {
                    uiEvents.sendEvent(DismissColorPicker)
                }
            }
            is ToolSelected -> {
                canvas.updateSelectedTool(tool = event.tool)
            }
            is ThicknessSelected -> {
                canvas.updateThickness(thickness = event.thickness)
            }
        }
    }
}