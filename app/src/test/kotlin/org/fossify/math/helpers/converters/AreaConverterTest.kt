package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class AreaConverterTest {
    private val converter = AreaConverter

    @Test
    fun convert_1_square_kilometer_to_square_meter() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            AreaConverter.Unit.SquareKilometer as Converter.Unit
        )
        val result = converter.convert(input, AreaConverter.Unit.SquareMeter)

        assertEquals(0, result.value.compareTo(BigDecimal("1000000")))
    }

    @Test
    fun convert_10000_square_meter_to_hectare() {
        val input = ValueWithUnit(
            BigDecimal("10000"),
            AreaConverter.Unit.SquareMeter as Converter.Unit
        )
        val result = converter.convert(input, AreaConverter.Unit.Hectare)

        assertEquals(0, result.value.compareTo(BigDecimal.ONE))
    }

    @Test
    fun convert_1_acre_to_square_meter() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            AreaConverter.Unit.Acre as Converter.Unit
        )
        val result = converter.convert(input, AreaConverter.Unit.SquareMeter)

        assertEquals(BigDecimal("4046.8564224"), result.value.setScale(7, RoundingMode.HALF_UP))
    }
}
