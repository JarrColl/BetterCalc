package com.example.bettercalc

import com.example.bettercalc.parsing.AstPrint
import com.example.bettercalc.parsing.Parser
import com.example.bettercalc.parsing.Tokeniser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ParsingUnitTests {
    val tokeniser = Tokeniser()
    val parser = Parser()
    var answer = BigDecimal.ZERO
    val viewModel = CalculatorViewModel()

    fun viewModelCalculate(equation: String): String {
        viewModel.formula.value = equation
        viewModel.handleEquals()
        return viewModel.formula.value
    }

    @Test
    fun basicSqrtOperator() {
        assertEquals("2", viewModelCalculate("1sqrt4"))
    }

    @Test
    fun percentAsOperator() {
        assertEquals("0.04", viewModelCalculate("2%2"))
    }

    @Test
    fun digitPercentResult() {
        assertEquals("0.02", viewModelCalculate("2%"))
    }

    @Test
    fun percentBeforeAnOperator() {
        assertEquals("-0.9", viewModelCalculate("10%-1"))
    }

    @Test
    fun floatPrecisionTest() {
        assertEquals("2.4", viewModelCalculate("3*4/5"))
    }

    @Test
    fun inverseByDivision() {
        assertEquals("0.3333333333333333", viewModelCalculate("1/3"))
    }

    @Test
    fun leftAssociativityResult() {
        assertEquals("0.6666666666666666", viewModelCalculate("1/3*2"))
    }

    @Test
    fun negativePercentPlusInt() {
        assertEquals("0.9", viewModelCalculate("-10%+1"))
    }

    @Test
    fun negativePercentMultiplyInt() {
        assertEquals("-0.8", viewModelCalculate("-20%*4"))
    }

    @Test
    fun scientificNotationImplemented() {
        //TODO
        assertEquals("-0.12309472391847091328479", viewModelCalculate("5%5"))
    }

    @Test
    fun plusMultiplyBodmas() {
        assertEquals("11", viewModelCalculate("1*2+9"))

        assertEquals("11", viewModelCalculate("9+1*2"))
    }

    @Test
    fun twoDigitPercent() {
        assertEquals("0.12", viewModelCalculate("12%"))
    }

    @Test
    fun percentAsOperator1() {
        assertEquals("0.25", viewModelCalculate("5%5"))
    }
    @Test
    fun percentAsOperator2() {
        assertEquals("10.5", viewModelCalculate("10+10%5"))
    }
    @Test
    fun percentAsOperator4() {
        assertEquals("0.0001", viewModelCalculate("10%10%10%10%"))
    }

/*** CalculatorViewModel CALCULATION RESULT UNIT TESTS***/
    @Test
    fun trimZerosWhenNoDecimal() {
        assertEquals("20", viewModelCalculate("10+10"))
    }

    @Test
    fun percentAsOperator3() {
        assertEquals("0.01", viewModelCalculate("10%10%10%10"))
    }


/*** PARSING AND TOKEN OUTPUT UNIT TESTS ***/
    @Test
    fun parsePercentAsOperator1() {
        val answer = AstPrint(parser.parseTesting("10%10%10%10%"))
        assertEquals("(PERCENT (PERCENT (PERCENT 10 10) 10) (PERCENT 10))", answer)
    }
    @Test
    fun parsePercentAsOperator2() {
        val answer = AstPrint(parser.parseTesting("10+10%5"))
        assertEquals("(PLUS 10 (PERCENT 10 5))", answer)
    }


    @Test
    fun printTokens() {
        var answer = "";
        val tokens = tokeniser.tokenise("2+2+2")
        for (token in tokens) {
            answer += (token.tokenType.toString() + " ")
        }
        assertEquals("DIGIT PLUS DIGIT PLUS DIGIT NULL ", answer)
    }

    @Test
    fun leftAssociativityPrint() {
        val answer = AstPrint(parser.parseTesting("1/3*2"))
        assertEquals("(MULTIPLY (DIVIDE 1 3) 2)", answer)
    }

    @Test
    fun parseConsecutiveTerms() {
        val answer = AstPrint(parser.parseTesting("2+2+2"))
        assertEquals("(PLUS (PLUS 2 2) 2)", answer)
    }

    @Test
    fun parseBinaryPercent() {
        val answer = AstPrint(parser.parseTesting("2%2"))
        assertEquals("(PERCENT 2 2)", answer)
    }

    /*** PARSER DOESN'T CRASH UNIT TESTS ***/

    @Test
    fun handlePlusWithoutLeft() {
        try {
            parser.calculate("+5")
            assertTrue(true)
        } catch (e: Exception) {
            println(e)
            assertTrue(false)
        }
    }

    @Test
    fun handlePlusWithoutRight() {
        try {
            parser.calculate("5+")
            assertTrue(true)
        } catch (e: Exception) {
            println(e)
            assertTrue(false)
        }
    }

    @Test
    fun handleBlankInput() {
        try {
            parser.calculate("")
            assertTrue(true)
        } catch (e: Exception) {
            println(e)
            assertTrue(false)
        }
    }
}
