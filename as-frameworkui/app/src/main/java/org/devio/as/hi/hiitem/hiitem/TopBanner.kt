package org.devio.`as`.hi.hiitem.hiitem

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.devio.`as`.hi.hiitem.R

class TopBanner(data: ItemData) : HiDataItem<ItemData, RecyclerView.ViewHolder>(data) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val imageview: ImageView = holder.itemView.findViewById<ImageView>(R.id.item_image);
        imageview.setImageResource(R.drawable.item_banner)
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_list_item_banner
    }

}