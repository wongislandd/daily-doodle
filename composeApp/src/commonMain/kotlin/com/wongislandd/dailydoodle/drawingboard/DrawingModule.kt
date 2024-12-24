package com.wongislandd.dailydoodle.drawingboard

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val drawingModule = module {
    viewModelOf(::DrawingBoardViewModel)
    factoryOf(::Canvas)
    factoryOf(::CanvasPathSlice)
    factoryOf(::DrawingBoardScreenStateSlice)
    factoryOf(::BrushControllerSlice)
    factoryOf(::ExportSlice)
}