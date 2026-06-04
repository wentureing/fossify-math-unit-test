package org.fossify.math.helpers

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class NumberFormatHelperUsTest {

    companion object {
        private lateinit var helper: NumberFormatHelper
        private var originalLocale: Locale? = null

        @BeforeClass
        @JvmStatic
        fun setup() {
            originalLocale = Locale.getDefault()
            Locale.setDefault(Locale.US)
            helper = NumberFormatHelper()  // 使用系统 locale（已是 US）
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            originalLocale?.let { Locale.setDefault(it) }
        }
    }

    @Test
    fun bigDecimalToString() {
        val formatter = DecimalFormat().apply {
            maximumFractionDigits = 15
            isGroupingUsed = true
            decimalFormatSymbols = DecimalFormatSymbols.getInstance()
        }
        fun expected(bd: BigDecimal) = formatter.format(bd).trimEnd('0').trimEnd('.')

        assertEquals(expected(BigDecimal("1234567")), helper.bigDecimalToString(BigDecimal("1234567")))
        assertEquals(expected(BigDecimal("1234567.89000")), helper.bigDecimalToString(BigDecimal("1234567.89000")))
        assertEquals(expected(BigDecimal("-9876.543")), helper.bigDecimalToString(BigDecimal("-9876.543")))
    }

    @Test
    fun addGroupingSeparators() {
        assertEquals("1,234,567.89", helper.addGroupingSeparators("1234567.89"))
        assertEquals("1,234", helper.addGroupingSeparators("1234"))
        assertEquals("abc", helper.addGroupingSeparators("abc"))
    }

    @Test
    fun removeGroupingSeparator() {
        assertEquals("1234.56", helper.removeGroupingSeparator("1,234.56"))
        assertEquals("1234.56", helper.removeGroupingSeparator("1234.56"))
    }

    @Test
    fun formatForDisplay() {
        assertEquals("1,234.003", helper.formatForDisplay("1234.003"))
        assertEquals("0.003", helper.formatForDisplay("0.003"))
    }
}
