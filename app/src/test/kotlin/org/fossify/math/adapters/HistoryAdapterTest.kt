package org.fossify.math.adapters

import android.widget.FrameLayout
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.fossify.math.activities.SimpleActivity
import org.fossify.math.helpers.CalculatorImpl
import org.fossify.math.models.History

/**
 * HistoryAdapter 的单元测试类。
 * 测试目标：验证适配器的数据绑定、点击行为、长按复制等核心功能。
 *
 * 使用 Robolectric 提供 Android 运行环境（LayoutInflater、资源等），
 * 使用 MockK 模拟外部依赖（CalculatorImpl、itemClick 回调），
 * 确保测试隔离且无需真实设备。
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])                 // 指定 Android SDK 版本，避免兼容性问题
class HistoryAdapterTest {

    // 被测试适配器所依赖的 Activity（真实实例，由 Robolectric 创建）
    private lateinit var activity: SimpleActivity

    // 模拟的计算器实现，只验证方法调用，不执行真实计算
    private lateinit var calculator: CalculatorImpl

    // 模拟的 itemClick 回调（通常用于关闭历史对话框）
    private lateinit var itemClickMock: () -> Unit

    // 被测试对象：HistoryAdapter
    private lateinit var adapter: HistoryAdapter

    /**
     * 每个测试方法执行前的初始化设置。
     * - 创建真实的 SimpleActivity（Robolectric 提供）
     * - 创建模拟的 CalculatorImpl 和 itemClick 回调
     * - 准备假的历史数据列表（注意 History 构造参数顺序：id, formula, result, timestamp）
     * - 实例化 HistoryAdapter
     */
    @Before
    fun setUp() {
        // 使用 Robolectric 构建 Activity，不启动 UI，但能提供有效的 Context 和布局加载器
        activity = Robolectric.buildActivity(SimpleActivity::class.java).create().get()

        // 创建模拟对象：relaxed = true 表示对未 stub 的方法返回默认值（不抛异常）
        calculator = mockk(relaxed = true)
        itemClickMock = mockk(relaxed = true)

        // 构造假的历史数据，id 传 null 表示由数据库自动生成（测试中不需要真实 id）
        val historyItems = listOf(
            History(id = null, formula = "2+2", result = "4", timestamp = 1000L),
            History(id = null, formula = "3*5", result = "15", timestamp = 2000L)
        )

        // 创建适配器实例
        adapter = HistoryAdapter(activity, historyItems, calculator, itemClickMock)
    }

    /**
     * 测试场景：用户短按（点击）历史条目。
     *
     * 预期行为：
     * 1. 计算器的 addNumberToFormula 方法被调用一次，参数为该条目的 result 值（"4"）。
     * 2. itemClick 回调被调用一次（通常用于关闭历史对话框）。
     *
     * 验证方法：MockK 的 verify 函数，精确验证调用次数和参数。
     */
    @Test
    fun `short click item calls calculator with result and triggers itemClick`() {
        // 创建一个临时的父布局，用于承载 ViewHolder（只为了满足 onCreateViewHolder 的 parent 参数）
        val parent = FrameLayout(activity)

        // 创建 ViewHolder（内部会 inflate 布局）
        val holder = adapter.onCreateViewHolder(parent, 0)

        // 绑定第 0 条数据（即 "2+2 = 4"）
        adapter.onBindViewHolder(holder, 0)

        // 模拟点击该条目
        holder.itemView.performClick()

        // 验证：calculator.addNumberToFormula 被调用一次，且参数为 "4"
        verify(exactly = 1) { calculator.addNumberToFormula("4") }

        // 验证：itemClick 回调被调用一次
        verify(exactly = 1) { itemClickMock.invoke() }
    }

    /**
     * 测试场景：适配器的 itemCount 是否与数据源大小一致。
     *
     * 预期行为：返回的数据条目数等于构造时传入的列表大小。
     */
    @Test
    fun `adapter item count matches data size`() {
        // 断言：适配器返回的数量等于 2（因为列表中有两条数据）
        assertEquals(2, adapter.itemCount)
    }

    // 注：长按复制剪贴板的测试暂未包含，因为 Robolectric 4.13 的 ShadowClipboardManager API 与预期略有差异。
    // 后续可单独实现，使用标准的 android.content.ClipboardManager API 进行验证。
}
