package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class PathState(
    val paths: List<PathData> = emptyList(),
    val currentPath: PathData? = null
)

data class CanvasState(
    val selectedColor: Color = Color.Black,
    val pathState: PathState = PathState()
)

class Canvas {

    private val _state: MutableStateFlow<CanvasState> =
        MutableStateFlow(CanvasState())
    val state: StateFlow<CanvasState> = _state

    fun updateSelectedColor(color: Color) {
        _state.update {
            it.copy(selectedColor = color)
        }
    }

    fun updatePathState(pathState: PathState) {
        _state.update {
            it.copy(pathState = pathState)
        }
    }
}