package org.fossify.math.helpers

import com.ezylang.evalex.Expression
import com.ezylang.evalex.parser.ParseException
import java.math.BigDecimal
import java.math.MathContext

sealed class EvalError {
    object DivideByZero : EvalError()
    object SyntaxError : EvalError()
    object NumberFormat : EvalError()
}

sealed class EvalResult {
    data class Success(val value: BigDecimal) : EvalResult()
    data class Failure(val error: EvalError) : EvalResult()
}

object ExpressionEvaluator {

    private const val MATH_CONTEXT_PRECISION = 20
    private val mathContext = MathContext(MATH_CONTEXT_PRECISION)

    /**
     * 计算表达式，返回成功或失败。
     * @param expression 原始格式表达式（小数点 .，无千位分隔符，运算符 + - × ÷ ^ √ %）
     */
    fun evaluate(expression: String): EvalResult {
        return try {
            val prepared = prepareExpression(expression)
            val expr = Expression(prepared)
            val result = expr.evaluate().numberValue as BigDecimal
            EvalResult.Success(result)
        } catch (e: ArithmeticException) {
            EvalResult.Failure(EvalError.DivideByZero)
        } catch (e: ParseException) {
            EvalResult.Failure(EvalError.SyntaxError)
        } catch (e: Exception) {
            // 其他异常（如函数未定义）视为语法错误
            EvalResult.Failure(EvalError.SyntaxError)
        }
    }

    /**
     * 预览表达式，失败返回 null（不抛出异常）。
     */
    fun evaluateOrNull(expression: String): BigDecimal? {
        return when (val result = evaluate(expression)) {
            is EvalResult.Success -> result.value
            is EvalResult.Failure -> null
        }
    }

    /**
     * 预处理表达式：
     * - 将乘除号转换为 EvalEx 可识别的 * 和 /
     * - 将 √ 转换为 SQRT() 函数
     * - 将 % 转换为 /100
     *
     * 输入表达式使用原始格式（小数点 .，无千位分隔符，运算符 + - × ÷ ^ √ %）。
     */
    private fun prepareExpression(expression: String): String {
        var result = expression

        // 乘除号转换（必须优先于根号和百分比处理，但根号和百分比不涉及乘除号，顺序无关）
        result = result.replace("×", "*").replace("÷", "/")

        // 转换根号：√数字 -> SQRT(数字)
        val sqrtRegex = Regex("√(\\d+(?:\\.\\d+)?)")
        result = sqrtRegex.replace(result) { "SQRT(${it.groupValues[1]})" }

        // 转换百分比：数字% -> (数字/100)
        val percentRegex = Regex("(\\d+(?:\\.\\d+)?)%")
        result = percentRegex.replace(result) { "(${it.groupValues[1]}/100)" }

        return result
    }
}
