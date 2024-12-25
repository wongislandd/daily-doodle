package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.geometry.Offset
import com.wongislandd.nexus.events.UiEvent

sealed interface DrawingAction: UiEvent {
    data object OnNewPathStart: DrawingAction
    data class OnTap(val offset: Offset): DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnNewPathEnd: DrawingAction
    data object OnUndo: DrawingAction
    data object OnRedo: DrawingAction
}