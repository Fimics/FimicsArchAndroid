package org.devio.`as`.hi.hiitem.hiitem

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.devio.`as`.hi.hiitem.R

class GirdDataItem(data: ItemData) : HiDataItem<ItemData, GirdDataItem.MyHolder>(data) {


    override fun onBindData(holder: MyHolder, position: Int) {
        holder.imageView!!.setImageResource(R.drawable.item_grid)
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_list_item_grid;
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null

        init {
            imageView = itemView.findViewById<ImageView>(R.id.item_image)
        }
    }
}
