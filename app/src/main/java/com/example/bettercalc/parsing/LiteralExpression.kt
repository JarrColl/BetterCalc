package com.example.bettercalc.parsing

import java.math.BigDecimal

class LiteralExpression(val value: BigDecimal): Expression() {
    override fun calculate(): BigDecimal {
        return value
    }

    override fun fetchOperator(): TokenType? {
        return null
    }
}
