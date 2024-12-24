package com.wongislandd.dailydoodle.drawingboard

import Redo
import Undo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.nexus.events.UiEvent
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.util.conditionallyChain
import com.wongislandd.nexus.util.noIndicationClickable
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun DrawingBoardScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<DrawingBoardViewModel>()
    val screenState by viewModel.drawingBoardScreenStateSlice.screenState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showColorPicker by remember { mutableStateOf(false) }
    LaunchedEffect(screenState.isColorPickerShown) {
        showColorPicker = screenState.isColorPickerShown
    }
    val onSendEvent: (UiEvent) -> Unit = { event ->
        coroutineScope.launch {
            viewModel.uiEventBus.sendEvent(
                event
            )
        }
    }
    DailyDoodleTopAppBar(title = "Drawing Board") {
        when (val canvasState = screenState.canvasState) {
            is Resource.Success -> {
                Box(modifier = modifier.fillMaxSize()) {
                    PathsCanvas(
                        canvasState.data.pathState,
                        viewModel.uiEventBus,
                        modifier = Modifier.fillMaxSize()
                    )
                    SettingsPanel(
                        settings = canvasState.data.settings,
                        isUndoAvailable = screenState.isUndoAvailable,
                        isRedoAvailable = screenState.isRedoAvailable,
                        onSendEvent = onSendEvent,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                    ColorPickerBottomSheet(
                        isVisible = showColorPicker,
                        currentColor = canvasState.data.settings.selectedColor,
                        onDismissRequest = { onSendEvent(DismissColorPicker) },
                        onColorSelected = { color ->
                            coroutineScope.launch {
                                viewModel.uiEventBus.sendEvent(ColorSelected(color))
                            }
                        },
                    )

                }
            }

            is Resource.Error -> {
                // Handle error
            }

            is Resource.Loading -> {
                // Handle loading
            }
        }
    }
}

@Composable
private fun ColorPickerBottomSheet(
    isVisible: Boolean,
    currentColor: Color,
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val controller = rememberColorPickerController()
    var tentativeColor by remember { mutableStateOf(currentColor) }
    var hexCode by remember { mutableStateOf("") }
    LaunchedEffect(currentColor) {
        controller.selectByColor(currentColor, false)
    }
    Column(
        modifier = modifier.fillMaxSize().conditionallyChain(
            isVisible, Modifier.background(
                Color.Transparent.copy(alpha = .2f)
            ).noIndicationClickable { onDismissRequest() }
        ).conditionallyChain(
            !isVisible,
            Modifier.background(Color.Transparent.copy(alpha = 0f))
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().weight(1f, fill = true))
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 }
            ) + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically(
                targetOffsetY = { it / 2 }
            ) + shrinkVertically(
                shrinkTowards = Alignment.Top
            ) + fadeOut()
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = Color.White,
                modifier = Modifier.noIndicationClickable()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    HsvColorPicker(
                        modifier = modifier
                            .padding(10.dp)
                            .size(250.dp),
                        controller = controller,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            tentativeColor = colorEnvelope.color
                            hexCode = colorEnvelope.hexCode.uppercase()
                        },
                        initialColor = currentColor
                    )
                    AlphaSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(35.dp),
                        controller = controller,
                    )
                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(35.dp),
                        controller = controller,
                    )
                    TileColorAndText(
                        controller = controller,
                        colorHex = hexCode,
                        onClick = {
                            onColorSelected(tentativeColor)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TileColorAndText(
    controller: ColorPickerController,
    colorHex: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "#$colorHex",
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
            color = controller.selectedColor.value
        )
        AlphaTile(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .clickable { onClick() },
            controller = controller
        )
    }
}

@Composable
fun SettingsPanel(
    settings: CanvasSettings,
    isUndoAvailable: Boolean = false,
    isRedoAvailable: Boolean = false,
    onSendEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        UndoAndRedo(
            isUndoAvailable = isUndoAvailable,
            isRedoAvailable = isRedoAvailable,
            onSendEvent = onSendEvent
        )
        ColorPickerEntryPoint(settings.selectedColor, { onSendEvent(ShowColorPicker) })
    }
}

@Composable
fun UndoAndRedo(
    isUndoAvailable: Boolean = false,
    isRedoAvailable: Boolean = false,
    onSendEvent: (UiEvent) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(onClick = { onSendEvent(DrawingAction.OnUndo) }, enabled = isUndoAvailable) {
            Icon(Undo, contentDescription = "Undo last action")
        }
        IconButton(onClick = { onSendEvent(DrawingAction.OnRedo) }, enabled = isRedoAvailable) {
            Icon(Redo, contentDescription = "Redo last action")
        }
    }
}