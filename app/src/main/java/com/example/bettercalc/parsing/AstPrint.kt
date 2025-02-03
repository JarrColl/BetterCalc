package com.example.bettercalc.parsing

fun AstPrint(expr: Expression): String {
    return when(expr) {
        is BinaryExpression -> {
            parenthesise(expr.operator.toString(), expr.left,  expr.right)
        }

        is UnaryExpression -> {
            parenthesise(expr.operator.toString(), expr.right)
        }

        is LiteralExpression -> {
            expr.value.toString()
        }

        else -> throw Exception("Unexpected ASTPrint expression type.")
    }
}

fun parenthesise(name: String, vararg exprs: Expression): String {
    var returnStr = "(" + name

    for (expr in exprs) {
        returnStr += " ${AstPrint(expr)}"
    }
    returnStr += ")"

    return returnStr
}