package com.wongislandd.dailydoodle.sharing

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toCValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextFillRect
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextSetRGBFillColor
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.stringWithString
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

actual class ShareServiceImpl actual constructor(
    appContext: Any?
) : ShareService {
    override suspend fun share(image: ImageBitmap, shareProgressListener: ShareProgressListener) {
        shareProgressListener.onShareStart()
        // Convert the bitmap into a UIImage
        val uiImage = image.toUIImage(shareProgressListener)
        // Save the UIImage to a temporary file
        val tempDir = NSTemporaryDirectory()
        val tempFile = NSString.stringWithString("$tempDir/daily-doodle-share.jpg")
        val imageData = uiImage.JPEGRepresentation(1.0) ?: return

        imageData.writeToFile(tempFile, true)

        // Create a share sheet
        val fileUrl = NSURL.fileURLWithPath(tempFile)
        withContext(Dispatchers.Main) {
            val activityViewController = UIActivityViewController(
                activityItems = listOf(fileUrl),
                applicationActivities = null
            )
            // Present the share sheet
            val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootController?.presentViewController(
                activityViewController,
                animated = true,
                completion = null
            )
            shareProgressListener.onShareReady()
        }
    }

    override fun isShareEnabled(): Boolean = true
}

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(shareProgressListener: ShareProgressListener): UIImage {
    val pixelMap = this.toPixelMap()
    val width = pixelMap.width
    val height = pixelMap.height

    // Create a CGColorSpace
    val colorSpace = CGColorSpaceCreateDeviceRGB()

    // Create a CGContext
    val context = CGBitmapContextCreate(
        data = null,
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
    ) ?: throw IllegalStateException("Unable to create CGContext")

    // Flip the context vertically
    CGContextTranslateCTM(context, 0.0, height.toDouble())
    CGContextScaleCTM(context, 1.0, -1.0)

    // Draw the pixels into the CGContext
    for (y in 0 until height) {
        shareProgressListener.onProgress(y.toFloat() / height.toFloat())
        for (x in 0 until width) {
            val color = pixelMap[x, y]
            val components = color.toRgbaComponents()
            CGContextSetRGBFillColor(context, components.r, components.g, components.b, components.a)
            CGContextFillRect(context, CGRectMake(x.toDouble(), y.toDouble(), 1.0, 1.0))
        }
    }

    // Create a CGImage and wrap it in a UIImage
    val cgImage = CGBitmapContextCreateImage(context) ?: throw IllegalStateException("Unable to create CGImage")
    return UIImage.imageWithCGImage(cgImage)
}

// Extension function to convert Color to RGBA components
private fun androidx.compose.ui.graphics.Color.toRgbaComponents(): RgbaComponents {
    return RgbaComponents(
        this.red.toDouble(),
        this.green.toDouble(),
        this.blue.toDouble(),
        this.alpha.toDouble()
    )
}

private data class RgbaComponents(val r: Double, val g: Double, val b: Double, val a: Double)

// Extension function to convert UIImage to NSData for JPEG Representation
fun UIImage.JPEGRepresentation(compressionQuality: CGFloat): NSData? {
    return UIImageJPEGRepresentation(this, compressionQuality)
}