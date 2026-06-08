package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class LengthConverterTest {
    private val converter = LengthConverter

    @Test
    fun convert_1_kilometer_to_meter() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            LengthConverter.Unit.Kilometer as Converter.Unit
        )
        val result = converter.convert(input, LengthConverter.Unit.Meter)

        assertEquals(0, result.value.compareTo(BigDecimal("1000")))
    }

    @Test
    fun convert_1_meter_to_inch() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            LengthConverter.Unit.Meter as Converter.Unit
        )
        val result = converter.convert(input, LengthConverter.Unit.Inch)

        // 1 m / 0.0254 m/in ≈ 39.37007874 in
        assertEquals(BigDecimal("39.37007874"), result.value.setScale(8, RoundingMode.HALF_UP))
    }

    @Test
    fun convert_12_inches_to_foot() {
        val input = ValueWithUnit(
            BigDecimal("12"),
            LengthConverter.Unit.Inch as Converter.Unit
        )
        val result = converter.convert(input, LengthConverter.Unit.Foot)

        // 12 * 0.0254 / 0.3048 = 1
        assertEquals(0, result.value.compareTo(BigDecimal.ONE))
    }
}
