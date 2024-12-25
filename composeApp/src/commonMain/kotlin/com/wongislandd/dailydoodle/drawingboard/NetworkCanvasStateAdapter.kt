package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wongislandd.dailydoodle.NetworkBrushSettings
import com.wongislandd.dailydoodle.NetworkCanvasSettings
import com.wongislandd.dailydoodle.NetworkCanvasState
import com.wongislandd.dailydoodle.NetworkPathData
import com.wongislandd.nexus.util.Transformer

class NetworkCanvasStateAdapter : Transformer<NetworkCanvasState, CanvasState> {
    override fun transform(input: NetworkCanvasState): CanvasState {
        val canvasSettings = input.settings.toCanvasSettings()
        val paths = input.paths.toPathDataList()
        val undoStack = input.undoStack.toPathDataList()
        return CanvasState(
            settings = canvasSettings,
            pathState = PathState(paths = paths),
            undoStack = undoStack
        )
    }
}

private fun NetworkCanvasSettings.toCanvasSettings(): CanvasSettings {
    return CanvasSettings(
        brushSettings = brushSettings.toBrushSettings(),
        colorHistory = colorHistory.map { Color(it) },
        thicknessHistory = thicknessHistory.map { it.dp }
    )
}

private fun NetworkBrushSettings.toBrushSettings(): BrushSettings {
    return BrushSettings(
        selectedTool = selectedTool,
        selectedPencilThickness = selectedPencilThickness.dp,
        selectedEraserThickness = selectedEraserThickness.dp,
        selectedColor = Color(selectedColor)
    )
}

private fun List<NetworkPathData>.toPathDataList(): List<PathData> {
    return map { it.toPathData() }
}

private fun NetworkPathData.toPathData(): PathData {
    return PathData(
        offsets = offsets.map { Offset(it.x, it.y) },
        thickness = thickness.dp,
        color = Color(color)
    )
}
