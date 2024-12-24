package com.wongislandd.nexus.util

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.conditionallyChain(condition: Boolean, modifier: Modifier): Modifier {
    return if (condition) this.then(modifier) else this
}

fun Modifier.noIndicationClickable(onClick: () -> Unit = {}) : Modifier {
    return this.clickable(indication = null, interactionSource = null, onClick = onClick)
}