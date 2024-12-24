package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class PathState(
    val paths: List<PathData> = emptyList(),
    val currentPath: PathData? = null
)

data class CanvasSettings(
    val selectedColor: Color = Color(0xFF000000),
    val isUndoAvailable: Boolean = false,
    val isRedoAvailable: Boolean = false
)

data class CanvasState(
    val settings: CanvasSettings = CanvasSettings(),
    val pathState: PathState = PathState(),
    val undoStack: List<PathData> = emptyList()
)

class Canvas {

    private val _state: MutableStateFlow<CanvasState> =
        MutableStateFlow(CanvasState())
    val state: StateFlow<CanvasState> = _state

    fun updateSelectedColor(color: Color) {
        _state.update {
            it.copy(
                settings = it.settings.copy(
                    selectedColor = color
                )
            )
        }
    }

    fun updatePathState(pathState: PathState) {
        _state.update {
            val newCanvasState = it.copy(pathState = pathState)
            checkForUndoRedoAvailability(newCanvasState)
            newCanvasState
        }
    }

    fun updateUndoStack(undoStack: List<PathData>) {
        _state.update {
            val newCanvasState = it.copy(undoStack = undoStack)
            checkForUndoRedoAvailability(newCanvasState)
            newCanvasState
        }
    }

    private fun checkForUndoRedoAvailability(
        newCanvasState: CanvasState
    ) {
        val shouldRedoBeAvailable = newCanvasState.undoStack.isNotEmpty()
        val shouldUndoBeAvailable = newCanvasState.pathState.paths.isNotEmpty()
        _state.update {
            it.copy(settings = it.settings.copy(isRedoAvailable = shouldRedoBeAvailable))
        }
        _state.update {
            it.copy(settings = it.settings.copy(isUndoAvailable = shouldUndoBeAvailable))
        }
    }
}