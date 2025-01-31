package com.example.bettercalc

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

private const val nonChainableOperators = "+-×÷^√"

class CalculatorViewModel : ViewModel() {
    val formula = mutableStateOf("")

    fun backspace() {
        formula.value = formula.value.substring(0, formula.value.length - 1)
    }

    fun reset() {
        formula.value = ""
    }

    fun handleDigit(digit: Char) {
        formula.value += digit
    }

    fun handleOperator(symbol: Char) {
        if (formula.value[formula.value.length - 1] in nonChainableOperators) {
            formula.value = formula.value.substring(0, formula.value.length - 1)
        }
        when (symbol) {
            '+' -> {
                formula.value += '+'
            }

            '-' -> {
                formula.value += '-'
            }

            '×' -> {
                formula.value += '×'
            }

            '÷' -> {
                formula.value += '÷'
            }

            '^' -> {
                formula.value += '^'
            }

            '%' -> {
                if (formula.value[formula.value.length - 1] != '%') {
                    formula.value += '%'
                }
            }

            '√' -> {
                formula.value += '√'
            }

            else -> throw Exception("Unreachable: Unexpected symbol encountered.")
        }
    }
}
