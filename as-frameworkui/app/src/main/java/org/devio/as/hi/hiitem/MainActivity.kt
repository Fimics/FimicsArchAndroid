package org.devio.`as`.hi.hiitem

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.arc.demo.utils.ActivityManager
import kotlinx.android.synthetic.main.activity_main.*
import org.devio.`as`.hi.hiitem.hiitem.ActivityDataItem
import org.devio.`as`.hi.hiitem.hiitem.ImageDataItem
import org.devio.`as`.hi.hiitem.hiitem.ItemTabDataItem
import org.devio.`as`.hi.hiitem.hiitem.VideoDataItem
import org.devio.`as`.hi.hiitem.hiitem.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var hiAdapter = HiAdapter(this)

        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = hiAdapter;

        var dataSets: ArrayList<HiDataItem<*, out RecyclerView.ViewHolder>> = ArrayList()
        dataSets.add(TopTabDataItem(ItemData()))
        dataSets.add(TopBanner(ItemData()))
        dataSets.add(GirdDataItem(ItemData()))
        dataSets.add(ActivityDataItem(ItemData()))//活动区域
        dataSets.add(ItemTabDataItem(ItemData()))//商品tab栏

        for (i in 0..9) {
            if (i % 2 == 0) {
                //feeds流的视频类型
                dataSets.add(VideoDataItem(1, ItemData()))
            } else {
                //feeds流的图片类型
                dataSets.add(ImageDataItem(1, ItemData()))
            }
        }

        hiAdapter.addItems(dataSets, false)


        //监听前后台变化
        ActivityManager.instance.addFrontBackCallback(frontBackCallback)
    }

    val frontBackCallback = object : ActivityManager.FrontBackCallback {
        override fun onChanged(front: Boolean) {
            val state: String;
            if (front) {
                state = "前台"
            } else {
                state = "后台"
            }

            Toast.makeText(applicationContext, "当前处于：" + state, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.instance.removeFrontBackCallback(frontBackCallback)
    }
}
