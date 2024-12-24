package com.wongislandd.dailydoodle.sharing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

actual class ShareServiceImpl actual constructor(
    private val appContext: Any?
) : ShareService {

    override fun share(image: ImageBitmap) {
        val realContext = requireNotNull(appContext as Context) {
            "Context must be provided"
        }
        val androidBitMap = image.asAndroidBitmap()
        val file = File(realContext.cacheDir, "test")
        FileOutputStream(file).use { out ->
            androidBitMap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        shareImage(realContext, file)
    }

    private fun shareImage(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        // can't launch from application context
        (ActivityRef.current as? Activity)?.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
}