package org.devio.as.hi.hiitem.hiitem.old_demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.devio.as.hi.hiitem.R;
import org.devio.as.hi.hiitem.hiitem.ItemData;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final List<ItemData> dataList = mockDataSets();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new HomeAdapter(this, dataList));

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return dataList.get(position).itemType < ItemData.TYPE_VIDEO ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(manager);

    }

    private List<ItemData> mockDataSets() {
        List<ItemData> dataList = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            ItemData itemData = new ItemData();
            if (index < 6) {
                itemData.itemType = index;
            } else {
                itemData.itemType = index % 2 == 0 ? ItemData.TYPE_VIDEO : ItemData.TYPE_IMAGE;
            }
            dataList.add(itemData);
        }
        return dataList;
    }
}
