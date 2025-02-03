package com.example.bettercalc

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.bettercalc.parsing.Parser

private const val nonChainableOperators = "+-×÷^√."
private val parser = Parser()

class CalculatorViewModel : ViewModel() {
    val formula = mutableStateOf("")

    fun backspace() {
        formula.value = formula.value.dropLast(1)
    }

    fun reset() {
        formula.value = ""
    }

    fun handleDigit(digit: Char) {
        formula.value += digit
    }

    fun handleEquals() {
        formula.value = parser.calculate(formula.value).toString().trimEnd('0').trimEnd('.')
    }

    //TODO prevent output like 86.39999999999999999999999999999999
    //TODO prevent this issue: "3"->"x"->"-"->"x" = "3xx"
    //TODO Prevent chaining of decimal like 3.4.5.6.2
    fun handleOperator(symbol: Char) { // TODO: Make this function less trash
        if (formula.value.isNotEmpty()) {
            if (formula.value.last() in nonChainableOperators) {
                // '-' is allowed to be the first character. This must be done to prevent the initial '-' from being replaced by another operator and causing a format error.
                if (formula.value.length == 1 && formula.value[0] == '-') {
                    return
                }

                if (symbol != '-' || (formula.value[formula.value.lastIndex] == '-')) {
                    formula.value =
                        formula.value.dropLast(1) //TODO: Does this cause a recomposition before the rest finishes? resulting in 2 recompositions?
                }
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
                    if (formula.value.last() != '%') {
                        formula.value += '%'
                    }
                }

                '.' -> {
                    if (formula.value.last() == '%') {
                        formula.value = formula.value.dropLast(1) + "."
                    } else {
                        formula.value += "."
                    }
                }

                '√' -> {
                    formula.value += '√'
                }

                else -> throw Exception("Unreachable: Unexpected symbol encountered.")
            }
        } else {
            if (symbol == '-') {
                formula.value += '-'
            }
        }
    }
}
