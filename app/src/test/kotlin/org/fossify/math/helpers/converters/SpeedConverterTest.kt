package org.fossify.math.helpers.converters

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class SpeedConverterTest {
    private val converter = SpeedConverter

    // 测试1：零值任何转换都返回零
    @Test
    fun convert_zero_value_always_returns_zero() {
        val input = ValueWithUnit(
            BigDecimal.ZERO,
            SpeedConverter.Unit.MeterPerSecond as Converter.Unit
        )
        val result = converter.convert(input, SpeedConverter.Unit.KilometerPerHour)

        // ✅ 正确比较BigDecimal：只比较数值，忽略scale
        assertEquals(0, result.value.compareTo(BigDecimal.ZERO))
    }

    // 测试2：1 米/秒 → 3.6 千米/小时
    @Test
    fun convert_1_meter_per_second_to_kilometer_per_hour() {
        val input = ValueWithUnit(
            BigDecimal.ONE,
            SpeedConverter.Unit.MeterPerSecond as Converter.Unit
        )
        val result = converter.convert(input, SpeedConverter.Unit.KilometerPerHour)

        // 统一scale到1位小数后比较
        assertEquals(BigDecimal("3.6"), result.value.setScale(1, RoundingMode.HALF_UP))
    }

    // 测试3：100 千米/小时 → 约 62.14 英里/小时
    @Test
    fun convert_100_kilometer_per_hour_to_mile_per_hour() {
        val input = ValueWithUnit(
            BigDecimal("100"),
            SpeedConverter.Unit.KilometerPerHour as Converter.Unit
        )
        val result = converter.convert(input, SpeedConverter.Unit.MilePerHour)

        // 统一scale到2位小数后比较
        assertEquals(BigDecimal("62.14"), result.value.setScale(2, RoundingMode.HALF_UP))
    }
}
