package com.wongislandd.dailydoodle

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wongislandd.dailydoodle.sharing.ActivityReference
import com.wongislandd.nexus.theming.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityReference.current = this
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        setContent {
            AppTheme {
                App(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .safeDrawingPadding()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityReference.current = null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}