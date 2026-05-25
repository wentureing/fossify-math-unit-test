package org.fossify.math.activities

import android.os.Looper
import android.view.View
import android.widget.TextView
import org.fossify.math.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
@LooperMode(LooperMode.Mode.PAUSED)
class MainActivitySimpleTest {

    private lateinit var activity: MainActivity
    private lateinit var resultView: TextView

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .get()

        // 确保主线程 Looper 中所有初始化消息（如有）被执行
        shadowOf(Looper.getMainLooper()).idle()

        resultView = activity.findViewById(R.id.result)
        // 验证初始状态，覆盖 Activity 初始化逻辑
        assertEquals("0", resultView.text.toString())
    }

    @After
    fun tearDown() {
        activity.finish()
        // 清理可能残留的消息，避免影响后续测试
        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun testEnvironment() {
        // 真正验证 Activity 和关键控件非空，覆盖环境初始化
        assertNotNull(activity)
        assertNotNull(resultView)
        assertTrue(activity.findViewById<View>(R.id.btn_1) != null)
    }

    @Test
    fun testAddTwoNumbers() {
        clickView(R.id.btn_1)
        clickView(R.id.btn_plus)
        clickView(R.id.btn_2)
        clickView(R.id.btn_equals)

        // 使用缓存的 resultView，确保获取最新 UI 结果
        assertEquals("3", resultView.text.toString())

        println("testAddTwoNumbers executed, result=${resultView.text}")
    }

    //乘法测试
    @Test
    fun testMultiplyTwoNumbers() {
        clickView(R.id.btn_2)
        clickView(R.id.btn_multiply) // 乘法按钮
        clickView(R.id.btn_3)
        clickView(R.id.btn_equals)

        assertEquals("6", resultView.text.toString())
        println("testMultiplyTwoNumbers executed, result=${resultView.text}")
    }


    //除法测试
    @Test
    fun testDivideTwoNumbers() {
        clickView(R.id.btn_6)
        clickView(R.id.btn_divide) // 除法按钮
        clickView(R.id.btn_2)
        clickView(R.id.btn_equals)

        assertEquals("3", resultView.text.toString())
        println("testDivideTwoNumbers executed, result=${resultView.text}")
    }

    @Test
    fun testClear() {
        clickView(R.id.btn_1)
        clickView(R.id.btn_2)
        clickView(R.id.btn_clear)

        // 预期清除最后一个数字，剩余 "1"（依赖于 CalculatorImpl 实现）
        assertEquals("1", resultView.text.toString())
        println("testClear executed, result=${resultView.text}")
    }


    @Test
    fun testClear2() {
        clickView(R.id.btn_2)
        clickView(R.id.btn_1)
        clickView(R.id.btn_clear)

        // 预期清除最后一个数字，剩余 "1"（依赖于 CalculatorImpl 实现）
        assertEquals("2", resultView.text.toString())
        println("testClear executed, result=${resultView.text}")
    }

    // 封装点击逻辑：使用 View 类型避免类型转换异常
    private fun clickView(id: Int) {
        val view = activity.findViewById<View>(id)
        view.performClick()
        // 执行因点击而可能产生的主线程消息（如 UI 更新）
        shadowOf(Looper.getMainLooper()).idle()
    }
}
