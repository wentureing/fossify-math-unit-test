package org.fossify.math.helpers

import android.content.Context
import org.fossify.commons.extensions.showErrorToast
import org.fossify.commons.extensions.toast
import org.fossify.math.R

/**
 * 统一处理计算器相关的错误提示，保留原有字符串资源与 Toast 行为。
 */
class ErrorHandler(private val context: Context) {

    /**
     * 处理表达式求值过程中产生的 [EvalError]。
     */
    fun handle(error: EvalError) {
        when (error) {
            EvalError.DivideByZero -> context.toast(R.string.formula_divide_by_zero_error)
            EvalError.SyntaxError -> context.toast(org.fossify.commons.R.string.unknown_error_occurred)
            EvalError.NumberFormat -> context.toast(org.fossify.commons.R.string.unknown_error_occurred)
        }
    }

    /**
     * 处理 [NumberFormatException]（如数字解析失败），与原 [CalculatorImpl] 中
     * [android.content.Context.showErrorToast] 行为一致。
     */
    fun handleNumberFormat(exception: NumberFormatException) {
        context.showErrorToast(exception)
    }

    /**
     * 处理其他未预期的异常，使用通用的未知错误提示。
     */
    fun handleGeneral(exception: Exception) {
        context.toast(org.fossify.commons.R.string.unknown_error_occurred)
    }
}
