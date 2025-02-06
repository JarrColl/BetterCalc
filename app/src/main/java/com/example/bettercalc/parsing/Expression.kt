package com.example.bettercalc.parsing

import java.math.BigDecimal

abstract class Expression {
//    abstract override fun toString(): String
    abstract fun calculate(): BigDecimal
    abstract fun fetchOperator(): TokenType?
}
