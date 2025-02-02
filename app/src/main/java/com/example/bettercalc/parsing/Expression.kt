package com.example.bettercalc.parsing

import com.example.bettercalc.parsing.TokenType

abstract class Expression {
//    abstract override fun toString(): String
    abstract fun calculate(): Double
    abstract fun fetchOperator(): TokenType?
}
