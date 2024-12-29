package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wongislandd.dailydoodle.CanvasMetadata
import com.wongislandd.dailydoodle.DrawingUtencils

data class CanvasState(
    val settings: CanvasSettings = CanvasSettings(),
    val canvasMetadata: CanvasMetadata = CanvasMetadata(),
    val pathState: PathState = PathState(),
    val undoStack: List<PathData> = emptyList()
)

data class PathState(
    val paths: List<PathData> = emptyList(),
    val currentPath: PathData? = null
)

data class PathData(val offsets: List<Offset>, val thickness: Dp = 10.dp, val color: Color)

data class BrushSettings(
    val selectedTool: DrawingUtencils = DrawingUtencils.PENCIL,
    val selectedPencilThickness: Dp = DrawingUtencils.PENCIL.defaultThickness.dp,
    val selectedEraserThickness: Dp = DrawingUtencils.ERASER.defaultThickness.dp,
    val selectedColor: Color = Color.Black
)

data class CanvasSettings(
    val brushSettings: BrushSettings = BrushSettings(),
    val colorHistory: List<Color> = emptyList(),
    val thicknessHistory: List<Dp> = emptyList(),
    val isUndoAvailable: Boolean = false,
    val isRedoAvailable: Boolean = false
)