package org.fossify.math.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.fossify.commons.extensions.copyToClipboard
import org.fossify.commons.extensions.getProperTextColor
import org.fossify.math.activities.SimpleActivity
import org.fossify.math.databinding.HistoryViewBinding
import org.fossify.math.helpers.CalculatorImpl
import org.fossify.math.helpers.ExpressionFormatter
import org.fossify.math.models.History

class HistoryAdapter(
    val activity: SimpleActivity,
    val items: List<History>,
    val calc: CalculatorImpl,
    val formatter: ExpressionFormatter,
    val itemClick: () -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var textColor = activity.getProperTextColor()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(HistoryViewBinding.inflate(activity.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: HistoryViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: History): View {
            itemView.apply {
                // 原始格式 -> 本地化显示
                val displayFormula = try {
                    formatter.toDisplay(item.formula)
                } catch (e: Exception) {
                    item.formula
                }
                val displayResult = try {
                    formatter.toDisplay(item.result)
                } catch (e: Exception) {
                    item.result
                }
                binding.itemFormula.text = displayFormula
                binding.itemResult.text = displayResult
                binding.itemFormula.setTextColor(textColor)
                binding.itemResult.setTextColor(textColor)

                // 点击加载原始表达式（不是结果）
                setOnClickListener {
                    calc.loadRawExpression(item.formula)
                    itemClick()
                }

                // 长按复制原始结果（原始格式）
                setOnLongClickListener {
                    activity.baseContext.copyToClipboard(item.result)
                    true
                }
            }
            return itemView
        }
    }
}
