package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.dailydoodle.NetworkBrushSettings
import com.wongislandd.dailydoodle.NetworkCanvasSettings
import com.wongislandd.dailydoodle.NetworkCanvasState
import com.wongislandd.dailydoodle.NetworkOffset
import com.wongislandd.dailydoodle.NetworkPathData
import com.wongislandd.nexus.util.Transformer

/**
 * Keep this in sync with the [NetworkCanvasStateAdapter]
 */
class CanvasStateAdapter : Transformer<CanvasState, NetworkCanvasState> {
    override fun transform(input: CanvasState): NetworkCanvasState {
        val canvasSettings = input.settings.toNetworkCanvasSettings()
        val paths = input.pathState.paths.toNetworkPathDataList()
        val undoStack = input.undoStack.toNetworkPathDataList()
        return NetworkCanvasState(
            settings = canvasSettings,
            paths = paths,
            undoStack = undoStack,
            canvasMetadata = input.canvasMetadata
        )
    }

    private fun CanvasSettings.toNetworkCanvasSettings(): NetworkCanvasSettings {
        return NetworkCanvasSettings(
            brushSettings = brushSettings.toNetworkBrushSettings(),
            colorHistory = colorHistory.map { it.value },
            thicknessHistory = thicknessHistory.map { it.value }
        )
    }

    private fun BrushSettings.toNetworkBrushSettings(): NetworkBrushSettings {
        return NetworkBrushSettings(
            selectedTool = selectedTool,
            selectedPencilThickness = selectedPencilThickness.value,
            selectedEraserThickness = selectedEraserThickness.value,
            selectedColor = selectedColor.value
        )
    }

    private fun List<PathData>.toNetworkPathDataList(): List<NetworkPathData> {
        return map { it.toNetworkPathData() }
    }

    private fun PathData.toNetworkPathData(): NetworkPathData {
        return NetworkPathData(
            offsets = offsets.map { NetworkOffset(it.x, it.y) },
            thickness = thickness.value,
            color = color.value
        )
    }
}