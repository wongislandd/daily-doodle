package com.wongislandd.dailydoodle.sharing

import androidx.compose.ui.graphics.ImageBitmap

interface ShareService {
    fun share(image: ImageBitmap)
}

object ActivityRef {
    var current: Any? = null
}

expect class ShareServiceImpl(appContext: Any?) : ShareService