package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TimeConverterTest {
    private val converter = TimeConverter

    @Test
    fun convert_1_hour_to_minute() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            TimeConverter.Unit.Hour as Converter.Unit
        )
        val result = converter.convert(input, TimeConverter.Unit.Minute)

        assertEquals(0, result.value.compareTo(BigDecimal("60")))
    }

    @Test
    fun convert_1_day_to_second() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            TimeConverter.Unit.Day as Converter.Unit
        )
        val result = converter.convert(input, TimeConverter.Unit.Second)

        // 24 * 3600 = 86400
        assertEquals(0, result.value.compareTo(BigDecimal("86400")))
    }

    @Test
    fun convert_1_week_to_day() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            TimeConverter.Unit.Week as Converter.Unit
        )
        val result = converter.convert(input, TimeConverter.Unit.Day)

        assertEquals(0, result.value.compareTo(BigDecimal("7")))
    }
}
