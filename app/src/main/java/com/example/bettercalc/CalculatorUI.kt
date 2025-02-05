package com.example.bettercalc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bettercalc.ui.theme.BetterCalcTheme
import kotlinx.coroutines.launch

enum class AutoScrollLocation() {
    END, START,
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    BetterCalcTheme {
        val viewModel = CalculatorViewModel()
        viewModel.formula.value = "3*7%"
        CalculatorUI(viewModel)
    }
}

//TODO: Handle divide by zero errors.
@Composable
fun CalculatorUI(viewModel: CalculatorViewModel) {
    var autoScrollLocation by remember { mutableStateOf(AutoScrollLocation.END) }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier.align(Alignment.BottomCenter)
        ) {
            CalculatorOutputDisplay(
                screenText = viewModel.formula.value,
                autoScrollLocation = autoScrollLocation,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 32.dp)
            )
            HorizontalDivider(Modifier.padding(vertical = 15.dp))
            CalculatorButtons(viewModel,
                setAutoScrollLocation = { newDirection -> autoScrollLocation = newDirection })
        }
    }
}

@Composable
fun CalculatorOutputDisplay(
    screenText: String, autoScrollLocation: AutoScrollLocation, modifier: Modifier
) {
    // TODO: on button press, reset the scroll of this to the end.
    val displayLazyRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(screenText) {
        when (autoScrollLocation) {
            AutoScrollLocation.END -> {
                withFrameNanos {
                    displayLazyRowState.layoutInfo.visibleItemsInfo.firstOrNull()?.let { item ->
                        val offset =
                            (item.size - displayLazyRowState.layoutInfo.viewportSize.width).coerceAtLeast(
                                0
                            )
                        coroutineScope.launch {
                            displayLazyRowState.scrollToItem(0, -offset)
//                            displayLazyRowState.scrollToItem(0)
                        }
                    }
                }
            }

            AutoScrollLocation.START -> {
                println("START")
                displayLazyRowState.layoutInfo.visibleItemsInfo.firstOrNull()?.let { item ->
                    val offset =
                        (item.size - displayLazyRowState.layoutInfo.viewportSize.width).coerceAtLeast(
                            0
                        )
                    displayLazyRowState.scrollToItem(0, offset)
                }
            }
        }

    }

    LazyRow(
        reverseLayout = true, state = displayLazyRowState, modifier = modifier
    ) {
        item {
            Text(
                screenText,
                fontSize = 80.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}


// TODO: Remove need to type in the unicode math symbols and passing them around by using ascii chars or enum.
// TODO: Reduce number of places that need to be changed to add a new button/symbol.
@Composable
fun CalculatorButtons(
    viewModel: CalculatorViewModel, setAutoScrollLocation: (AutoScrollLocation) -> Unit
) {
    val buttonSpacing = 8.dp
    val digitBgColor = MaterialTheme.colorScheme.surfaceContainer
    val digitTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val operandBgColor = MaterialTheme.colorScheme.secondaryContainer
    val operandTextColor = MaterialTheme.colorScheme.onSecondaryContainer

    Column(
        verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        modifier = Modifier.padding(10.dp)
    ) {
        val buttonModifier: (Color) -> Modifier = { color ->
            Modifier
                .aspectRatio(1f)
                .weight(1f)
                .clip(CircleShape)
                .background(color)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("%", "^", "√", "÷").forEach { symbol ->
                CalculatorButton(
                    symbol,
                    color = operandTextColor,
                    modifier = buttonModifier(operandBgColor),
                    onClickHandler = {
                        setAutoScrollLocation(AutoScrollLocation.END)
                        viewModel.handleOperator(symbol)
                    },
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("7", "8", "9").forEach { symbol ->
                CalculatorButton(
                    symbol, color = digitTextColor,
                    modifier = buttonModifier(digitBgColor),
                    onClickHandler = {
                        setAutoScrollLocation(AutoScrollLocation.END)
                        viewModel.handleDigit(symbol)
                    },
                )
            }
            CalculatorButton(
                "×", color = operandTextColor,
                modifier = buttonModifier(operandBgColor),
                onClickHandler = {
                    setAutoScrollLocation(AutoScrollLocation.END)
                    viewModel.handleOperator("×")
                },
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("4", "5", "6").forEach { symbol ->
                CalculatorButton(
                    symbol, color = digitTextColor,
                    modifier = buttonModifier(digitBgColor),
                    onClickHandler = {
                        setAutoScrollLocation(AutoScrollLocation.END)
                        viewModel.handleDigit(symbol)
                    },
                )
            }
            CalculatorButton(
                "-",
                color = operandTextColor,
                modifier = buttonModifier(operandBgColor),
                onClickHandler = {
                    setAutoScrollLocation(AutoScrollLocation.END)
                    viewModel.handleOperator("-")
                },
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("1", "2", "3").forEach { symbol ->
                CalculatorButton(symbol,
                    color = digitTextColor,
                    modifier = buttonModifier(digitBgColor),
                    onClickHandler = {
                        setAutoScrollLocation(AutoScrollLocation.END)
                        viewModel.handleDigit(symbol)
                    })
            }
            CalculatorButton(
                "+", color = operandTextColor, modifier = buttonModifier(operandBgColor),
                onClickHandler = {
                    setAutoScrollLocation(AutoScrollLocation.END)
                    viewModel.handleOperator("+")
                },
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            CalculatorButton(
                "0", color = digitTextColor, modifier = buttonModifier(digitBgColor),
                onClickHandler = {
                    setAutoScrollLocation(AutoScrollLocation.END)
                    viewModel.handleDigit("0")
                },
            )
            CalculatorButton(
                ".", color = digitTextColor, modifier = buttonModifier(digitBgColor)
            ) { viewModel.handleOperator(".") }
            CalculatorButton("C",
                color = operandTextColor,
                modifier = buttonModifier(operandBgColor),
                onClickHandler = {
                    setAutoScrollLocation(AutoScrollLocation.END)
                    viewModel.backspace()
                },
                onLongClickHandler = { viewModel.reset() })
            CalculatorButton("=",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = buttonModifier(MaterialTheme.colorScheme.primaryContainer),
                onClickHandler = {
                    setAutoScrollLocation(AutoScrollLocation.START)
                    viewModel.handleEquals()
                })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalculatorButton(
    symbol: String,
    color: Color = Color.Black,
    modifier: Modifier = Modifier,
    onLongClickHandler: (() -> Unit)? = null,
    onClickHandler: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    Box(contentAlignment = Alignment.Center, modifier = modifier.combinedClickable(onClick = {
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onClickHandler()
    }, onLongClick = onLongClickHandler?.let { handler ->
        {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            handler()
        }
    }, onLongClickLabel = if (onLongClickHandler != null) "Reset Display" else null
    )
    ) {
        Text(symbol, fontSize = 40.sp, color = color)
    }
}
