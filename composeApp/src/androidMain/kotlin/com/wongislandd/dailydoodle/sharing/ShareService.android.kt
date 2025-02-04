package com.wongislandd.dailydoodle.sharing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

actual class ShareServiceImpl actual constructor(
    private val appContext: Any?
) : ShareService {

    override suspend fun share(image: ImageBitmap, shareProgressListener: ShareProgressListener) {
        shareProgressListener.onShareStart()
        val realContext = requireNotNull(appContext as Context) {
            "Context must be provided"
        }
        val androidBitMap = image.asAndroidBitmap()
        shareProgressListener.onProgress(.5f)
        val file = File(realContext.cacheDir, "daily-doodle-export.png")
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { out ->
                androidBitMap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        }
        shareImage(realContext, file)
        shareProgressListener.onShareReady()
    }

    override fun isShareEnabled(): Boolean = true

    private fun shareImage(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setDataAndType(uri, "image/png")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // can't launch from application context
        (ActivityReference.current as? Activity)?.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
}