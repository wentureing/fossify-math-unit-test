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
 * 测试 NumberFormatHelper 在中文（中国）locale 下的行为。
 * 中文 locale 使用小数点"."和千位分隔符","（与 US locale 相同），
 * 但此测试确保在不同的 Locale 设置下格式化行为一致。
 */
class NumberFormatHelperChinaTest {

    companion object {
        private lateinit var helper: NumberFormatHelper
        private var originalLocale: Locale? = null

        @BeforeClass
        @JvmStatic
        fun setup() {
            originalLocale = Locale.getDefault()
            Locale.setDefault(Locale.CHINA)
            helper = NumberFormatHelper()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            originalLocale?.let { Locale.setDefault(it) }
        }
    }

    @Test
    fun bigDecimalToString_formats_integer_with_grouping() {
        val formatter = DecimalFormat().apply {
            maximumFractionDigits = 15
            isGroupingUsed = true
            decimalFormatSymbols = DecimalFormatSymbols.getInstance()
        }
        fun expected(bd: BigDecimal) = formatter.format(bd).trimEnd('0').trimEnd('.')
        assertEquals(expected(BigDecimal("1234567")), helper.bigDecimalToString(BigDecimal("1234567")))
    }

    @Test
    fun bigDecimalToString_formats_decimal_with_grouping() {
        val formatter = DecimalFormat().apply {
            maximumFractionDigits = 15
            isGroupingUsed = true
            decimalFormatSymbols = DecimalFormatSymbols.getInstance()
        }
        fun expected(bd: BigDecimal) = formatter.format(bd).trimEnd('0').trimEnd('.')
        assertEquals(expected(BigDecimal("1234567.89000")), helper.bigDecimalToString(BigDecimal("1234567.89000")))
    }

    @Test
    fun bigDecimalToString_formats_negative_number() {
        val formatter = DecimalFormat().apply {
            maximumFractionDigits = 15
            isGroupingUsed = true
            decimalFormatSymbols = DecimalFormatSymbols.getInstance()
        }
        fun expected(bd: BigDecimal) = formatter.format(bd).trimEnd('0').trimEnd('.')
        assertEquals(expected(BigDecimal("-9876.543")), helper.bigDecimalToString(BigDecimal("-9876.543")))
    }

    @Test
    fun bigDecimalToString_strips_trailing_zeros() {
        val result = helper.bigDecimalToString(BigDecimal("3.140000"))
        assertEquals("3.14", result)
    }

    @Test
    fun bigDecimalToString_strips_trailing_decimal_point() {
        val result = helper.bigDecimalToString(BigDecimal("100.000"))
        assertEquals("100", result)
    }

    @Test
    fun bigDecimalToString_zero_value() {
        val result = helper.bigDecimalToString(BigDecimal.ZERO)
        assertEquals("0", result)
    }

    @Test
    fun addGroupingSeparators_standard_number() {
        assertEquals("1,234,567.89", helper.addGroupingSeparators("1234567.89"))
    }

    @Test
    fun addGroupingSeparators_small_number() {
        assertEquals("1,234", helper.addGroupingSeparators("1234"))
    }

    @Test
    fun addGroupingSeparators_single_digit() {
        assertEquals("5", helper.addGroupingSeparators("5"))
    }

    @Test
    fun addGroupingSeparators_invalid_input_returns_original() {
        assertEquals("abc", helper.addGroupingSeparators("abc"))
    }

    @Test
    fun addGroupingSeparators_empty_string_returns_empty() {
        assertEquals("", helper.addGroupingSeparators(""))
    }

    @Test
    fun removeGroupingSeparator_with_grouping() {
        assertEquals("1234.56", helper.removeGroupingSeparator("1,234.56"))
    }

    @Test
    fun removeGroupingSeparator_without_grouping() {
        assertEquals("1234.56", helper.removeGroupingSeparator("1234.56"))
    }

    @Test
    fun removeGroupingSeparator_integer_only() {
        assertEquals("1234", helper.removeGroupingSeparator("1,234"))
    }

    @Test
    fun removeGroupingSeparator_zero() {
        assertEquals("0", helper.removeGroupingSeparator("0"))
    }

    @Test
    fun formatForDisplay_with_decimal() {
        assertEquals("1,234.003", helper.formatForDisplay("1234.003"))
    }

    @Test
    fun formatForDisplay_small_decimal() {
        assertEquals("0.003", helper.formatForDisplay("0.003"))
    }

    @Test
    fun formatForDisplay_integer() {
        assertEquals("1,234", helper.formatForDisplay("1234"))
    }

    @Test
    fun formatForDisplay_large_number() {
        assertEquals("12,345,678.12345", helper.formatForDisplay("12345678.12345"))
    }

    @Test
    fun formatForDisplay_preserves_trailing_zeros_after_decimal() {
        assertEquals("1.00", helper.formatForDisplay("1.00"))
    }
}
