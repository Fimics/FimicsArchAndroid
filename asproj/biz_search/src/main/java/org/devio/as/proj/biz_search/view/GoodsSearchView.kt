package org.devio.`as`.proj.biz_search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import org.devio.hi.ui.recyclerview.HiRecyclerView
import org.devio.`as`.proj.pub_mod.model.items.GoodsItem
import org.devio.`as`.proj.pub_mod.model.model.GoodsModel
import org.devio.hi.ui.item.HiAdapter

class GoodsSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HiRecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HiAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(
        list: List<GoodsModel>,
        loadInit: Boolean
    ) {
        val dataItems = mutableListOf<GoodsItem>()
        for (goodsModel in list) {
            dataItems.add(GoodsItem(goodsModel, true))
        }
        val hiAdapter = adapter as HiAdapter
        if (loadInit) hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
    }
}