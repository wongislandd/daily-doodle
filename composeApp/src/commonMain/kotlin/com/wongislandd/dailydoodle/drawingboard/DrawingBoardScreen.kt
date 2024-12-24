package com.wongislandd.dailydoodle.drawingboard

import Eraser
import Pencil
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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
    var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }
    val coroutineScope = rememberCoroutineScope()
    val onSendEvent: (UiEvent) -> Unit = { event ->
        coroutineScope.launch {
            viewModel.uiEventBus.sendEvent(
                event
            )
        }
    }
    DailyDoodleTopAppBar(title = "Drawing Board", actions = {
        if (screenState.isShareEnabled) {
            IconButton(onClick = { onSendEvent(ImageExported(canvasSize)) }) {
                Icon(
                    Icons.Default.Share,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Export Drawing"
                )
            }
        }
    }) {
        when (val canvasState = screenState.canvasState) {
            is Resource.Success -> {
                Box(modifier = modifier.fillMaxSize().onGloballyPositioned { coordinates ->
                    // Retrieve the size of the composable
                    canvasSize = coordinates.size.toSize()
                }) {
                    PathsCanvas(
                        canvasState.data.pathState,
                        viewModel.uiEventBus,
                        modifier = Modifier.fillMaxSize()
                    )
                    SettingsPanel(
                        settings = canvasState.data.settings,
                        isUndoAvailable = canvasState.data.settings.isUndoAvailable,
                        isRedoAvailable = canvasState.data.settings.isRedoAvailable,
                        onSendEvent = onSendEvent,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                    ThicknessSelectionBottomSheet(
                        isVisible = screenState.isThicknessSelectorShown,
                        currentTool = canvasState.data.settings.brushSettings.selectedTool,
                        currentThickness = canvasState.data.settings.brushSettings.getThickness(),
                        currentColor = canvasState.data.settings.brushSettings.selectedColor,
                        thicknessHistory = canvasState.data.settings.thicknessHistory,
                        onDismissRequest = { onSendEvent(DismissThicknessSelector) },
                        onThicknessChange = { thickness -> onSendEvent(ThicknessSelected(thickness)) }
                    )
                    ColorPickerBottomSheet(
                        isVisible = screenState.isColorPickerShown,
                        currentColor = canvasState.data.settings.brushSettings.selectedColor,
                        onDismissRequest = { onSendEvent(DismissColorPicker) },
                        onColorSelected = { color ->
                            coroutineScope.launch {
                                viewModel.uiEventBus.sendEvent(ColorSelected(color))
                            }
                        },
                        colorHistory = canvasState.data.settings.colorHistory
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
private fun SettingBottomSheet(
    isVisible: Boolean,
    title: String? = null,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
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
                Column {
                    title?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    content()
                }
            }
        }
    }
}

@Composable
private fun ThicknessSelectionBottomSheet(
    isVisible: Boolean,
    currentTool: DrawingUtencils,
    currentThickness: Dp,
    currentColor: Color,
    thicknessHistory: List<Dp>,
    onDismissRequest: () -> Unit,
    onThicknessChange: (Dp) -> Unit,
    modifier: Modifier = Modifier
) {
    var tentativeThickness by remember { mutableStateOf(currentThickness) }
    LaunchedEffect(currentThickness) {
        tentativeThickness = currentThickness
    }

    SettingBottomSheet(
        isVisible = isVisible,
        title = "Select ${currentTool.displayName} thickness",
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(300.dp).fillMaxWidth().padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            // Dynamic Preview
            when (currentTool) {
                DrawingUtencils.PENCIL -> ThicknessPreview(
                    thickness = tentativeThickness,
                    color = currentColor
                )

                DrawingUtencils.ERASER -> ThicknessPreview(
                    thickness = tentativeThickness,
                    color = Color.White,
                    modifier = Modifier.border(2.dp, Color.Black, shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = tentativeThickness.value,
                onValueChange = {
                    tentativeThickness = it.dp
                },
                valueRange = 5f..50f // control min and max thickness here
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (currentTool == DrawingUtencils.PENCIL && thicknessHistory.isNotEmpty()) {
                LabeledComponent("Previously used") {
                    History(thicknessHistory) { thickness ->
                        Box(modifier = Modifier.sizeIn(minWidth = 36.dp)) {
                            ThicknessPreview(
                                thickness = thickness,
                                color = currentColor,
                                modifier = Modifier.clickable {
                                    tentativeThickness = thickness
                                }.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            TextButton(onClick = {
                onThicknessChange(tentativeThickness)
                onDismissRequest()
            }) {
                Text(text = "Confirm")
            }
        }
    }
}

@Composable
private fun ThicknessPreview(thickness: Dp, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(thickness)
            .clip(CircleShape)
            .background(color = color)
    )
}

@Composable
private fun ColorPickerBottomSheet(
    isVisible: Boolean,
    currentColor: Color,
    colorHistory: List<Color>,
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
    SettingBottomSheet(
        isVisible = isVisible,
        title = "Change colors",
        onDismissRequest = onDismissRequest,
        modifier = modifier
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
            LabeledComponent(label = "Alpha") {
                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp),
                    controller = controller,
                )
            }
            LabeledComponent(label = "Brightness") {
                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp),
                    controller = controller,
                )
            }
            TileColorAndText(
                controller = controller,
                colorHex = hexCode,
                onClick = {
                    onColorSelected(tentativeColor)
                }
            )
            if (colorHistory.isNotEmpty()) {
                LabeledComponent(label = "Previously used") {
                    ColorHistory(
                        colorHistory = colorHistory,
                        onColorSelected = { color ->
                            controller.selectByColor(color, true)
                        },
                    )
                }
            }
            TextButton(onClick = {
                onColorSelected(tentativeColor)
                onDismissRequest()
            }) {
                Text(text = "Confirm")
            }
        }
    }
}

@Composable
private fun ColorHistory(
    colorHistory: List<Color>,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    History(colorHistory, modifier = modifier) { color ->
        AlphaTile(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(6.dp))
                .clickable { onColorSelected(color) },
            selectedColor = color
        )
    }
}

@Composable
private fun <T> History(
    historyEntries: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(historyEntries.size) { index ->
            content(historyEntries[index])
        }
    }
}

@Composable
private fun LabeledComponent(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
        )
        content()
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
    Row(
        modifier = modifier.padding(16.dp)
    ) {
        UndoAndRedo(
            isUndoAvailable = isUndoAvailable,
            isRedoAvailable = isRedoAvailable,
            onSendEvent = onSendEvent
        )
        BrushSettingsUI(
            brushSettings = settings.brushSettings,
            onSendEvent = onSendEvent
        )
    }
}

@Composable
private fun BrushSettingsUI(
    brushSettings: BrushSettings,
    onSendEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        SettingIconButton(
            icon = Icons.Default.Settings,
            contentDescription = "Change brush settings",
            onClick = { onSendEvent(ShowThicknessSelector) }
        )
        ToolSelection(brushSettings.selectedTool, onSendEvent)
        Spacer(modifier = Modifier.size(16.dp))
        ColorPickerEntryPoint(
            brushSettings.selectedColor,
            { onSendEvent(ShowColorPicker) })
    }
}

@Composable
private fun ToolSelection(
    selectedTool: DrawingUtencils,
    onSendEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val toolIcon = when (selectedTool) {
        DrawingUtencils.PENCIL -> Pencil
        DrawingUtencils.ERASER -> Eraser
    }
    SettingIconButton(
        toolIcon,
        "Currently selected tool: ${selectedTool.name}",
        isEnabled = true,
        modifier = modifier,
        onClick = {
            onSendEvent(
                ToolSelected(
                    if (selectedTool == DrawingUtencils.PENCIL) DrawingUtencils.ERASER else DrawingUtencils.PENCIL
                )
            )
        }
    )
}

@Composable
private fun UndoAndRedo(
    isUndoAvailable: Boolean = false,
    isRedoAvailable: Boolean = false,
    onSendEvent: (UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        SettingIconButton(Undo, "Undo last action", isUndoAvailable) {
            onSendEvent(DrawingAction.OnUndo)
        }
        SettingIconButton(Redo, "Redo last action", isRedoAvailable) {
            onSendEvent(DrawingAction.OnRedo)
        }

    }
}

@Composable
private fun SettingIconButton(
    icon: ImageVector,
    contentDescription: String? = null,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier, enabled = isEnabled) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (isEnabled) Color.Black else Color.Gray
        )
    }
}