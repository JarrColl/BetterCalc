package com.example.bettercalc.parsing

import com.example.bettercalc.parsing.TokenType

class UnaryExpression(val operator: TokenType, val right: Expression) : Expression() {
    override fun calculate(): Double {
        return when (operator) {
            TokenType.MINUS -> -right.calculate()
            TokenType.PERCENT -> right.calculate() / 100
            else -> throw Exception("Unexpected Unary Operator")
        }
    }

    override fun fetchOperator(): TokenType? {
        return operator
    }
}
