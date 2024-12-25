package com.wongislandd.dailydoodle

data class NetworkCanvasState(
    val settings: NetworkCanvasSettings,
    val paths: List<NetworkPathData>,
    val undoStack: List<NetworkPathData>
)

data class NetworkCanvasSettings(
    val brushSettings: NetworkBrushSettings,
    val colorHistory: List<ULong>,
    val thicknessHistory: List<Float>
)

data class NetworkOffset(val x: Float, val y: Float)

data class NetworkPathData(val offsets: List<NetworkOffset>, val thickness: Float, val color: ULong)

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