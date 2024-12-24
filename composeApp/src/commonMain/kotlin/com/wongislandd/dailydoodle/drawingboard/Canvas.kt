package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wongislandd.nexus.util.addUniqueWithLimit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

enum class DrawingUtencils(val defaultThickness: Dp) {
    PENCIL(10.dp),
    ERASER(20.dp)
}

data class PathState(
    val paths: List<PathData> = emptyList(),
    val currentPath: PathData? = null
)

data class BrushSettings(
    val selectedTool: DrawingUtencils = DrawingUtencils.PENCIL,
    val selectedPencilThickness: Dp = DrawingUtencils.PENCIL.defaultThickness,
    val selectedEraserThickness: Dp = DrawingUtencils.ERASER.defaultThickness,
    val selectedColor: Color = Color.Black
)

data class CanvasSettings(
    val brushSettings: BrushSettings = BrushSettings(),
    val colorHistory: List<Color> = emptyList(),
    val thicknessHistory: List<Dp> = emptyList(),
    val isUndoAvailable: Boolean = false,
    val isRedoAvailable: Boolean = false
)

fun CanvasSettings.getBrushColor(): Color {
    return if (brushSettings.selectedTool == DrawingUtencils.ERASER) {
        Color.White
    } else brushSettings.selectedColor
}

fun BrushSettings.getThickness(): Dp {
    return when (selectedTool) {
        DrawingUtencils.PENCIL -> selectedPencilThickness
        DrawingUtencils.ERASER -> selectedEraserThickness
    }
}

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
            val mutableColorHistory = it.settings.colorHistory.toMutableList()
            val newColorHistory = mutableColorHistory.addUniqueWithLimit(color, 10)
            it.copy(
                settings = it.settings.copy(
                    brushSettings = it.settings.brushSettings.copy(
                        selectedColor = color
                    ),
                    colorHistory = newColorHistory,
                )
            )
        }
    }

    fun updateSelectedTool(tool: DrawingUtencils) {
        _state.update {
            it.copy(
                settings = it.settings.copy(
                    brushSettings = it.settings.brushSettings.copy(
                        selectedTool = tool
                    )
                )
            )
        }
    }

    fun updateThickness(thickness: Dp) {
        _state.update {
            if (it.settings.brushSettings.selectedTool == DrawingUtencils.PENCIL) {
                val mutableThicknessHistory = it.settings.thicknessHistory.toMutableList()
                val newThicknessHistory = mutableThicknessHistory.addUniqueWithLimit(thickness, 5)
                it.copy(
                    settings = it.settings.copy(
                        brushSettings = it.settings.brushSettings.copy(
                            selectedPencilThickness = thickness
                        ),
                        thicknessHistory = newThicknessHistory
                    )
                )
            } else {
                it.copy(
                    settings = it.settings.copy(
                        brushSettings = it.settings.brushSettings.copy(
                            selectedEraserThickness = thickness
                        )
                    )
                )
            }
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