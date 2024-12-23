package com.wongislandd.dailydoodle.drawingboard

import com.wongislandd.nexus.viewmodel.ViewModelSlice

open class CanvasViewModelSlice : ViewModelSlice() {

    // Canvas can be nullable internally
    private var _canvas: Canvas? = null

    // Register a canvas to be used later
    fun provideCanvas(canvas: Canvas) {
        _canvas = canvas
    }

    // Custom getter for canvas
    val canvas: Canvas
        get() = _canvas ?: throw IllegalStateException("Canvas has not been registered yet!")
}