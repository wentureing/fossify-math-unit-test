package org.fossify.math.activities

import org.fossify.math.R

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit Converter 页面 Espresso 集成测试
 * 测试单位转换页面的输入、清除和单位选择功能
 */
@RunWith(AndroidJUnit4::class)
class UnitConverterActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<UnitConverterActivity>

    /**
     * 使用长度转换器（ID=0）启动Activity
     */
    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), UnitConverterActivity::class.java).apply {
            putExtra(UnitConverterActivity.EXTRA_CONVERTER_ID, 0) // LengthConverter
        }
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    /**
     * 测试环境初始化验证
     * 确认Activity和关键控件正常加载
     */
    @Test
    fun testEnvironment() {
        // 验证数字键盘关键按钮存在且可见
        onView(withId(R.id.btn_0)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_1)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_5)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_9)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_clear)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_decimal)).check(matches(isDisplayed()))

        // 验证toolbar存在且可见
        onView(withId(R.id.unit_converter_toolbar))
            .check(matches(isDisplayed()))

        println("testEnvironment executed for UnitConverterActivity")
    }

    /**
     * 测试数字输入
     * 点击数字1, 2, 3 → 验证上方输入区域显示"123"
     */
    @Test
    fun testNumpadInput() {
        clickView(R.id.btn_1)
        clickView(R.id.btn_2)
        clickView(R.id.btn_3)

        // 验证top_unit_text显示输入内容
        onView(withId(R.id.top_unit_text))
            .check(matches(withText("123")))

        println("testNumpadInput executed, top_unit_text=123")
    }

    /**
     * 测试清除按钮
     * 输入"45" → 点击清除 → 结果应为"4"
     */
    @Test
    fun testClearSingleDigit() {
        clickView(R.id.btn_4)
        clickView(R.id.btn_5)
        clickView(R.id.btn_clear)

        onView(withId(R.id.top_unit_text))
            .check(matches(withText("4")))

        println("testClearSingleDigit executed, result=4")
    }

    /**
     * 测试长按清除按钮：清空全部内容
     * 输入"789" → 长按清除 → 输入区域重置为"0"
     */
    @Test
    fun testClearAllOnLongPress() {
        clickView(R.id.btn_7)
        clickView(R.id.btn_8)
        clickView(R.id.btn_9)

        // 长按清除按钮
        onView(withId(R.id.btn_clear))
            .perform(longClick())

        // 断言输入区域重置为默认值"0"
        onView(withId(R.id.top_unit_text))
            .check(matches(withText("0")))

        println("testClearAllOnLongPress executed")
    }

    /**
     * 测试底部转换结果显示
     * 输入数字后验证底部结果区域有值
     */
    @Test
    fun testConversionResultDisplayed() {
        // 输入数字"1"
        clickView(R.id.btn_1)

        // 底部转换结果应该有显示（自动转换）
        onView(withId(R.id.bottom_unit_text))
            .check(matches(isDisplayed()))

        println("testConversionResultDisplayed executed")
    }

    /**
     * 封装点击操作
     */
    private fun clickView(id: Int) {
        onView(withId(id))
            .perform(click())
    }
}
