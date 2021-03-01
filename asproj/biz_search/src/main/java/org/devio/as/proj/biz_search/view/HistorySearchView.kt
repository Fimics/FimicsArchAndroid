package org.devio.`as`.proj.biz_search.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.layout_history_search.view.*
import org.devio.`as`.proj.biz_search.KeyWord
import org.devio.`as`.proj.biz_search.R
import java.util.*
import kotlin.collections.ArrayList

class HistorySearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val keywords = ArrayList<KeyWord>()

    init {

        LayoutInflater.from(context).inflate(R.layout.layout_history_search, this, true)
        orientation = LinearLayout.VERTICAL
    }

    fun bindData(histories: ArrayList<KeyWord>) {
        if (histories == null) return
        keywords.clear()
        keywords.addAll(histories)

        for (index in 0 until histories.size) {
            var chipItem: Chip
            val childCount = chip_group.childCount
            if (index < childCount) {
                chipItem = chip_group.getChildAt(index) as Chip
            } else {
                chipItem = generateChipItem()
                chip_group.addView(chipItem)
            }
            chipItem.text = histories[index].keyWord
        }
    }

    private fun generateChipItem(): Chip {
        val chipItem: Chip = LayoutInflater.from(context)
            .inflate(R.layout.layout_history_search_chip_item, chip_group, false) as Chip
        chipItem.isCheckable = true
        chipItem.isChecked = false
        chipItem.id = chip_group.childCount
        return chipItem
    }

    fun setOnCheckedChangedListener(callback: (KeyWord) -> Unit) {
        chip_group.setOnCheckedChangeListener { chipGroup, checkedId ->
            for (index in 0 until chipGroup.childCount) {
                val childAt = chip_group.getChildAt(index) as AppCompatCheckBox
                if (childAt.id == checkedId && childAt.isChecked) {
                    //chipItem 具有状态，被选中后，再次点击会被切换到未选中态，此时回调的checkedId=-1
                    //我们为了让 一个chipItem能够重复多次点击，所以选中后，主动清除chip_group的选择标记
                    callback(keywords[index])
                    chip_group.clearCheck()
                    break
                }
            }
        }
    }

    fun setOnHistoryClearListener(callback: () -> Unit) {
        if_delete.setOnClickListener {
            chip_group.removeAllViews()
            keywords.clear()
            callback()
        }
    }

}