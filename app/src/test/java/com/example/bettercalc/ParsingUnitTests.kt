package com.example.bettercalc

import com.example.bettercalc.parsing.Parser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ParsingUnitTests {
    val parser = Parser()

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
//TODO: make this test pass.

//    @Test
//    fun percentAsOperator() {
//        var answer: Double = 0.0;
//        answer = parser.calculate("5%5")
//        assertEquals(0.25, answer, 0.0)
//    }

//    @Test
//    fun handleInvalidInput() {
//        try {
//            parser.calculate("+5")
//            assertTrue(true)
//        } catch (e: Exception) {
//           Log(e)
//            assertTrue(false)
//        }
//    }
}
