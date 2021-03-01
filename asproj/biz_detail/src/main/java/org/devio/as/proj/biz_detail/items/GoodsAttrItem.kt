package org.devio.`as`.proj.biz_detail.items

import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.layout_detail_item_attr.*
import org.devio.`as`.proj.biz_detail.R
import org.devio.`as`.proj.biz_detail.model.DetailModel
import org.devio.hi.ui.input.InputItemLayout
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

/**
 * 详情页--商品详情描述模块
 */
class GoodsAttrItem(val detailModel: DetailModel) : HiDataItem<DetailModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val goodAttr = detailModel.goodAttr
        goodAttr?.let {
            val iterator = it.iterator()
            var index = 0
            val attrContainer = holder.attr_container
            attrContainer.visibility = View.VISIBLE
            while (iterator.hasNext()) {
                val attr = iterator.next()
                val entries = attr.entries
                val key = entries.first().key
                val value = entries.first().value

                val attrItemView: InputItemLayout = if (index < attrContainer.childCount) {
                    attrContainer.getChildAt(index)
                } else {
                    LayoutInflater.from(context)
                        .inflate(
                            R.layout.layout_detail_item_attr_item,
                            attrContainer,
                            false
                        )
                } as InputItemLayout

                attrItemView.getEditText().hint = value
                attrItemView.getEditText().isEnabled = false
                attrItemView.getTitleView().text = key

                if (attrItemView.parent == null) {
                    attrContainer.addView(attrItemView)
                }
                index++
            }
        }

        detailModel.goodDescription?.let {
            val attrDesc = holder.attr_desc
            attrDesc.visibility = View.VISIBLE
            attrDesc.text = it
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_attr
    }
}