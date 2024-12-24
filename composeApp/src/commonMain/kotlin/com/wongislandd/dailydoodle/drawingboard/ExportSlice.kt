package com.wongislandd.dailydoodle.drawingboard

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastForEach
import com.wongislandd.dailydoodle.sharing.ShareService
import com.wongislandd.nexus.events.UiEvent

data class ImageExported(val size: Size): UiEvent

class ExportSlice(private val shareService: ShareService): CanvasViewModelSlice() {

    override fun handleUiEvent(event: UiEvent) {
        super.handleUiEvent(event)
        if (event is ImageExported) {
            exportCanvas(event.size)
            println("Exporting image of ${event.size}")
        }
    }

    private fun exportCanvas(size: Size) {
        val drawScope = CanvasDrawScope()
        val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
        val graphicsCanvas = androidx.compose.ui.graphics.Canvas(bitmap)
        val pathState = canvas.state.value.pathState
        drawScope.draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = graphicsCanvas,
            size = size
        ) {
            // Draw existing paths
            pathState.paths.fastForEach { pathData ->
                drawPath(
                    path = pathData.offsets,
                    color = pathData.color,
                    thickness = pathData.thickness
                )
            }
        }
        println(bitmap)
        shareService.share(bitmap)
    }
}