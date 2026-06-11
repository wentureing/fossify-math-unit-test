package org.fossify.math.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Settings 页面 Espresso 集成测试（Compose UI）
 * SettingsActivity 使用 Jetpack Compose 构建UI，
 * 无传统View ID，通过isRoot验证根视图渲染
 */
@RunWith(AndroidJUnit4::class)
class SettingsActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsActivity::class.java)

    /**
     * 测试环境初始化验证
     * 确认Activity正常启动，根视图可见
     */
    @Test
    fun testEnvironment() {
        // 验证Activity根视图已渲染
        onView(isRoot())
            .check(matches(isDisplayed()))

        println("testEnvironment executed for SettingsActivity")
    }
}
