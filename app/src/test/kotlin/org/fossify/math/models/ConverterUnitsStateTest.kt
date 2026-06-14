package org.fossify.math.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ConverterUnitsStateTest {

    @Test
    fun constructor_stores_top_and_bottom_units() {
        val topUnit = org.fossify.math.helpers.converters.LengthConverter.Unit.Meter
        val bottomUnit = org.fossify.math.helpers.converters.LengthConverter.Unit.Kilometer
        val state = ConverterUnitsState(topUnit, bottomUnit)
        assertEquals(topUnit, state.topUnit)
        assertEquals(bottomUnit, state.bottomUnit)
    }

    @Test
    fun data_class_equality_same_units() {
        val topUnit = org.fossify.math.helpers.converters.MassConverter.Unit.Kilogram
        val bottomUnit = org.fossify.math.helpers.converters.MassConverter.Unit.Gram
        val state1 = ConverterUnitsState(topUnit, bottomUnit)
        val state2 = ConverterUnitsState(topUnit, bottomUnit)
        assertEquals(state1, state2)
    }

    @Test
    fun data_class_inequality_different_top_unit() {
        val state1 = ConverterUnitsState(
            org.fossify.math.helpers.converters.LengthConverter.Unit.Meter,
            org.fossify.math.helpers.converters.LengthConverter.Unit.Kilometer
        )
        val state2 = ConverterUnitsState(
            org.fossify.math.helpers.converters.LengthConverter.Unit.Centimeter,
            org.fossify.math.helpers.converters.LengthConverter.Unit.Kilometer
        )
        assertNotEquals(state1, state2)
    }

    @Test
    fun data_class_inequality_different_bottom_unit() {
        val state1 = ConverterUnitsState(
            org.fossify.math.helpers.converters.LengthConverter.Unit.Meter,
            org.fossify.math.helpers.converters.LengthConverter.Unit.Kilometer
        )
        val state2 = ConverterUnitsState(
            org.fossify.math.helpers.converters.LengthConverter.Unit.Meter,
            org.fossify.math.helpers.converters.LengthConverter.Unit.Centimeter
        )
        assertNotEquals(state1, state2)
    }

    @Test
    fun data_class_copy_preserves_bottom_unit() {
        val topUnit = org.fossify.math.helpers.converters.VolumeConverter.Unit.Liter
        val bottomUnit = org.fossify.math.helpers.converters.VolumeConverter.Unit.Milliliter
        val state = ConverterUnitsState(topUnit, bottomUnit)
        val copied = state.copy(topUnit = org.fossify.math.helpers.converters.VolumeConverter.Unit.GallonUS)
        assertEquals(bottomUnit, copied.bottomUnit)
        assertEquals(org.fossify.math.helpers.converters.VolumeConverter.Unit.GallonUS, copied.topUnit)
    }

    @Test
    fun data_class_copy_preserves_top_unit() {
        val topUnit = org.fossify.math.helpers.converters.AreaConverter.Unit.SquareMeter
        val bottomUnit = org.fossify.math.helpers.converters.AreaConverter.Unit.SquareKilometer
        val state = ConverterUnitsState(topUnit, bottomUnit)
        val copied = state.copy(bottomUnit = org.fossify.math.helpers.converters.AreaConverter.Unit.Hectare)
        assertEquals(topUnit, copied.topUnit)
        assertEquals(org.fossify.math.helpers.converters.AreaConverter.Unit.Hectare, copied.bottomUnit)
    }

    @Test
    fun data_class_same_top_and_bottom_units() {
        val unit = org.fossify.math.helpers.converters.LengthConverter.Unit.Meter
        val state = ConverterUnitsState(unit, unit)
        assertEquals(unit, state.topUnit)
        assertEquals(unit, state.bottomUnit)
    }

    @Test
    fun data_class_with_energy_converter_units() {
        val state = ConverterUnitsState(
            org.fossify.math.helpers.converters.EnergyConverter.Unit.Kilocalorie,
            org.fossify.math.helpers.converters.EnergyConverter.Unit.Kilojoule
        )
        assertEquals(org.fossify.math.helpers.converters.EnergyConverter.Unit.Kilocalorie, state.topUnit)
        assertEquals(org.fossify.math.helpers.converters.EnergyConverter.Unit.Kilojoule, state.bottomUnit)
    }

    @Test
    fun data_class_with_pressure_converter_units() {
        val state = ConverterUnitsState(
            org.fossify.math.helpers.converters.PressureConverter.Unit.Bar,
            org.fossify.math.helpers.converters.PressureConverter.Unit.Psi
        )
        assertEquals(org.fossify.math.helpers.converters.PressureConverter.Unit.Bar, state.topUnit)
        assertEquals(org.fossify.math.helpers.converters.PressureConverter.Unit.Psi, state.bottomUnit)
    }

    @Test
    fun data_class_with_temperature_converter_units() {
        val state = ConverterUnitsState(
            org.fossify.math.helpers.converters.TemperatureConverter.Unit.Celsius,
            org.fossify.math.helpers.converters.TemperatureConverter.Unit.Fahrenheit
        )
        assertEquals(org.fossify.math.helpers.converters.TemperatureConverter.Unit.Celsius, state.topUnit)
        assertEquals(org.fossify.math.helpers.converters.TemperatureConverter.Unit.Fahrenheit, state.bottomUnit)
    }

    @Test
    fun data_class_with_speed_converter_units() {
        val state = ConverterUnitsState(
            org.fossify.math.helpers.converters.SpeedConverter.Unit.KilometerPerHour,
            org.fossify.math.helpers.converters.SpeedConverter.Unit.MilePerHour
        )
        assertEquals(org.fossify.math.helpers.converters.SpeedConverter.Unit.KilometerPerHour, state.topUnit)
        assertEquals(org.fossify.math.helpers.converters.SpeedConverter.Unit.MilePerHour, state.bottomUnit)
    }

    @Test
    fun data_class_with_time_converter_units() {
        val state = ConverterUnitsState(
            org.fossify.math.helpers.converters.TimeConverter.Unit.Hour,
            org.fossify.math.helpers.converters.TimeConverter.Unit.Minute
        )
        assertEquals(org.fossify.math.helpers.converters.TimeConverter.Unit.Hour, state.topUnit)
        assertEquals(org.fossify.math.helpers.converters.TimeConverter.Unit.Minute, state.bottomUnit)
    }
}
