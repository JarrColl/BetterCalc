package com.example.bettercalc

import org.fossify.calculator.helpers.parsing.Parser
import org.junit.Test

import org.junit.Assert.*

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
//TODO: make this test pass.

//    @Test
//    fun percentAsOperator() {
//        var answer: Double = 0.0;
//        answer = parser.calculate("5%5")
//        assertEquals(0.25, answer, 0.0)
//    }
}
