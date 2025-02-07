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

    @Test
    fun basicSqrtOperator() {
        answer = parser.calculate("1sqrt4")
        assertEquals(BigDecimal(2.0), answer)
    }

    @Test
    fun percentAsOperator() {
        answer = parser.calculate("2%2")
        assertEquals(BigDecimal("0.04"), answer)
    }

    @Test
    fun digitPercentResult() {
        answer = parser.calculate("2%")
        assertEquals(BigDecimal("0.02"), answer)
    }

    @Test
    fun percentBeforeAnOperator() {
        answer = parser.calculate("10%-1")
        assertEquals(BigDecimal("-0.9"), answer)
    }

    @Test
    fun floatPrecisionTest() {
        answer = parser.calculate("3*4/5")
        assertEquals(BigDecimal("2.4"), answer)
    }

    @Test
    fun inverseByDivision() {
        answer = parser.calculate("1/3")
        assertEquals(BigDecimal("0.3333333333333333"), answer)
    }

    @Test
    fun leftAssociativityResult() {
        answer = parser.calculate("1/3*2")
        assertEquals(BigDecimal("0.6666666666666666"), answer)
    }

    @Test
    fun negativePercentPlusInt() {
        answer = parser.calculate("-10%+1")
        assertEquals(BigDecimal("0.9"), answer)
    }

    @Test
    fun negativePercentMultiplyInt() {
        answer = parser.calculate("-20%*4")
        assertEquals(BigDecimal("-0.8"), answer)
    }

    @Test
    fun scientificNotationImplemented() {
        answer = parser.calculate("-10%+1")
        assertEquals(BigDecimal(-0.12309472391847091328479), answer)
    }

    @Test
    fun plusMultiplyBodmas() {
        answer = parser.calculate("1*2+9")
        assertEquals(BigDecimal(11.0), answer)

        answer = parser.calculate("9+1*2")
        assertEquals(BigDecimal(11.0), answer)
    }

    @Test
    fun twoDigitPercent() {
        answer = parser.calculate("12%")
        assertEquals(BigDecimal("0.12"), answer)
    }

    @Test
    fun percentAsOperator1() {
        answer = parser.calculate("5%5")
        assertEquals(BigDecimal(0.25), answer)
    }
    @Test
    fun percentAsOperator2() {
        answer = parser.calculate("10+10%5")
        assertEquals(BigDecimal("10.5"), answer)
    }
    @Test
    fun percentAsOperator4() {
        answer = parser.calculate("10%10%10%10%")
        assertEquals(BigDecimal("0.0001"), answer)
    }

/*** CalculatorViewModel CALCULATION RESULT UNIT TESTS***/
    @Test
    fun trimZerosWhenNoDecimal() {
        viewModel.formula.value = "10+10"
        viewModel.handleEquals()
        assertEquals("20", viewModel.formula.value)
    }

    @Test
    fun percentAsOperator3() {
        viewModel.formula.value = "10%10%10%10"
        viewModel.handleEquals()
        assertEquals("0.01", viewModel.formula.value)
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
