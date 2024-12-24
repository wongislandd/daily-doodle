package com.wongislandd.dailydoodle.sharing

import androidx.compose.ui.graphics.ImageBitmap

actual class ShareServiceImpl actual constructor(
    appContext: Any?
) : ShareService {

    override suspend fun share(image: ImageBitmap) {

    }

    override fun isShareEnabled(): Boolean = false
}