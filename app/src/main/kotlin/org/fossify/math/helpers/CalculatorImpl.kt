package org.fossify.math.helpers

import android.content.Context
import org.fossify.commons.extensions.toast
import org.fossify.math.R
import org.json.JSONObject
import org.json.JSONTokener
import java.math.BigDecimal

class CalculatorImpl(
    calculator: Calculator,
    private val context: Context,
    calculatorState: String = ""
) {
    private var callback: Calculator? = calculator

    // 新内部状态
    private var rawExpression: String = "0"           // 原始格式表达式（. 小数点，无千位分隔符）
    private var lastResult: BigDecimal? = null        // 上次计算结果
    private var lastRawExpression: String = ""        // 上次成功求值的原始表达式
    private var lastKeyType: String = ""              // "digit", "operator", "equals"

    // 依赖新模块
    private val evaluator = ExpressionEvaluator
    private val formatter = ExpressionFormatter()
    private val historyManager = HistoryManager(context)
    private val errorHandler = ErrorHandler(context)

    init {
        if (calculatorState.isNotEmpty()) {
            setFromSaveInstanceState(calculatorState)
        }
        refreshDisplay()
    }

    // ==================== 公共方法 ====================

    fun numpadClicked(id: Int) {
        if (lastKeyType == EQUALS) {
            // 等号后开始新输入，清空表达式
            rawExpression = "0"
            lastResult = null
            lastRawExpression = ""
            lastKeyType = ""
        }

        val digit = when (id) {
            R.id.btn_0 -> '0'
            R.id.btn_1 -> '1'
            R.id.btn_2 -> '2'
            R.id.btn_3 -> '3'
            R.id.btn_4 -> '4'
            R.id.btn_5 -> '5'
            R.id.btn_6 -> '6'
            R.id.btn_7 -> '7'
            R.id.btn_8 -> '8'
            R.id.btn_9 -> '9'
            R.id.btn_decimal -> '.'  // 始终使用点，不用本地化小数点
            else -> return
        }

        if (rawExpression == "0" && digit != '.') {
            rawExpression = digit.toString()
        } else {
            rawExpression += digit
        }

        lastKeyType = DIGIT
        refreshDisplay()
    }

    fun handleOperation(operation: String) {
        if (lastKeyType == EQUALS) {
            // 等号后输入运算符，以 lastResult 开始新表达式
            rawExpression = lastResult?.toPlainString() ?: "0"
            lastResult = null
            lastRawExpression = ""
            lastKeyType = ""
        }

        // 避免连续输入两个运算符（但允许，由求值时处理）
        val operator = getSign(operation)  // 返回 + - × ÷ ^ √ %
        rawExpression += operator

        lastKeyType = "operator"
        refreshDisplay()
    }

    fun handleEquals() {
        // 无效表达式检查
        if (rawExpression.isEmpty() || rawExpression.matches(Regex("^[+\\-×÷^√%]+$"))) {
            errorHandler.handle(EvalError.SyntaxError)
            return
        }

        // 处理连续等号：按新要求废除重复等号求值（不再自动重复运算）
        // 因此每次等号都直接对当前表达式求值
        val exprToEval = rawExpression

        when (val result = evaluator.evaluate(exprToEval)) {
            is EvalResult.Success -> {
                val resultStr = result.value.toPlainString()
                // 记录历史（原始格式）
                historyManager.addEntry(exprToEval, resultStr)

                // 更新状态
                lastResult = result.value
                lastRawExpression = exprToEval
                rawExpression = resultStr
                lastKeyType = EQUALS

                refreshDisplay()
            }
            is EvalResult.Failure -> {
                errorHandler.handle(result.error)
                // 失败时不修改 rawExpression 等状态
            }
        }
    }

    fun handleClear() {
        if (rawExpression.isNotEmpty()) {
            rawExpression = rawExpression.dropLast(1)
        }
        if (rawExpression.isEmpty()) {
            rawExpression = "0"
        }
        // 如果清空后为 "0" 且之前是计算结果，可选择性重置上次结果，但保持简单
        if (rawExpression == "0") {
            lastResult = null
            lastRawExpression = ""
            lastKeyType = ""
        } else {
            lastKeyType = CLEAR
        }
        refreshDisplay()
    }

    fun handleReset() {
        rawExpression = "0"
        lastResult = null
        lastRawExpression = ""
        lastKeyType = ""
        refreshDisplay()
    }

    fun turnToNegative(): Boolean {
        // 找到 rawExpression 中最后一个数字段（可能带负号）
        val regex = Regex("-?\\d+(?:\\.\\d+)?")
        val matches = regex.findAll(rawExpression).toList()
        if (matches.isEmpty()) return false

        val lastMatch = matches.last()
        val numStr = lastMatch.value
        val newNumStr = if (numStr.startsWith('-')) numStr.drop(1) else "-$numStr"
        rawExpression = rawExpression.replaceRange(lastMatch.range, newNumStr)
        refreshDisplay()
        return true
    }

    fun getCalculatorStateJson(): JSONObject {
        val json = JSONObject()
        json.put(INPUT_DISPLAYED_FORMULA, rawExpression)
        json.put(SECOND_VALUE, lastResult?.toPlainString() ?: "")
        json.put(PREVIOUS_CALCULATION, lastRawExpression)
        json.put(LAST_KEY, lastKeyType)
        // 其余字段保留为空字符串以兼容旧结构（但不会被读取）
        json.put(RES, "")
        json.put(LAST_OPERATION, "")
        json.put(BASE_VALUE, "")
        return json
    }

    fun loadRawExpression(expression: String) {
        handleReset()
        rawExpression = expression
        lastKeyType = ""
        refreshDisplay()
    }

    // ==================== 私有辅助 ====================

    private fun refreshDisplay() {
        // 公式显示：原始格式 -> 本地化显示
        val displayFormula = formatter.toDisplay(rawExpression)
        callback?.showNewFormula(displayFormula, context)

        // 结果预览：尝试求值，失败则显示空字符串
        val previewResult = evaluator.evaluateOrNull(rawExpression)
        val displayResult = previewResult?.let { formatter.toDisplay(it.toPlainString()) } ?: ""
        callback?.showNewResult(displayResult, context)
    }

    private fun getSign(operation: String) = when (operation) {
        MINUS -> "-"
        MULTIPLY -> "×"
        DIVIDE -> "÷"
        PERCENT -> "%"
        POWER -> "^"
        ROOT -> "√"
        else -> "+"
    }

    private fun setFromSaveInstanceState(json: String) {
        try {
            val jsonObject = JSONTokener(json).nextValue() as JSONObject
            rawExpression = jsonObject.optString(INPUT_DISPLAYED_FORMULA, "0")
            lastResult = jsonObject.optString(SECOND_VALUE).takeIf { it.isNotEmpty() }?.toBigDecimal()
            lastRawExpression = jsonObject.optString(PREVIOUS_CALCULATION, "")
            lastKeyType = jsonObject.optString(LAST_KEY, "")
        } catch (e: Exception) {
            // 恢复失败时使用默认值
            rawExpression = "0"
            lastResult = null
            lastRawExpression = ""
            lastKeyType = ""
        }
    }

    // 保留原 Calculator 接口中使用的常量（这里假设它们已在文件中定义）
    // 注意：原代码中的常量如 PLUS, MINUS, EQUALS 等未在此重写文件中导入，
    // 但它们在编译时存在（来自同一个包的其他文件）。我们保持引用不变。
}
