package com.wongislandd.dailydoodle

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> {
    error("No AppViewModel provided")
}