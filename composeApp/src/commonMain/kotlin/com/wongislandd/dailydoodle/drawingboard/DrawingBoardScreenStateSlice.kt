package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.dailydoodle.sharing.ShareService
import com.wongislandd.nexus.events.BackChannelEvent
import com.wongislandd.nexus.events.Event
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object DismissThicknessSelector : BackChannelEvent, UiEvent
object ShowThicknessSelector : BackChannelEvent, UiEvent
object DismissColorPicker : BackChannelEvent, UiEvent
object ShowColorPicker : BackChannelEvent, UiEvent

class DrawingBoardScreenStateSlice(shareService: ShareService) : CanvasViewModelSlice() {

    private val _screenState: MutableStateFlow<DrawingBoardScreenState> =
        MutableStateFlow(
            DrawingBoardScreenState(
                shareState = ShareState(
                    isShareEnabled = shareService.isShareEnabled()
                )
            )
        )
    val screenState: StateFlow<DrawingBoardScreenState> = _screenState

    override fun afterInit() {
        super.afterInit()
        sliceScope.launch {
            canvas.state.collectLatest { latestCanvasState ->
                _screenState.update {
                    it.copy(
                        canvasState = Resource.Success(latestCanvasState)
                    )
                }
            }
        }
    }

    override fun handleUiEvent(event: UiEvent) {
        super.handleUiEvent(event)
        handleGeneralEvent(event)
    }

    override fun handleBackChannelEvent(event: BackChannelEvent) {
        super.handleBackChannelEvent(event)
        handleGeneralEvent(event)
        when (event) {
            is ShareStateUpdate -> _screenState.update {
                it.copy(
                    shareState = event.shareState
                )
            }
        }
    }

    private fun handleGeneralEvent(event: Event) {
        when (event) {
            is DismissColorPicker -> _screenState.update {
                it.copy(isColorPickerShown = false)
            }

            is ShowColorPicker -> _screenState.update {
                it.copy(isColorPickerShown = true)
            }
            is DismissThicknessSelector -> _screenState.update {
                it.copy(isThicknessSelectorShown = false)
            }

            is ShowThicknessSelector -> _screenState.update {
                it.copy(isThicknessSelectorShown = true)
            }
        }
    }
}