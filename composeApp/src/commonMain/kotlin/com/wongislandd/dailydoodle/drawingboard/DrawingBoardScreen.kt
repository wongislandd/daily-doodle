package com.wongislandd.dailydoodle.drawingboard

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
import com.dokar.sheets.BottomSheet
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.rememberBottomSheetState
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
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun DrawingBoardScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<DrawingBoardViewModel>()
    val screenState by viewModel.drawingBoardScreenStateSlice.screenState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val colorSheetState = rememberBottomSheetState()
    LaunchedEffect(screenState.isColorPickerShown) {
        if (screenState.isColorPickerShown) {
            colorSheetState.expand()
        } else {
            colorSheetState.collapse()
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
                        onSendEvent = { event ->
                            coroutineScope.launch {
                                viewModel.uiEventBus.sendEvent(
                                    event
                                )
                            }
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                    ColorPickerBottomSheet(
                        sheetState = colorSheetState,
                        currentColor = canvasState.data.settings.selectedColor,
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
    sheetState: BottomSheetState,
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val controller = rememberColorPickerController()
    var tentativeColor by remember { mutableStateOf(currentColor) }
    var hexCode by remember { mutableStateOf("") }
    LaunchedEffect(currentColor) {
        controller.selectByColor(currentColor, false)
    }
    BottomSheet(
        state = sheetState,
        modifier = modifier,
        skipPeeked = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
        IconButton(onClick = { onSendEvent(DrawingAction.OnUndo) }, enabled = isUndoAvailable) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
        }
        IconButton(onClick = { onSendEvent(DrawingAction.OnRedo) }, enabled = isRedoAvailable) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
        ColorPickerEntryPoint(settings.selectedColor, { onSendEvent(ShowColorPicker) })
    }
}