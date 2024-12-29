package com.wongislandd.dailydoodle.prompting

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wongislandd.dailydoodle.LocalAppViewModel
import com.wongislandd.dailydoodle.util.DailyDoodleTopAppBar
import com.wongislandd.navigation.NavigationItemKey
import com.wongislandd.navigation.PROMPT_ARG
import com.wongislandd.nexus.navigation.LocalNavHostController
import com.wongislandd.nexus.util.Resource
import com.wongislandd.nexus.util.conditionallyChain
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PromptScreen(modifier: Modifier = Modifier) {

    val viewModel = koinViewModel<PromptViewModel>()
    val appViewModel = LocalAppViewModel.current
    val localNavController = LocalNavHostController.current
    val screenState by viewModel.promptScreenStateSlice.screenState.collectAsState()
    DailyDoodleTopAppBar(title = "Generating Prompt") {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO move some of this logic into the screen state
                DiceRoller(
                    isRolling = screenState.promptRes is Resource.Loading,
                    onClick = if (screenState.promptRes is Resource.Success) {
                        {
                            appViewModel.navigate(
                                localNavController,
                                NavigationItemKey.DRAWING_BOARD,
                                mapOf(PROMPT_ARG to screenState.promptRes.data)
                            )
                            screenState.promptRes.data
                        }
                    } else null
                )
                Spacer(modifier = Modifier.size(16.dp))
                when (val promptRes = screenState.promptRes) {
                    is Resource.Success -> {
                        Text(
                            "The prompt is...",
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = promptRes.data,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    is Resource.Loading -> {
                        Text(
                            "Generating prompt...",
                        )
                    }

                    is Resource.Error -> {
                        Text(
                            "Error generating prompt",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiceRoller(
    isRolling: Boolean,
    onClick: (() -> Unit)? = null
) {
    var rotation by remember { mutableStateOf(0f) }
    val isClickable = onClick != null

    // Animate rotation while rolling
    val animatedRotation by animateFloatAsState(
        targetValue = if (isRolling) rotation + 360f else rotation,
        animationSpec = tween(300)
    )

    // Update rotation during rolling
    LaunchedEffect(isRolling) {
        if (isRolling) {
            for (i in 1..10) {
                rotation += 36f // Rotate dice incrementally
                delay(100) // Wait before next change
            }
        } else {
            rotation = 0f
        }
    }

    // The visual dice
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        Card(
            modifier = Modifier
                .graphicsLayer(rotationZ = animatedRotation),
            elevation = 8.dp,
            backgroundColor = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
                    .conditionallyChain(isClickable, Modifier.clickable {
                        onClick?.invoke()
                    })
            ) {
                if (isClickable) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Draw"
                    )
                } else {
                    androidx.compose.foundation.text.BasicText(
                        text = "?",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}