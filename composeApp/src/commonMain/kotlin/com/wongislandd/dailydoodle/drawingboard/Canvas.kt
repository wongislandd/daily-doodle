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
    val selectedColor: Color = Color(0xFF000000)
)

data class CanvasState(
    val settings: CanvasSettings = CanvasSettings(),
    val pathState: PathState = PathState()
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
            it.copy(pathState = pathState)
        }
    }
}