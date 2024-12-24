package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.UiEvent
import kotlinx.coroutines.launch

data class PathData(val offsets: List<Offset>, val color: Color)

data class ToggleRedoAvailability(val isAvailable: Boolean) : BackChannelEvent
data class ToggleUndoAvailability(val isAvailable: Boolean) : BackChannelEvent

// TODO Tie the availability of undo/redo tighter to its value (e.g. empty undo stack/path stack)
class CanvasPathSlice : CanvasViewModelSlice() {

    private val _undoStack = ArrayDeque<PathData>()

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
        if (newPaths.size == 1) {
            sliceScope.launch {
                backChannelEvents.sendEvent(ToggleUndoAvailability(true))
            }
        }
        val newPathState = canvas.state.value.pathState.copy(
            paths = newPaths,
            currentPath = null
        )
        canvas.updatePathState(newPathState)
        clearUndoStack()
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
        if (currentPaths.size == 1) {
            sliceScope.launch {
                backChannelEvents.sendEvent(ToggleUndoAvailability(false))
            }
        }
        if (currentPaths.isEmpty()) return
        val lastPath = currentPaths[currentPaths.lastIndex]
        val newPaths = currentPaths.dropLast(1)
        pushUndoStack(lastPath)
        canvas.updatePathState(canvas.state.value.pathState.copy(paths = newPaths))
    }

    private fun onRedo() {
        val pathToRedo = popUndoStack() ?: return
        val pathState = canvas.state.value.pathState
        val updatedPathState = pathState.copy(paths = pathState.paths + pathToRedo)
        canvas.updatePathState(updatedPathState)
        sliceScope.launch {
            backChannelEvents.sendEvent(ToggleUndoAvailability(true))
        }
    }

    private fun pushUndoStack(pathData: PathData) {
        _undoStack.add(pathData)
        if (_undoStack.size == 1) {
            sliceScope.launch {
                backChannelEvents.sendEvent(ToggleRedoAvailability(true))
            }
        }
    }

    private fun popUndoStack(): PathData? {
        val latestUndo = _undoStack.removeLastOrNull()
        if (_undoStack.isEmpty()) {
            sliceScope.launch {
                backChannelEvents.sendEvent(ToggleRedoAvailability(false))
            }
        }
        return latestUndo
    }

    private fun clearUndoStack() {
        _undoStack.clear()
        sliceScope.launch {
            backChannelEvents.sendEvent(ToggleRedoAvailability(false))
        }
    }
}