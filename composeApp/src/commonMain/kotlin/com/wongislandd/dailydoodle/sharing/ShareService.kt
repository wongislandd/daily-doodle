package com.wongislandd.dailydoodle.sharing

import androidx.compose.ui.graphics.ImageBitmap

interface ShareProgressListener {
    fun onShareStart()
    fun onProgress(progress: Float)
    fun onShareReady()
}

interface ShareService {
    suspend fun share(image: ImageBitmap, shareProgressListener: ShareProgressListener)
    fun isShareEnabled(): Boolean
}

// not ideal lol
object ActivityReference {
    var current: Any? = null
}

expect class ShareServiceImpl(appContext: Any?) : ShareService