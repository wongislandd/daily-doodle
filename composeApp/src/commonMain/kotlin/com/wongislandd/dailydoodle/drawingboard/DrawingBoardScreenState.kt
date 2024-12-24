package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.nexus.util.Resource

data class DrawingBoardScreenState(
    val canvasState: Resource<CanvasState> = Resource.Loading(),
    val isColorPickerShown: Boolean = false,
    val isUndoAvailable: Boolean = false,
    val isRedoAvailable: Boolean = false
)