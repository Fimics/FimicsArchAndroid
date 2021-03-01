package org.devio.`as`.proj.biz_home.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.devio.`as`.proj.ability.HiAbility
import org.devio.`as`.proj.biz_home.BR
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.biz_home.model.Subcategory
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.library.util.HiRes
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class GridItem(val list: List<Subcategory>) :
    HiDataItem<List<Subcategory>, HiViewHolder>(list) {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter = GridAdapter(context, list)

        HiAbility.traceEvent(
            "home_page_expose",
            mapOf<String, Any>(Pair("index", position), Pair("card_type", "grid_item"))
        )
    }

    override fun getItemView(parent: ViewGroup): View? {
        val gridView = RecyclerView(parent.context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = HiDisplayUtil.dp2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(HiRes.getColor(R.color.color_white))
        return gridView
    }


    inner class GridAdapter(val context: Context, val list: List<Subcategory>) :
        RecyclerView.Adapter<GridAdapter.GirdItemViewHolder>() {
        private var inflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GirdItemViewHolder {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                inflater,
                R.layout.layout_home_op_grid_item, parent, false
            )
            return GirdItemViewHolder(binding.root, binding)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: GirdItemViewHolder, position: Int) {
            val subcategory = list[position]
            holder.binding.setVariable(BR.subCategory, subcategory)

            //holder.item_image.loadUrl(subcategory.subcategoryIcon)
            //holder.item_title.text = subcategory.subcategoryName

            holder.itemView.setOnClickListener {
                //会跳转到子分类列表上面去，是一个单独的页面
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRoute.startActivity(context, bundle, "/goods/list")

                HiAbility.traceEvent(
                    "home_page_click",
                    mapOf<String, Any>(Pair("index", position), Pair("card_type", "grid_item"))
                )
            }
        }

        inner class GirdItemViewHolder(view: View, val binding: ViewDataBinding) :
            HiViewHolder(view) {
        }
    }
}