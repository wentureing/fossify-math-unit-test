package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class VolumeConverterTest {
    private val converter = VolumeConverter

    @Test
    fun convert_1_liter_to_milliliter() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            VolumeConverter.Unit.Liter as Converter.Unit
        )
        val result = converter.convert(input, VolumeConverter.Unit.Milliliter)

        assertEquals(0, result.value.compareTo(BigDecimal("1000")))
    }

    @Test
    fun convert_1_cubic_meter_to_liter() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            VolumeConverter.Unit.CubicMeter as Converter.Unit
        )
        val result = converter.convert(input, VolumeConverter.Unit.Liter)

        assertEquals(0, result.value.compareTo(BigDecimal("1000")))
    }

    @Test
    fun convert_1_gallon_us_to_liter() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            VolumeConverter.Unit.GallonUS as Converter.Unit
        )
        val result = converter.convert(input, VolumeConverter.Unit.Liter)

        assertEquals(BigDecimal("3.785411784"), result.value.setScale(9, RoundingMode.HALF_UP))
    }
}
