package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class ConverterBaseTest {

    // ========== ValueWithUnit 数据类测试 ==========

    @Test
    fun value_with_unit_stores_value_correctly() {
        val vwu = ValueWithUnit(BigDecimal("42.5"), AreaConverter.Unit.SquareMeter)
        assertEquals(0, vwu.value.compareTo(BigDecimal("42.5")))
    }

    @Test
    fun value_with_unit_stores_unit_correctly() {
        val vwu = ValueWithUnit(BigDecimal("10"), LengthConverter.Unit.Meter)
        assertEquals(LengthConverter.Unit.Meter, vwu.unit)
    }

    @Test
    fun value_with_unit_data_class_equality() {
        val vwu1 = ValueWithUnit(BigDecimal("100"), VolumeConverter.Unit.Liter)
        val vwu2 = ValueWithUnit(BigDecimal("100"), VolumeConverter.Unit.Liter)
        assertEquals(vwu1, vwu2)
    }

    @Test
    fun value_with_unit_data_class_copy() {
        val original = ValueWithUnit(BigDecimal("5"), MassConverter.Unit.Kilogram)
        val copied = original.copy(value = BigDecimal("10"))
        assertEquals(0, copied.value.compareTo(BigDecimal("10")))
        assertEquals(MassConverter.Unit.Kilogram, copied.unit)
    }

    // ========== Converter.Unit.withValue 测试 ==========

    @Test
    fun unit_with_value_creates_correct_value_with_unit() {
        val vwu = LengthConverter.Unit.Kilometer.withValue(BigDecimal("3"))
        assertEquals(0, vwu.value.compareTo(BigDecimal("3")))
        assertEquals(LengthConverter.Unit.Kilometer, vwu.unit)
    }

    @Test
    fun unit_with_value_for_mass() {
        val vwu = MassConverter.Unit.Gram.withValue(BigDecimal("500"))
        assertEquals(0, vwu.value.compareTo(BigDecimal("500")))
        assertEquals(MassConverter.Unit.Gram, vwu.unit)
    }

    // ========== Converter.ALL 列表测试 ==========

    @Test
    fun all_converters_contains_nine_entries() {
        assertEquals(9, Converter.ALL.size)
    }

    @Test
    fun all_converters_contains_length_converter() {
        assertTrue(Converter.ALL.contains(LengthConverter))
    }

    @Test
    fun all_converters_contains_area_converter() {
        assertTrue(Converter.ALL.contains(AreaConverter))
    }

    @Test
    fun all_converters_contains_volume_converter() {
        assertTrue(Converter.ALL.contains(VolumeConverter))
    }

    @Test
    fun all_converters_contains_mass_converter() {
        assertTrue(Converter.ALL.contains(MassConverter))
    }

    @Test
    fun all_converters_contains_temperature_converter() {
        assertTrue(Converter.ALL.contains(TemperatureConverter))
    }

    @Test
    fun all_converters_contains_time_converter() {
        assertTrue(Converter.ALL.contains(TimeConverter))
    }

    @Test
    fun all_converters_contains_speed_converter() {
        assertTrue(Converter.ALL.contains(SpeedConverter))
    }

    @Test
    fun all_converters_contains_pressure_converter() {
        assertTrue(Converter.ALL.contains(PressureConverter))
    }

    @Test
    fun all_converters_contains_energy_converter() {
        assertTrue(Converter.ALL.contains(EnergyConverter))
    }

    // ========== 转换器基本属性测试 ==========

    @Test
    fun each_converter_has_non_empty_units() {
        Converter.ALL.forEach { converter ->
            assertTrue(
                "${converter.key} should have at least 2 units",
                converter.units.size >= 2
            )
        }
    }

    @Test
    fun each_converter_has_default_top_unit() {
        Converter.ALL.forEach { converter ->
            assertNotNull("${converter.key} should have a defaultTopUnit", converter.defaultTopUnit)
            assertTrue(
                "${converter.key}'s defaultTopUnit should be in units list",
                converter.units.contains(converter.defaultTopUnit)
            )
        }
    }

    @Test
    fun each_converter_has_default_bottom_unit() {
        Converter.ALL.forEach { converter ->
            assertNotNull("${converter.key} should have a defaultBottomUnit", converter.defaultBottomUnit)
            assertTrue(
                "${converter.key}'s defaultBottomUnit should be in units list",
                converter.units.contains(converter.defaultBottomUnit)
            )
        }
    }

    @Test
    fun each_converter_has_non_empty_key() {
        Converter.ALL.forEach { converter ->
            assertTrue(
                "${converter.key} should have a non-empty key",
                converter.key.isNotEmpty()
            )
        }
    }

    @Test
    fun each_converter_has_unique_key() {
        val keys = Converter.ALL.map { it.key }
        assertEquals(
            "All converter keys should be unique",
            keys.size, keys.toSet().size
        )
    }

    // ========== Converter.Unit.toBase/fromBase 基础测试 ==========

    @Test
    fun unit_to_base_for_length_meter() {
        // Meter 的 factor 是 BigDecimal.ONE，所以 toBase 应该返回原值
        val result = LengthConverter.Unit.Meter.toBase(BigDecimal("5"))
        assertEquals(0, result.compareTo(BigDecimal("5")))
    }

    @Test
    fun unit_from_base_for_length_kilometer() {
        // 1000 meter = 1 km, 所以 fromBase(1000) = 1
        val result = LengthConverter.Unit.Kilometer.fromBase(BigDecimal("1000"))
        assertEquals(0, result.compareTo(BigDecimal.ONE))
    }

    @Test
    fun unit_to_base_for_length_kilometer() {
        // 1 km = 1000 m (factor = 1000)
        val result = LengthConverter.Unit.Kilometer.toBase(BigDecimal("3"))
        assertEquals(0, result.compareTo(BigDecimal("3000")))
    }

    @Test
    fun unit_to_base_and_from_base_roundtrip() {
        Converter.ALL
            .filter { it != TemperatureConverter } // 温度转换器有偏移量，不是简单的线性换算
            .forEach { converter ->
                converter.units.forEach { unit ->
                    val originalValue = BigDecimal("123.456")
                    val baseValue = unit.toBase(originalValue)
                    val recoveredValue = unit.fromBase(baseValue)
                    assertEquals(
                        "Roundtrip failed for ${converter.key}.${unit.key}",
                        0, originalValue.compareTo(recoveredValue)
                    )
                }
            }
    }

    // ========== convert 方法基础测试 ==========

    @Test
    fun convert_zero_returns_zero_for_all_converters() {
        Converter.ALL.forEach { converter ->
            val topUnit = converter.defaultTopUnit
            val bottomUnit = converter.defaultBottomUnit
            val input = ValueWithUnit(BigDecimal.ZERO, topUnit)
            val result = converter.convert(input, bottomUnit)
            assertEquals(
                "Zero conversion failed for ${converter.key}",
                0, result.value.compareTo(BigDecimal.ZERO)
            )
        }
    }

    @Test
    fun convert_same_unit_returns_same_value_for_all_converters() {
        Converter.ALL.forEach { converter ->
            val unit = converter.defaultTopUnit
            val input = ValueWithUnit(BigDecimal("42.5"), unit)
            val result = converter.convert(input, unit)
            assertEquals(
                "Same unit conversion failed for ${converter.key}",
                0, result.value.compareTo(BigDecimal("42.5"))
            )
        }
    }
}
