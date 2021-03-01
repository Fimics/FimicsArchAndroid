package org.devio.`as`.proj.biz_detail

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import org.devio.hi.library.util.ColorUtil
import org.devio.hi.library.util.HiDisplayUtil
import kotlin.math.abs
import kotlin.math.min

class TitleScrollListener(thresholdDp: Float = 100f, val callback: (Int) -> Unit) :
    RecyclerView.OnScrollListener() {
    private val thresholdPx = HiDisplayUtil.dp2px(thresholdDp)
    private var lastFraction = 0f

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        //在这里面 我们需要去判断列表的滑动的距离 然后跟thresholdPx 做运算，计算当前滑动状态
        //计算出一个新的颜色值 transpanrent ---white
        // recyclerView.scrollY
        // dy

        val viewHolder = recyclerView.findViewHolderForAdapterPosition(0) ?: return
        val top = abs(viewHolder.itemView.top).toFloat()

        //计算出当前滑动百分比
        val fraction = top / thresholdPx
        if (lastFraction > 1f) {
            lastFraction = fraction
            return
        }
        val newColor = ColorUtil.getCurrentColor(Color.TRANSPARENT, Color.WHITE, min(fraction, 1f))
        callback(newColor)

        lastFraction = fraction
    }
}