package com.example.bettercalc.parsing

import java.math.BigDecimal

class UnaryExpression(val operator: TokenType, val right: Expression) : Expression() {
    override fun calculate(): BigDecimal {
        return when (operator) {
            TokenType.MINUS -> -right.calculate()
            TokenType.PERCENT -> right.calculate().divide(BigDecimal("100.0"))
            else -> throw Exception("Unexpected Unary Operator")
        }
    }

    override fun fetchOperator(): TokenType? {
        return operator
    }
}
