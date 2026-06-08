package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class MassConverterTest {
    private val converter = MassConverter

    @Test
    fun convert_zero_value_always_returns_zero() {
        val input = ValueWithUnit(
            BigDecimal.ZERO,
            MassConverter.Unit.Gram as Converter.Unit
        )
        val result = converter.convert(input, MassConverter.Unit.Kilogram)

        assertEquals(0, result.value.compareTo(BigDecimal.ZERO))
    }

    @Test
    fun convert_1000_gram_to_kilogram() {
        val input = ValueWithUnit(
            BigDecimal("1000"),
            MassConverter.Unit.Gram as Converter.Unit
        )
        val result = converter.convert(input, MassConverter.Unit.Kilogram)

        assertEquals(0, result.value.compareTo(BigDecimal.ONE))
    }

    @Test
    fun convert_1_kilogram_to_pound() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            MassConverter.Unit.Kilogram as Converter.Unit
        )
        val result = converter.convert(input, MassConverter.Unit.Pound)

        // 1 kg = 1 / 0.45359237 lb ≈ 2.20462262 lb
        assertEquals(BigDecimal("2.20462262"), result.value.setScale(8, RoundingMode.HALF_UP))
    }

    @Test
    fun convert_1_pound_to_kilogram() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            MassConverter.Unit.Pound as Converter.Unit
        )
        val result = converter.convert(input, MassConverter.Unit.Kilogram)

        assertEquals(BigDecimal("0.45359237"), result.value.setScale(8, RoundingMode.HALF_UP))
    }
}
