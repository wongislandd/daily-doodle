package com.wongislandd.dailydoodle.util

import com.wongislandd.dailydoodle.drawingboard.PathState
import kotlin.math.max
import kotlin.math.min


// Function to calculate the bounding box for a PathState
fun PathState.calculateBoundingBox(): BoundingBox {
    // Initialize min and max values to extreme limits
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    // Iterate over all paths and calculate the bounding box
    paths.forEach { pathData ->
        pathData.offsets.forEach { offset ->
            minX = min(minX, offset.x)
            minY = min(minY, offset.y)
            maxX = max(maxX, offset.x)
            maxY = max(maxY, offset.y)
        }
    }

    // If the path list is empty, we can assume a default bounding box
    if (minX == Float.MAX_VALUE || minY == Float.MAX_VALUE || maxX == Float.MIN_VALUE || maxY == Float.MIN_VALUE) {
        return BoundingBox(0f, 0f, 0f, 0f)
    }

    return BoundingBox(minX, minY, maxX, maxY)
}

// Bounding box data class
data class BoundingBox(val minX: Float, val minY: Float, val maxX: Float, val maxY: Float)
