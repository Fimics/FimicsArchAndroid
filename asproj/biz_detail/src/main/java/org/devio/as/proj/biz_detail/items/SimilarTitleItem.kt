package org.devio.`as`.proj.biz_detail.items

import org.devio.`as`.proj.biz_detail.R
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class SimilarTitleItem : HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}