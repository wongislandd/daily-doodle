package com.wongislandd.dailydoodle

import androidx.compose.ui.window.ComposeUIViewController
import com.wongislandd.nexus.theming.AppTheme

fun MainViewController() = ComposeUIViewController {
    AppTheme {
        App()
    }
}