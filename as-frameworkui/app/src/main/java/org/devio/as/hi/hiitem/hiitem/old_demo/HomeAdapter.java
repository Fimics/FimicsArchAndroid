package org.devio.as.hi.hiitem.hiitem.old_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.devio.as.hi.hiitem.R;
import org.devio.as.hi.hiitem.hiitem.ItemData;

import java.util.ArrayList;
import java.util.List;

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<ItemData> dataSets = new ArrayList<>();

    public HomeAdapter(Context context, List<ItemData> dataSets) {
        this.context = context;
        this.dataSets.addAll(dataSets);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        ItemData itemData = dataSets.get(position);
        return itemData.itemType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = 0;
        switch (viewType) {
            case ItemData.TYPE_TOP_TAB:
                layoutRes = R.layout.layout_list_item_top_tab;
                break;
            case ItemData.TYPE_BANNER:
                layoutRes = R.layout.layout_list_item_banner;
                break;
            case ItemData.TYPE_GRID_ITEM:
                layoutRes = R.layout.layout_list_item_grid;
                break;
            case ItemData.TYPE_ACTIVITY:
                layoutRes = R.layout.layout_list_item_activity;
                break;
            case ItemData.TYPE_ITEM_TAB:
                layoutRes = R.layout.layout_list_item_tab;
                break;
            case ItemData.TYPE_VIDEO:
                layoutRes = R.layout.layout_list_item_video;
                break;
            case ItemData.TYPE_IMAGE:
                layoutRes = R.layout.layout_list_item_image;
                break;

        }
        View view = null;
        if (layoutRes != 0) {
            //根据type 加载对应的item 布局资源
            view = mInflater.inflate(layoutRes, parent, false);
        } else {
            //如果该item的类型不在已知类型内，则创建个兜底View。
            view = new TextView(context);
            view.setVisibility(View.GONE);
        }
        //这里更有甚者会创建多种ViewHolder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //switch-case 分门别类的对每一种类型的item的数据进行绑定，以及其它业务逻辑处理
        ItemData itemData = dataSets.get(position);
        switch (itemData.itemType) {
            case ItemData.TYPE_TOP_TAB:
                holder.imageView.setImageResource(R.drawable.item_top_tab);
                break;
            case ItemData.TYPE_BANNER:
                holder.imageView.setImageResource(R.drawable.item_banner);
                break;
            case ItemData.TYPE_GRID_ITEM:
                holder.imageView.setImageResource(R.drawable.item_grid);
                break;
            case ItemData.TYPE_ACTIVITY:
                holder.imageView.setImageResource(R.drawable.item_activity);
                break;
            case ItemData.TYPE_ITEM_TAB:
                holder.imageView.setImageResource(R.drawable.item_tab);
                break;
            case ItemData.TYPE_VIDEO:
                holder.imageView.setImageResource(R.drawable.item_video);
                break;
            case ItemData.TYPE_IMAGE:
                holder.imageView.setImageResource(R.drawable.item_image);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return dataSets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

    }
}
