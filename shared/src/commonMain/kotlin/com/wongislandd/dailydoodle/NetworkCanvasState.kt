package com.wongislandd.dailydoodle

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCanvasState(
    val settings: NetworkCanvasSettings,
    val paths: List<NetworkPathData>,
    val undoStack: List<NetworkPathData>,
    val canvasMetadata: CanvasMetadata
)

@Serializable
data class NetworkCanvasSettings(
    val brushSettings: NetworkBrushSettings,
    val colorHistory: List<ULong>,
    val thicknessHistory: List<Float>
)

@Serializable
data class NetworkOffset(val x: Float, val y: Float)

@Serializable
data class NetworkPathData(val offsets: List<NetworkOffset>, val thickness: Float, val color: ULong)

@Serializable
data class NetworkBrushSettings(
    val selectedTool: DrawingUtencils,
    val selectedPencilThickness: Float,
    val selectedEraserThickness: Float,
    val selectedColor: ULong
)

enum class DrawingUtencils(val displayName: String, val defaultThickness: Float) {
    PENCIL("pencil", 10f),
    ERASER("eraser", 20f)
}

@Serializable
data class CanvasMetadata(
    val date: Instant = Clock.System.now(),
    val title: String = "Work of Art",
)