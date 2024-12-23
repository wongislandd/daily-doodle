package com.wongislandd.dailydoodle

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.wongislandd.dailydoodle.di.initializeKoin

fun main() = application {

    initializeKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "dailydoodle",
    ) {
        App()
    }
}