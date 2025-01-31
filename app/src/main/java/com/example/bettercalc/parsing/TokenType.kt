package com.example.bettercalc.parsing

enum class TokenType {
    NULL,
    DIGIT,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    PERCENT,
    POWER,
    ROOT,
    // Additional Non-token, Parser operator types
    PLUS_PERCENT,
    MINUS_PERCENT,
}
