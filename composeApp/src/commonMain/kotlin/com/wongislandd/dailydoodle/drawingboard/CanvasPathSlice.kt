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
                DrawingAction.OnUndo -> onUndo()
                DrawingAction.OnRedo -> onRedo()
            }
        }
    }

    private fun onNewPathEnd() {
        val currentPath: PathData = canvas.state.value.pathState.currentPath ?: return
        val newPaths = canvas.state.value.pathState.paths + currentPath
        val newPathState = canvas.state.value.pathState.copy(
            paths = newPaths,
            currentPath = null
        )
        canvas.updatePathState(newPathState)
        canvas.updateUndoStack(emptyList())
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

    private fun onUndo() {
        val currentPaths = canvas.state.value.pathState.paths
        if (currentPaths.isEmpty()) return
        val lastPath = currentPaths[currentPaths.lastIndex]
        val newPaths = currentPaths.dropLast(1)
        canvas.updateUndoStack(canvas.state.value.undoStack + lastPath)
        canvas.updatePathState(canvas.state.value.pathState.copy(paths = newPaths))
    }

    private fun onRedo() {
        val latestUndo = canvas.state.value.undoStack.lastOrNull() ?: return
        canvas.updateUndoStack(canvas.state.value.undoStack.dropLast(1))
        val pathState = canvas.state.value.pathState
        val updatedPathState = pathState.copy(paths = pathState.paths + latestUndo)
        canvas.updatePathState(updatedPathState)
    }
}