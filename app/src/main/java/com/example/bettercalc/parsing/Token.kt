package com.example.bettercalc.parsing

import com.example.bettercalc.parsing.TokenType

data class Token(val tokenType: TokenType, val literal: Double?) {
}
