package com.wongislandd.dailydoodle.sharing

import androidx.compose.ui.graphics.ImageBitmap

interface ShareService {
    suspend fun share(image: ImageBitmap)
    fun isShareEnabled(): Boolean
}

// not ideal lol
object ActivityReference {
    var current: Any? = null
}

expect class ShareServiceImpl(appContext: Any?) : ShareService