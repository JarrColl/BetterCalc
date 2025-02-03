package com.example.bettercalc.parsing

/*
1. DIGIT
2. OPERATOR
3. DIGIT

 */

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
            return LiteralExpression(previous().literal!!) //TODO: Handle this null case??
        }

        throw Exception("Expected a primary expression, but found: ${previous().tokenType.toString()}") //TODO: Handle this?
    }

    private fun unarySuffix(): Expression {
        val expr = primary()
        if(this.match(TokenType.PERCENT)) {
            return UnaryExpression(previous().tokenType, expr)
        }
        return expr
    }

    private fun unaryPrefix(): Expression {
        if (this.match(TokenType.MINUS)) {
            val operator = previous().tokenType
            val right = primary()
            return UnaryExpression(operator, right)
        }

        return unarySuffix()
    }

    private fun order(): Expression {
        val expr = unaryPrefix()

        if (this.match(TokenType.POWER, TokenType.ROOT)) {
            var operator = previous().tokenType
            val right = unaryPrefix()
            return BinaryExpression(expr, operator, right)
        }
        return expr
    }


    private fun factor(): Expression {
        val expr = order()

        if (this.match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.PERCENT)) {
            var operator = previous().tokenType
            val right = factor()
            return BinaryExpression(expr, operator, right)
        }
        return expr
    }

    private fun term(): Expression {
        val expr = factor()

        if (this.match(TokenType.PLUS, TokenType.MINUS)) {
            var operator = previous().tokenType
            val right = term()
            if (right.fetchOperator() == TokenType.PERCENT) { //TODO: Check this case: "2%2+2"
                operator = when(operator) {
                    TokenType.PLUS -> TokenType.PLUS_PERCENT
                    TokenType.MINUS -> TokenType.MINUS_PERCENT
                    else-> operator
                }
            }
            return BinaryExpression(expr, operator, right)
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

    fun calculate(source: String): Double {
        resetParser()
        tokens = tokeniser.tokenise(source)
        val head = parse()
        return head.calculate()
    }
}
