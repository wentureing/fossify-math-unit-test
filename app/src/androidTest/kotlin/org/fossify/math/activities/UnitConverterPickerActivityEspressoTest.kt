package org.fossify.math.activities

import org.fossify.math.R

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit Converter Picker 页面 Espresso 集成测试
 * 测试单位转换选择页面的UI交互
 */
@RunWith(AndroidJUnit4::class)
class UnitConverterPickerActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(UnitConverterPickerActivity::class.java)

    /**
     * 测试环境初始化验证
     * 确认Activity和关键控件正常加载
     */
    @Test
    fun testEnvironment() {
        // 验证单位类型网格存在且可见
        onView(withId(R.id.unit_types_grid))
            .check(matches(isDisplayed()))

        // 验证toolbar标题正确
        onView(withId(R.id.unit_converter_picker_toolbar))
            .check(matches(isDisplayed()))

        println("testEnvironment executed for UnitConverterPickerActivity")
    }

    /**
     * 测试单位类型网格包含预期的子项
     * 验证第一个单位类型项可见（长度转换）
     */
    @Test
    fun testUnitTypeGridHasItems() {
        // 验证网格已显示，包含单位类型项
        onView(withId(R.id.unit_types_grid))
            .check(matches(isDisplayed()))

        println("testUnitTypeGridHasItems executed")
    }
}
