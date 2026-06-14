package org.fossify.math.helpers

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ConstantsTest {

    @Test
    fun digit_constant_is_correct() {
        assertEquals("digit", DIGIT)
    }


    @Test
    fun equals_constant_is_correct() {
        assertEquals("equals", EQUALS)
    }

    @Test
    fun plus_constant_is_correct() {
        assertEquals("plus", PLUS)
    }

    @Test
    fun minus_constant_is_correct() {
        assertEquals("minus", MINUS)
    }

    @Test
    fun multiply_constant_is_correct() {
        assertEquals("multiply", MULTIPLY)
    }

    @Test
    fun divide_constant_is_correct() {
        assertEquals("divide", DIVIDE)
    }

    @Test
    fun percent_constant_is_correct() {
        assertEquals("percent", PERCENT)
    }

    @Test
    fun power_constant_is_correct() {
        assertEquals("power", POWER)
    }

    @Test
    fun root_constant_is_correct() {
        assertEquals("root", ROOT)
    }

    @Test
    fun decimal_constant_is_correct() {
        assertEquals("decimal", DECIMAL)
    }

    @Test
    fun clear_constant_is_correct() {
        assertEquals("clear", CLEAR)
    }

    @Test
    fun reset_constant_is_correct() {
        assertEquals("reset", RESET)
    }

    // ========== 数字名称常量 ==========

    @Test
    fun nan_constant_is_correct() {
        assertEquals("NaN", NAN)
    }

    @Test
    fun zero_constant_is_correct() {
        assertEquals("zero", ZERO)
    }

    @Test
    fun one_constant_is_correct() {
        assertEquals("one", ONE)
    }

    @Test
    fun two_constant_is_correct() {
        assertEquals("two", TWO)
    }

    @Test
    fun three_constant_is_correct() {
        assertEquals("three", THREE)
    }

    @Test
    fun four_constant_is_correct() {
        assertEquals("four", FOUR)
    }

    @Test
    fun five_constant_is_correct() {
        assertEquals("five", FIVE)
    }

    @Test
    fun six_constant_is_correct() {
        assertEquals("six", SIX)
    }

    @Test
    fun seven_constant_is_correct() {
        assertEquals("seven", SEVEN)
    }

    @Test
    fun eight_constant_is_correct() {
        assertEquals("eight", EIGHT)
    }

    @Test
    fun nine_constant_is_correct() {
        assertEquals("nine", NINE)
    }

    // ========== SharedPrefs 常量 ==========

    @Test
    fun converter_units_prefix_is_correct() {
        assertEquals("converter_last_units", CONVERTER_UNITS_PREFIX)
    }

    // ========== 计算器状态常量 ==========

    @Test
    fun res_constant_is_correct() {
        assertEquals("res", RES)
    }

    @Test
    fun previous_calculation_constant_is_correct() {
        assertEquals("previousCalculation", PREVIOUS_CALCULATION)
    }

    @Test
    fun last_key_constant_is_correct() {
        assertEquals("lastKey", LAST_KEY)
    }

    @Test
    fun last_operation_constant_is_correct() {
        assertEquals("lastOperation", LAST_OPERATION)
    }

    @Test
    fun base_value_constant_is_correct() {
        assertEquals("baseValue", BASE_VALUE)
    }

    @Test
    fun second_value_constant_is_correct() {
        assertEquals("secondValue", SECOND_VALUE)
    }

    @Test
    fun input_displayed_formula_constant_is_correct() {
        assertEquals("inputDisplayedFormula", INPUT_DISPLAYED_FORMULA)
    }

    @Test
    fun calculator_state_constant_is_correct() {
        assertEquals("calculatorState", CALCULATOR_STATE)
    }

    // ========== 转换器状态常量 ==========

    @Test
    fun top_unit_constant_is_correct() {
        assertEquals("top_unit", TOP_UNIT)
    }

    @Test
    fun bottom_unit_constant_is_correct() {
        assertEquals("bottom_unit", BOTTOM_UNIT)
    }

    @Test
    fun converter_value_constant_is_correct() {
        assertEquals("converter_value", CONVERTER_VALUE)
    }

    @Test
    fun converter_state_constant_is_correct() {
        assertEquals("converter_state", CONVERTER_STATE)
    }

    // ========== MATH_CONTEXT 测试 ==========

    @Test
    fun math_context_is_not_null() {
        assertNotNull(MATH_CONTEXT)
    }

    @Test
    fun math_context_is_decimal128() {
        assertEquals(java.math.MathContext.DECIMAL128, MATH_CONTEXT)
    }

    // ========== 所有常量唯一性 ==========

    @Test
    fun all_calculator_button_constants_are_unique() {
        val constants = listOf(DIGIT, EQUALS, PLUS, MINUS, MULTIPLY, DIVIDE,
            PERCENT, POWER, ROOT, DECIMAL, CLEAR, RESET)
        assertEquals(constants.size, constants.toSet().size)
    }

    @Test
    fun all_digit_name_constants_are_unique() {
        val constants = listOf(NAN, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
        assertEquals(constants.size, constants.toSet().size)
    }

    @Test
    fun all_calculator_state_constants_are_unique() {
        val constants = listOf(RES, PREVIOUS_CALCULATION, LAST_KEY, LAST_OPERATION,
            BASE_VALUE, SECOND_VALUE, INPUT_DISPLAYED_FORMULA, CALCULATOR_STATE)
        assertEquals(constants.size, constants.toSet().size)
    }
}
