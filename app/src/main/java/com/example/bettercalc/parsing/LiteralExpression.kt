package org.fossify.calculator.helpers.parsing

import com.example.bettercalc.parsing.TokenType

class LiteralExpression(val value: Double): Expression() {
    override fun calculate(): Double {
        return value
    }

    override fun fetchOperator(): TokenType? {
        return null
    }
}
