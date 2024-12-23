package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.nexus.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DrawingBoardScreenStateSlice : CanvasViewModelSlice() {

    private val _screenState: MutableStateFlow<DrawingBoardScreenState> =
        MutableStateFlow(DrawingBoardScreenState(
            canvasState = Resource.Loading()
        ))
    val screenState: StateFlow<DrawingBoardScreenState> = _screenState

    override fun afterInit() {
        super.afterInit()
        sliceScope.launch {
            canvas.state.collectLatest {
                _screenState.value = DrawingBoardScreenState(
                    canvasState = Resource.Success(it)
                )
            }
        }
    }
}