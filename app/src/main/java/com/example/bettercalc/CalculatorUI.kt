package com.example.bettercalc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bettercalc.ui.theme.BetterCalcTheme

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BetterCalcTheme {
        val viewModel = CalculatorViewModel()
        viewModel.formula.value = "3*7%"
        CalculatorUI(viewModel)
    }
}

@Composable
fun CalculatorUI(viewModel: CalculatorViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier.align(Alignment.BottomCenter)
        ) {
            CalculatorOutputDisplay(
                viewModel.formula.value, Modifier.height(100.dp)
            )
            HorizontalDivider(Modifier.padding(vertical = 15.dp))
            CalculatorButtons(viewModel)
        }
    }
}

@Composable
fun CalculatorOutputDisplay(screenText: String, modifier: Modifier) {
    // TODO: on button press, reset the scroll of this to the end.
    LazyRow(
        reverseLayout = true, modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 32.dp)
    ) {
        item {
            Text(screenText, fontSize = 80.sp, textAlign = TextAlign.End, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.fillMaxWidth())
        }

    }
}


// TODO: Remove need to type in the unicode math symbols and passing them around by using ascii chars or enum.
// TODO: Reduce number of places that need to be changed to add a new button/symbol.

@Composable
fun CalculatorButtons(viewModel: CalculatorViewModel) {
    val buttonSpacing = 8.dp
    val digitBgColor = MaterialTheme.colorScheme.surfaceContainer
    val digitTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val operandBgColor = MaterialTheme.colorScheme.secondaryContainer
    val operandTextColor = MaterialTheme.colorScheme.onSecondaryContainer

    Column(verticalArrangement = Arrangement.spacedBy(buttonSpacing), modifier = Modifier.padding(10.dp)) {
        val buttonModifier: (Color) -> Modifier = { color ->
            Modifier
                .aspectRatio(1f)
                .weight(1f)
                .clip(CircleShape)
                .background(color)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing), modifier = Modifier.fillMaxWidth()) {
            CalculatorButton("%", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('%') }
            CalculatorButton("^", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('^') }
            CalculatorButton("√", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('√') }
            CalculatorButton("÷", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('÷') }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing), modifier = Modifier.fillMaxWidth()) {
            CalculatorButton("7", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('7') }
            CalculatorButton("8", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('8') }
            CalculatorButton("9", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('9') }
            CalculatorButton("×", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('×') }
//            Button(onClick " " handleMultiply() }) {
//                Text('×')
//            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing), modifier = Modifier.fillMaxWidth()) {
            CalculatorButton("4", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('4') }
            CalculatorButton("5", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('5') }
            CalculatorButton("6", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('6') }
            CalculatorButton("-", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('-') }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing), modifier = Modifier.fillMaxWidth()) {
            CalculatorButton("1", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('1') }
            CalculatorButton("2", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('2') }
            CalculatorButton("3", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('3') }
            CalculatorButton("+", color = operandTextColor, modifier = buttonModifier(operandBgColor)) { viewModel.handleOperator('+') }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing), modifier = Modifier.fillMaxWidth()) {
            CalculatorButton("0", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleDigit('0') }
            CalculatorButton(".", color = digitTextColor, modifier = buttonModifier(digitBgColor)) { viewModel.handleOperator('.') }
            CalculatorButton("C", color = operandTextColor, modifier = buttonModifier(operandBgColor), onLongClickHandler = { viewModel.reset() }) { viewModel.backspace() }
            CalculatorButton("=", color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = buttonModifier(MaterialTheme.colorScheme.primaryContainer)) { viewModel.handleEquals() }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CalculatorButton(
    symbol: String, color: Color = Color.Black, modifier: Modifier = Modifier, onLongClickHandler: () -> Unit = {}, onClickHandler: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.combinedClickable(
            onClick = { onClickHandler() },
            onLongClick = {
                onLongClickHandler()
            }, onLongClickLabel = "TODO"
        )
    ) {
        Text(symbol, fontSize = 40.sp, color = color)
    }
}
//    val haptics = LocalHapticFeedback.current
//        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
