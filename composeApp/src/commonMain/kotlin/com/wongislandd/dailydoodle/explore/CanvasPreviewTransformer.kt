package com.wongislandd.dailydoodle.explore

import com.wongislandd.dailydoodle.drawingboard.CanvasState
import com.wongislandd.dailydoodle.util.calculateBoundingBox
import com.wongislandd.nexus.util.Transformer

class CanvasPreviewTransformer : Transformer<CanvasState, CanvasPreview> {
    override fun transform(input: CanvasState): CanvasPreview {
        return CanvasPreview(
            paths = input.pathState.paths,
            canvasMetadata = input.canvasMetadata,
            boundingBox = input.pathState.calculateBoundingBox()
        )

    }
}