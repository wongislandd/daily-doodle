package com.wongislandd.dailydoodle.sharing

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toCValues
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGImageAlphaInfo
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
    override fun share(image: ImageBitmap) {
        Logger.i("ShareSHEET") { "Sharing image" }
        val uiImage = image.toUIImage()
        Logger.i("ShareSHEET") { "Converted ImageBitmap to UIImage" }
        // Save the UIImage to a temporary file
        val tempDir = NSTemporaryDirectory()
        val tempFile = NSString.stringWithString("$tempDir/shared_image.jpg")
        val imageData = uiImage.JPEGRepresentation(1.0) ?: return

        imageData.writeToFile(tempFile, true)

        // Create a share sheet
        val fileUrl = NSURL.fileURLWithPath(tempFile)
        val activityViewController = UIActivityViewController(
            activityItems = listOf(fileUrl),
            applicationActivities = null
        )

        // Present the share sheet
        val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootController?.presentViewController(activityViewController, animated = true, completion = null)
    }

    override fun isShareEnabled(): Boolean = true
}

// Extension function to convert ImageBitmap to UIImage
@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage {
    val pixelMap = this.toPixelMap()
    val width = pixelMap.width
    val height = pixelMap.height

    // Create a CGColorSpace
    val colorSpace = CGColorSpaceCreateDeviceRGB()

    // Prepare raw pixel data
    val pixelData = ByteArray(width * height * 4) // RGBA (4 bytes per pixel)
    var index = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = pixelMap[x, y]
            val components = color.toRgbaComponents()
            pixelData[index++] = (components.r * 255).toInt().toByte()
            pixelData[index++] = (components.g * 255).toInt().toByte()
            pixelData[index++] = (components.b * 255).toInt().toByte()
            pixelData[index++] = (components.a * 255).toInt().toByte()
        }
    }

    // Create a CGContext
    val context = CGBitmapContextCreate(
        data = pixelData.toCValues(),
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

    // Create a CGImage and wrap it in a UIImage
    val cgImage = CGBitmapContextCreateImage(context) ?: throw IllegalStateException("Unable to create CGImage")
    return UIImage.imageWithCGImage(cgImage)
}

// Extension function to convert Color to RGBA components
private fun androidx.compose.ui.graphics.Color.toRgbaComponents(): RgbaComponents {
    return RgbaComponents(this.red.toDouble(), this.green.toDouble(), this.blue.toDouble(), this.alpha.toDouble())
}

private data class RgbaComponents(val r: Double, val g: Double, val b: Double, val a: Double)

// Extension function to convert UIImage to NSData for JPEG Representation
fun UIImage.JPEGRepresentation(compressionQuality: CGFloat): NSData? {
    return UIImageJPEGRepresentation(this, compressionQuality)
}