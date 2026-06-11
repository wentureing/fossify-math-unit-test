package org.fossify.math.activities

import org.fossify.math.R

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.fossify.commons.helpers.IS_CUSTOMIZING_COLORS
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Widget 配置页面 Espresso 集成测试
 * 测试Widget颜色自定义预览计算器、保存按钮和颜色选择器
 */
@RunWith(AndroidJUnit4::class)
class WidgetConfigureActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<WidgetConfigureActivity>

    /**
     * 以颜色自定义模式启动Activity，避免FeatureLockedDialog
     */
    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), WidgetConfigureActivity::class.java).apply {
            putExtra(IS_CUSTOMIZING_COLORS, true)
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
        // 验证保存按钮存在且可见
        onView(withId(R.id.config_save))
            .check(matches(isDisplayed()))

        // 验证背景颜色选择器存在且可见
        onView(withId(R.id.config_bg_color))
            .check(matches(isDisplayed()))

        // 验证文本颜色选择器存在且可见
        onView(withId(R.id.config_text_color))
            .check(matches(isDisplayed()))

        // 验证透明度拖动条存在且可见
        onView(withId(R.id.config_bg_seekbar))
            .check(matches(isDisplayed()))

        println("testEnvironment executed for WidgetConfigureActivity")
    }

    /**
     * 测试预览计算器显示
     * 验证配置页面中的计算器预览包含关键按钮
     */
    @Test
    fun testCalculatorPreviewButtons() {
        // 验证预览计算器的关键按钮存在且可见
        onView(withId(R.id.btn_0)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_1)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_plus)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_multiply)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_divide)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_clear)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_equals)).check(matches(isDisplayed()))

        println("testCalculatorPreviewButtons executed")
    }

    /**
     * 测试预览计算公式和结果显示
     * 验证initVariables设置的预览数据显示正确
     */
    @Test
    fun testPreviewFormulaAndResult() {
        // 验证预览公式显示 "15,937*5"
        onView(withId(R.id.formula))
            .check(matches(withText("15,937*5")))
            .check(matches(isDisplayed()))

        // 验证预览结果显示 "79,685"
        onView(withId(R.id.result))
            .check(matches(withText("79,685")))
            .check(matches(isDisplayed()))

        println("testPreviewFormulaAndResult executed")
    }

    /**
     * 测试Widget背景可见
     */
    @Test
    fun testWidgetBackgroundVisible() {
        onView(withId(R.id.widget_background))
            .check(matches(isDisplayed()))

        println("testWidgetBackgroundVisible executed")
    }
}
