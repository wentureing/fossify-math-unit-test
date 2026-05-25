package org.fossify.math.helpers

import android.content.Context
import io.mockk.*
import org.fossify.math.R
import org.junit.After
import org.junit.Before
import org.junit.Test

class CalculatorImplOnlyNumberFormatHelperMockTest {

    private lateinit var calculatorMock: Calculator
    private lateinit var contextMock: Context
    private lateinit var calculatorImpl: CalculatorImpl

    @Before
    fun setUp() {
        calculatorMock = mockk(relaxed = true)
        contextMock = mockk(relaxed = true)

        // 只 mock NumberFormatHelper 的构造函数
        mockkConstructor(NumberFormatHelper::class)

        // 让 formatForDisplay 返回原字符串（不进行真实格式化）
        every { anyConstructed<NumberFormatHelper>().formatForDisplay(any()) } answers { arg(0) }
        // 提供 decimalSeparator 和 groupingSeparator 的默认值，避免真实访问 Locale
        every { anyConstructed<NumberFormatHelper>().decimalSeparator } returns "."
        every { anyConstructed<NumberFormatHelper>().groupingSeparator } returns ","

        // HistoryHelper 和 Expression 没有 mock，但在这个测试中不会被调用
        calculatorImpl = CalculatorImpl(calculatorMock, contextMock)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testDigitInputCallsNumberFormatHelper() {
        // 执行：按下数字 1
        calculatorImpl.numpadClicked(R.id.btn_1)

        // 验证：NumberFormatHelper.formatForDisplay 被调用（因为 addThousandsDelimiter 内部会调用）
        verify(exactly = 1) { anyConstructed<NumberFormatHelper>().formatForDisplay("1") }

        // 验证：最终结果正确显示
        verify(exactly = 1) { calculatorMock.showNewResult("1", contextMock) }
    }

    @Test
    fun testDecimalInputCallsNumberFormatHelper() {
        calculatorImpl.numpadClicked(R.id.btn_1)
        calculatorImpl.numpadClicked(R.id.btn_decimal)
        calculatorImpl.numpadClicked(R.id.btn_2)

        // formatForDisplay 会被调用多次（每次 addThousandsDelimiter 都会调用）
        verify(atLeast = 1) { anyConstructed<NumberFormatHelper>().formatForDisplay(any()) }
        verify { calculatorMock.showNewResult("1.2", contextMock) }
    }
}
