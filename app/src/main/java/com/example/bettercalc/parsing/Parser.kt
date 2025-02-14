package com.example.bettercalc.parsing

import java.math.BigDecimal

//TODO: HANDLE scientific notation (E)
class Parser() {
    private var tokeniser = Tokeniser()
    private var tokens: List<Token> = emptyList()
    private var current = 0

    private fun resetParser() {
        current = 0
    }

    private fun peekCurrent(): Token {
        return tokens[current]
    }

    private fun isAtEnd(): Boolean {
        return peekCurrent().tokenType == TokenType.NULL
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun advance(): Token {
        if (!isAtEnd()) {
            current++
        }
        return previous()
    }

    private fun reverse() {
        if (!isAtEnd()) {
            current--
        }
    }

    private fun checkTokenType(tokenType: TokenType): Boolean {
        if (isAtEnd()) {
            return false
        }

        return peekCurrent().tokenType == tokenType
    }

    private fun match(vararg tokenTypes: TokenType): Boolean {
        for (tokenType in tokenTypes) {
            if (checkTokenType(tokenType)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun primary(): Expression {
        if (this.match(TokenType.DIGIT)) {
            return LiteralExpression(previous().literal!!)
        }

        throw Exception("Expected a primary expression, but found: ${previous().tokenType}")
    }

    private fun unarySuffix(): Expression {
        val expr = primary()
        if (this.match(TokenType.PERCENT)) {
            if (peekCurrent().tokenType == TokenType.DIGIT) {
                reverse()
                return expr
            }

            return UnaryExpression(previous().tokenType, expr)
        }
        return expr
    }

    private fun unaryPrefix(): Expression {
        if (this.match(TokenType.MINUS)) {
            val operator = previous().tokenType
            val right = unaryPrefix()
            return UnaryExpression(operator, right)
        }

        return unarySuffix()
    }

    private fun order(): Expression {
        var expr = unaryPrefix()

        while (this.match(TokenType.POWER, TokenType.ROOT)) {
            var operator = previous().tokenType
            val right = unaryPrefix()
            expr = BinaryExpression(expr, operator, right)
        }
        return expr
    }


    private fun factor(): Expression {
        var expr = order()

        while (this.match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.PERCENT)) {//|| expr.fetchOperator() == TokenType.PERCENT) {
            var operator = previous().tokenType
            val right = order()
            expr = BinaryExpression(expr, operator, right)
        }
        return expr
    }

    private fun term(): Expression {
        var expr = factor()

        while (this.match(TokenType.PLUS, TokenType.MINUS)) {
            var operator = previous().tokenType
            val right = factor()
            if (right.fetchOperator() == TokenType.PERCENT && right is UnaryExpression) {
                operator = when (operator) {
                    TokenType.PLUS -> TokenType.PLUS_PERCENT
                    TokenType.MINUS -> TokenType.MINUS_PERCENT
                    else -> operator
                }
            }
            expr = BinaryExpression(expr, operator, right)
        }
        return expr
    }

    private fun parse(): Expression {
        return term()
    }

    fun parseTesting(source: String): Expression {
        resetParser()
        tokens = tokeniser.tokenise(source)
        return parse()
    }

    fun calculate(source: String): BigDecimal {
        resetParser()
        tokens = tokeniser.tokenise(source)
        val head = parse()
        return head.calculate()
    }
}
