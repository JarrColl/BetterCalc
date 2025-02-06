package com.example.bettercalc.parsing

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.pow


class BinaryExpression(val left: Expression, val operator: TokenType, val right: Expression) :
    Expression() {

    val mc = MathContext.DECIMAL64

    private fun BigDecimal.pow(exponent: BigDecimal): BigDecimal { // TODO: Avoid converting to double??
        var result = BigDecimal.ZERO
        val signOf2 = exponent.signum()

        // Perform X^(A+B)=X^A*X^B (B = remainder)
        val dn1 = this.toDouble()

        // Compare the same row of digits according to context
        val n2 = exponent.multiply(BigDecimal(signOf2), mc) // n2 is now positive
        val remainderOf2 = n2.remainder(BigDecimal.ONE)
        val n2IntPart = n2.subtract(remainderOf2)

        // Calculate big part of the power using context -
        // bigger range and performance but lower accuracy
        val intPow = this.pow(n2IntPart.intValueExact())
        val doublePow = BigDecimal(dn1.pow(remainderOf2.toDouble()), mc)
        result = intPow.multiply(doublePow, mc)
        println("result: $result, signOf2: $signOf2")

        // Fix negative power
        if (signOf2 == -1) result = BigDecimal.ONE.divide(result, mc)
        println("result2: $result")
        return result
    }


    override fun calculate(): BigDecimal {
        val leftDecimal = left.calculate()
        val rightDecimal = right.calculate()

        return when (operator) {
            TokenType.PLUS -> leftDecimal.add(rightDecimal, mc)
            TokenType.MINUS -> leftDecimal.subtract(rightDecimal, mc)
            TokenType.MULTIPLY -> leftDecimal.multiply(rightDecimal, mc)
            TokenType.DIVIDE -> leftDecimal.divide(rightDecimal, mc)
            TokenType.POWER -> leftDecimal.pow(rightDecimal)
            TokenType.ROOT -> leftDecimal * (rightDecimal.pow(BigDecimal(0.5)))
            TokenType.PLUS_PERCENT -> leftDecimal.add(
                leftDecimal.multiply(
                    rightDecimal, mc
                ), mc
            )

            TokenType.MINUS_PERCENT -> leftDecimal.subtract(
                leftDecimal.multiply(
                    rightDecimal, mc
                ), mc
            )

            else -> throw Exception("Unexpected Binary Operand")
        }
    }

    override fun fetchOperator(): TokenType? {
        return operator
    }
}
