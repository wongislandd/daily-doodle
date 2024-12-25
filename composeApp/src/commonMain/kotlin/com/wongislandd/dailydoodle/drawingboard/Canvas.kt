package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.wongislandd.dailydoodle.DrawingUtencils
import com.wongislandd.nexus.util.addUniqueWithLimit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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