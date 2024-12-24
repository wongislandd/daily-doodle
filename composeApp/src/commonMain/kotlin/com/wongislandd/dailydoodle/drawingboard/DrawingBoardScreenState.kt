package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.nexus.util.Resource

data class ShareState(
    val isSharing: Boolean = false,
    val progress: Float = 0f,
    val isShareEnabled: Boolean = false,
)

data class DrawingBoardScreenState(
    val canvasState: Resource<CanvasState> = Resource.Loading(),
    val isColorPickerShown: Boolean = false,
    val isThicknessSelectorShown: Boolean = false,
    val shareState: ShareState = ShareState(),
)