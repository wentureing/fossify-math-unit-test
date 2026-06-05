package org.fossify.math.helpers

/**
 * 负责原始表达式（内部存储格式）与显示表达式（本地化格式）之间的双向转换。
 *
 * 原始表达式规格：
 * - 小数点：固定为 `.`
 * - 无千位分隔符
 * - 运算符：`+ - × ÷ ^ √ %` （乘除号为显示符号，求值时转换为 `*` `/`）
 *
 * 显示表达式规格：
 * - 小数点及千位分隔符遵循当前系统 Locale
 * - 运算符与原始表达式相同
 *
 * 转换委托给 [NumberFormatHelper] 处理数字段的格式化，不涉及运算符转换。
 */
class ExpressionFormatter(
    private val numberFormatHelper: NumberFormatHelper = NumberFormatHelper()
) {

    /**
     * 将原始表达式转换为本地化显示表达式。
     * 提取所有数字段（整数或小数），通过 [NumberFormatHelper.bigDecimalToString] 格式化后替换回原位置。
     */
    fun toDisplay(raw: String): String {
        if (raw.isEmpty()) return raw

        // 匹配原始表达式中的数字段（整数或小数，使用点）
        val rawNumberRegex = Regex("\\d+(?:\\.\\d+)?")
        return rawNumberRegex.replace(raw) { matchResult ->
            val rawNum = matchResult.value
            val bd = rawNum.toBigDecimal()
            // 使用 NumberFormatHelper 格式化为本地化字符串（包含千位分隔符和正确小数点）
            numberFormatHelper.bigDecimalToString(bd)
        }
    }

    /**
     * 将本地化显示表达式转换为原始表达式。
     * 直接调用 [NumberFormatHelper.removeGroupingSeparator] 移除千位分隔符并将本地小数点转换为点。
     */
    fun toRaw(display: String): String {
        if (display.isEmpty()) return display
        return numberFormatHelper.removeGroupingSeparator(display)
    }
}
