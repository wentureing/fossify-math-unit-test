package org.fossify.math.activities

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.fossify.commons.activities.CustomizationActivity
import org.fossify.math.extensions.config
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsActivityEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SettingsActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testEnvironment() {
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun testVibrateSwitchToggle() {
        val initialState = getVibrateSetting()

        // 使用 useUnmergedTree = true 定位包含文本且可点击的父节点
        composeTestRule
            .onNode(
                hasAnyDescendant(hasText("按下按钮时振动")) and hasClickAction(),
                useUnmergedTree = true
            )
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        val newState = getVibrateSetting()
        assert(newState == !initialState) { "Vibrate setting should be toggled" }
    }

    @Test
    fun testCustomizeColorsLaunchesActivityAndDisplays() {
        composeTestRule.onNodeWithText("自定义外观")
            .assertExists()
            .performClick()

        // 验证 Intent 启动 CustomizationActivity
        Intents.intended(hasComponent(CustomizationActivity::class.java.name))

        // 等待新 Activity 完全启动并渲染
        composeTestRule.waitForIdle()
        Thread.sleep(1000) // 留出足够时间让页面切换完成

        // 验证 CustomizationActivity 中的关键元素“主题和颜色”可见（证明页面完整加载）
        onView(withText("主题和颜色")).check(matches(isDisplayed()))
    }

    private fun getVibrateSetting(): Boolean {
        var value = false
        composeTestRule.activityRule.scenario.onActivity { activity ->
            value = activity.config.vibrateOnButtonPress
        }
        return value
    }
}
