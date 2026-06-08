package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TemperatureConverterTest {
    private val converter = TemperatureConverter

    @Test
    fun convert_celsius_to_kelvin() {
        val input = ValueWithUnit(
            BigDecimal("0"),
            TemperatureConverter.Unit.Celsius as Converter.Unit
        )
        val result = converter.convert(input, TemperatureConverter.Unit.Kelvin)

        assertEquals(0, result.value.compareTo(BigDecimal("273.15")))

        val input100 = ValueWithUnit(
            BigDecimal("100"),
            TemperatureConverter.Unit.Celsius as Converter.Unit
        )
        val result100 = converter.convert(input100, TemperatureConverter.Unit.Kelvin)
        assertEquals(0, result100.value.compareTo(BigDecimal("373.15")))
    }

    @Test
    fun convert_kelvin_to_celsius() {
        val input = ValueWithUnit(
            BigDecimal("0"),
            TemperatureConverter.Unit.Kelvin as Converter.Unit
        )
        val result = converter.convert(input, TemperatureConverter.Unit.Celsius)

        assertEquals(0, result.value.compareTo(BigDecimal("-273.15")))
    }

    @Test
    fun convert_celsius_to_fahrenheit() {
        val inputFreezing = ValueWithUnit(
            BigDecimal("0"),
            TemperatureConverter.Unit.Celsius as Converter.Unit
        )
        val resultFreezing = converter.convert(inputFreezing, TemperatureConverter.Unit.Fahrenheit)
        assertEquals(0, resultFreezing.value.compareTo(BigDecimal("32")))

        val inputBoiling = ValueWithUnit(
            BigDecimal("100"),
            TemperatureConverter.Unit.Celsius as Converter.Unit
        )
        val resultBoiling = converter.convert(inputBoiling, TemperatureConverter.Unit.Fahrenheit)
        assertEquals(0, resultBoiling.value.compareTo(BigDecimal("212")))
    }

    @Test
    fun convert_fahrenheit_to_celsius() {
        val input32 = ValueWithUnit(
            BigDecimal("32"),
            TemperatureConverter.Unit.Fahrenheit as Converter.Unit
        )
        val result32 = converter.convert(input32, TemperatureConverter.Unit.Celsius)
        assertEquals(0, result32.value.compareTo(BigDecimal.ZERO))

        val input212 = ValueWithUnit(
            BigDecimal("212"),
            TemperatureConverter.Unit.Fahrenheit as Converter.Unit
        )
        val result212 = converter.convert(input212, TemperatureConverter.Unit.Celsius)
        assertEquals(0, result212.value.compareTo(BigDecimal("100")))
    }
}
