package org.fossify.math.helpers

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * 测试 NumberFormatHelper 在德国 locale（小数点逗号，千位分隔符点）下的行为。
 * 注意：所有输入必须符合当前 locale 的格式约定，否则会产生错误结果。
 */
class NumberFormatHelperGermanyTest {

    companion object {
        private lateinit var helper: NumberFormatHelper
        private var originalLocale: Locale? = null

        @BeforeClass
        @JvmStatic
        fun setup() {
            originalLocale = Locale.getDefault()
            Locale.setDefault(Locale.GERMANY)
            helper = NumberFormatHelper()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            originalLocale?.let { Locale.setDefault(it) }
        }
    }

    /**
     * bigDecimalToString: 将 BigDecimal 转换为当前 locale 的带千位分隔符的字符串。
     * - 使用系统当前 locale 的 DecimalFormat，忽略构造函数中传入的分隔符（潜在不一致）。
     * - 最多保留 15 位小数，去除尾部零和尾部的小数点符号（德文下逗号）。
     * - 输出始终符合当前 locale 的格式（例如 "1.234.567,89"）。
     *
     * 验证：与标准 DecimalFormat 行为一致。
     */
    @Test
    fun bigDecimalToString() {
        val formatter = DecimalFormat().apply {
            maximumFractionDigits = 15
            isGroupingUsed = true
            decimalFormatSymbols = DecimalFormatSymbols.getInstance()
        }
        fun expected(bd: BigDecimal) = formatter.format(bd).trimEnd('0').trimEnd(',')
        assertEquals(expected(BigDecimal("1234567")), helper.bigDecimalToString(BigDecimal("1234567")))
        assertEquals(expected(BigDecimal("1234567.89000")), helper.bigDecimalToString(BigDecimal("1234567.89000")))
        assertEquals(expected(BigDecimal("-9876.543")), helper.bigDecimalToString(BigDecimal("-9876.543")))
    }

    /**
     * addGroupingSeparators: 给纯数字字符串（不含千位分隔符，但小数点符合当前 locale）添加千位分隔符。
     * - 内部先调用 removeGroupingSeparator 清理所有千位分隔符（所以输入允许存在任意位置的千位分隔符，但小数点必须正确）。
     * - 然后转换为 BigDecimal（要求字符串为标准点分隔格式，而 removeGroupingSeparator 会输出该格式），
     * - 最后调用 bigDecimalToString 重新添加千位分隔符。
     * - 若解析失败（如包含非数字字符），原样返回输入。
     *
     * 前提：输入的小数点符号必须符合当前 locale（德国下为逗号）。
     * 错误示例：addGroupingSeparators("1234567.89") → 因为小数点错误，会导致 removeGroupingSeparator 误删点，
     *           最终输出 "123.456.789"（整数部分错误，小数丢失）。
     */
    @Test
    fun addGroupingSeparators() {
        // 正确输入
        assertEquals("1.234.567,89", helper.addGroupingSeparators("1234567,89"))
        // 输入中含有多余千位分隔符（位置错误），内部会先移除它们，不影响结果
        assertEquals("1.234.567,89", helper.addGroupingSeparators("12345.67,89"))
        assertEquals("1.234", helper.addGroupingSeparators("1234"))
        assertEquals("abc", helper.addGroupingSeparators("abc"))
        // 注意：以下错误输入（美式小数点）会导致错误输出，不作为有效断言
        // assertEquals("1.234.567,89", helper.addGroupingSeparators("1234567.89"))  // 实际输出 "123.456.789"
    }

    /**
     * removeGroupingSeparator: 从符合当前 locale 格式的数字字符串中移除千位分隔符，并将小数点符号转换为标准的点（`.`）。
     * - 先替换 groupingSeparator（德国下为点）为空，再替换 decimalSeparator（德国下为逗号）为点。
     * - 输出格式固定为 "1234.56"，可安全被 BigDecimal 解析。
     *
     * 前提：输入必须严格使用当前 locale 的分隔符（千位符点，小数点逗号）。
     * 错误示例：removeGroupingSeparator("1234.56")（美式小数点）
     *   → 第一步 replace(".", "") 把小数点移除 → "123456"
     *   → 第二步 replace(",", ".") 无变化 → "123456"
     *   → 输出 "123456"（丢失小数点，数值错误）。
     *
     * 如果输入不含千位分隔符，则仅转换小数点符号（如 "1234,56" → "1234.56"）。
     */
    @Test
    fun removeGroupingSeparator() {
        // 正确输入：带千位分隔符和不带千位分隔符的德国格式
        assertEquals("1234.56", helper.removeGroupingSeparator("1.234,56"))
        assertEquals("1234.56", helper.removeGroupingSeparator("1234,56"))
        // 错误样例（已注释，因会导致失败）：
        // assertEquals("1234.56", helper.removeGroupingSeparator("1234.56"))  // 实际输出 "123456"
    }

    /**
     * formatForDisplay: 将用户正在输入的字符串格式化为带千位分隔符的显示字符串，同时保留用户已输入的小数位数。
     * - 先调用 addGroupingSeparators 获得带千位符的字符串。
     * - 若原始输入包含当前 locale 的小数点（德国下逗号），则用原始输入的小数部分替换格式化后的小数部分，
     *   确保用户刚输入的小数位数不被截断（例如 "0,003" 不会变成 "0"）。
     * - 若输入无小数点，直接返回 addGroupingSeparators 的结果。
     *
     * 前提：输入字符串必须使用当前 locale 的小数点符号（德国下逗号）。
     * 错误示例：formatForDisplay("1234.003")（美式小数点）
     *   → addGroupingSeparators 会将其转为 "1.234.003"（因为点被当作千位符，误加）
     *   → 由于 input 不包含逗号，不进入小数保留逻辑，最终输出 "1.234.003"（错误）。
     */
    @Test
    fun formatForDisplay() {
        assertEquals("1.234,003", helper.formatForDisplay("1234,003"))
        assertEquals("0,003", helper.formatForDisplay("0,003"))
        assertEquals("1.234", helper.formatForDisplay("1234"))
        // 错误样例（已注释）：
        // assertEquals("1.234,003", helper.formatForDisplay("1234.003"))  // 实际输出 "1.234.003"
    }
}
