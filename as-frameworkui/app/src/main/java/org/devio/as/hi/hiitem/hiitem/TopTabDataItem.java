package org.devio.as.hi.hiitem.hiitem;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.devio.as.hi.hiitem.R;
import org.jetbrains.annotations.NotNull;

public class TopTabDataItem extends HiDataItem<ItemData, RecyclerView.ViewHolder> {
    public TopTabDataItem(ItemData itemData) {
        super(itemData);
    }

    @Override
    public void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.item_image);
        imageView.setImageResource(R.drawable.item_top_tab);
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.layout_list_item_top_tab;
    }
}
