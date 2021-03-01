package org.devio.`as`.proj.biz_search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_quick_search_list_item.*
import org.devio.`as`.proj.biz_search.KeyWord
import org.devio.`as`.proj.biz_search.R
import org.devio.hi.ui.item.HiAdapter
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

/**
 * 快搜推荐联想词列表
 */
class QuickSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HiAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(keywords: List<KeyWord>, callback: (KeyWord) -> Unit) {
        val dataItems = mutableListOf<QuickSearchItem>()
        for (keyword in keywords) {
            dataItems.add(QuickSearchItem(keyword, callback))
        }
        val hiAdapter = adapter as HiAdapter
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems,true)
    }


    private inner class QuickSearchItem(val keyWord: KeyWord, val callback: (KeyWord) -> Unit) :
        HiDataItem<KeyWord, HiViewHolder>() {
        override fun onBindData(holder: HiViewHolder, position: Int) {
            holder.item_title.text = keyWord.keyWord
            holder.itemView.setOnClickListener {
                callback(keyWord)
            }
        }

        override fun getItemLayoutRes(): Int {
            return R.layout.layout_quick_search_list_item
        }

    }
}