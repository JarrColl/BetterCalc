package com.example.bettercalc.parsing

import com.example.bettercalc.parsing.TokenType
import java.math.BigDecimal

data class Token(val tokenType: TokenType, val literal: BigDecimal?) {
}
