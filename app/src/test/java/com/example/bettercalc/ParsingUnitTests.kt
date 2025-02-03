package com.example.bettercalc

import com.example.bettercalc.parsing.AstPrint
import com.example.bettercalc.parsing.Parser
import com.example.bettercalc.parsing.Tokeniser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ParsingUnitTests {
    val tokeniser = Tokeniser()
    val parser = Parser()

    @Test
    fun percentAsOperator() {
        var answer: Double = 0.0;
        answer = parser.calculate("2%2")
        assertEquals(0.04, answer, 0.0)
    }

    @Test
    fun digitPercentResult() {
        var answer: Double = 0.0;
        answer = parser.calculate("2%")
        assertEquals(0.02, answer, 0.0)
    }

    @Test
    fun percentBeforeAnOperator() {
        var answer: Double = 0.0;
        answer = parser.calculate("10%-1")
        assertEquals(-0.9, answer, 0.0)
    }

    @Test
    fun negativePercentPlusInt() {
        var answer: Double = 0.0;
        answer = parser.calculate("-10%+1")
        assertEquals(-0.9, answer, 0.0)
    }

    @Test
    fun plusMultiplyBodmas() {
        var answer: Double = 0.0;
        answer = parser.calculate("1×2+9")
        assertEquals(11.0, answer, 0.0)

        answer = parser.calculate("9+1×2")
        assertEquals(11.0, answer, 0.0)
    }

    @Test
    fun twoDigitPercent() {
        var answer: Double = 0.0;
        answer = parser.calculate("12%")
        assertEquals(0.12, answer, 0.0)
    }

    @Test
    fun printTokens() {
        var answer: String = "";
        val tokens = tokeniser.tokenise("2+2+2")
        for (token in tokens) {
            answer += (token.tokenType.toString() + " ")
        }
        assertEquals("DIGIT PLUS DIGIT PLUS DIGIT NULL ", answer)
    }

    @Test
    fun parseConsecutiveTerms() {
        var answer: String = "";
        answer = AstPrint(parser.parseTesting("2+2+2"))
        println(answer)
        assertEquals("(PLUS 2.0 (PLUS 2.0 2.0))", answer)
    }

    @Test
    fun parseBinaryPercent() {
        var answer: String = "";
        answer = AstPrint(parser.parseTesting("2%2"))
        println(answer)
        assertEquals("(PERCENT 2.0 2.0)", answer)
    }


//TODO: make this test pass.

    @Test
    fun percentAsOperator2() {
        var answer: Double = 0.0;
        answer = parser.calculate("5%5")
        assertEquals(0.25, answer, 0.0)
    }

    @Test
    fun handleInvalidInput() {
        try {
            parser.calculate("+5")
            assertTrue(true)
        } catch (e: Exception) {
            println(e)
            assertTrue(false)
        }
    }
}
