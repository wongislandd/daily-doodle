package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.wongislandd.nexus.events.UiEvent

data class PathData(val offsets: List<Offset>, val color: Color)

class CanvasPathSlice : CanvasViewModelSlice() {

    override fun handleUiEvent(event: UiEvent) {
        if (event is DrawingAction) {
            when (event) {
                DrawingAction.OnNewPathStart -> onNewPathStart()
                is DrawingAction.OnDraw -> onDraw(event.offset)
                DrawingAction.OnNewPathEnd -> onNewPathEnd()
            }
        }
    }

    private fun onNewPathEnd() {
        val currentPath: PathData = canvas.state.value.pathState.currentPath ?: return
        val pathState: PathState = canvas.state.value.pathState
        val updatedPathState: PathState = pathState.copy(paths = pathState.paths + currentPath)
        canvas.updatePathState(updatedPathState)
    }

    private fun onDraw(offset: Offset) {
        val currentPathState = canvas.state.value.pathState
        val currentPath: PathData = currentPathState.currentPath ?: return
        canvas.updatePathState(
            currentPathState.copy(
                currentPath = currentPath.copy(
                    offsets = currentPath.offsets + offset
                )
            )
        )
    }

    private fun onNewPathStart() {
        val currentPathState = canvas.state.value.pathState
        canvas.updatePathState(
            currentPathState.copy(
                currentPath = PathData(
                    offsets = emptyList(),
                    color = canvas.state.value.settings.selectedColor
                )
            )
        )
    }
}