package org.fossify.math.adapters

import android.content.ClipboardManager
import android.content.Context
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowClipboardManager
import org.robolectric.shadows.ShadowLooper
import org.fossify.math.activities.SimpleActivity
import org.fossify.math.helpers.CalculatorImpl
import org.fossify.math.models.History

@RunWith(RobolectricTestRunner::class)
class HistoryAdapterSimpleTest {

    private lateinit var activity: SimpleActivity
    private lateinit var calculator: CalculatorImpl
    private lateinit var itemClickCallback: () -> Unit
    private lateinit var adapter: HistoryAdapter
    private lateinit var shadowClipboard: ShadowClipboardManager

    @Before
    fun setup() {
        // 使用真实 Activity（Robolectric 提供），避免 mock 复杂方法
        activity = RuntimeEnvironment.application.let { app ->
            SimpleActivity().apply {
                attachBaseContext(app)
            }
        }
        calculator = mockk(relaxed = true)
        itemClickCallback = mockk(relaxed = true)

        val historyItems = listOf(
            History("2+2", "4", System.currentTimeMillis()),   // 注意参数顺序: formula, result, timestamp
            History("3*5", "15", System.currentTimeMillis())
        )
        adapter = HistoryAdapter(activity, historyItems, calculator, itemClickCallback)

        // 获取剪贴板的 shadow 用于验证长按复制
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        shadowClipboard = ShadowClipboardManager.getInstance(clipboard) as ShadowClipboardManager
        shadowClipboard.clear()
    }

    @Test
    fun `short click item calls calculator with result and triggers itemClick`() {
        // 模拟 RecyclerView 测量布局（让 ViewHolder 可以点击）
        val parent = FrameLayout(activity)
        val holder = adapter.onCreateViewHolder(parent, 0)
        adapter.onBindViewHolder(holder, 0)

        // 执行点击
        holder.itemView.performClick()

        verify(exactly = 1) { calculator.addNumberToFormula("4") }
        verify(exactly = 1) { itemClickCallback.invoke() }
    }

    @Test
    fun `long click item copies result to clipboard`() {
        val parent = FrameLayout(activity)
        val holder = adapter.onCreateViewHolder(parent, 0)
        adapter.onBindViewHolder(holder, 0)

        holder.itemView.performLongClick()

        assertEquals("4", shadowClipboard.text)
    }

    @Test
    fun `adapter item count matches data size`() {
        assertEquals(2, adapter.itemCount)
    }
}
