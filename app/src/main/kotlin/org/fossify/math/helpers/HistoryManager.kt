package org.fossify.math.helpers

import android.content.Context
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.math.models.History

/**
 * 管理历史记录，存储和返回均为原始格式（无本地化格式）。
 * 利用 [HistoryHelper] 进行数据库操作，不负责格式化。
 * 注：删除历史记录的功能不由此类提供（保留在原调用方，如 HistoryDialog）。
 */
class HistoryManager(private val context: Context) {

    private val historyHelper = HistoryHelper(context)

    /**
     * 添加一条历史记录（异步，去重：最近一条相同公式和结果的记录不重复插入）。
     * @param formulaRaw 原始格式的表达式
     * @param resultRaw 原始格式的结果
     */
    fun addEntry(formulaRaw: String, resultRaw: String) {
        ensureBackgroundThread {
            // 查询最近一条记录，判断是否重复
            historyHelper.getHistory { list ->
                val last = list.firstOrNull()
                if (last != null && last.formula == formulaRaw && last.result == resultRaw) {
                    // 重复，不插入
                    return@getHistory
                }
                // 不重复，插入新记录
                historyHelper.insertOrUpdateHistoryEntry(
                    History(
                        id = null,
                        formula = formulaRaw,
                        result = resultRaw,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    /**
     * 获取所有历史记录（原始格式），按时间倒序（最新的在前）。
     */
    fun getHistory(callback: (List<History>) -> Unit) {
        historyHelper.getHistory(callback)
    }
}
