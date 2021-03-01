package org.devio.`as`.proj.common.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.devio.hi.library.util.HiViewUtil

fun ImageView.loadUrl(url: String) {
    if (HiViewUtil.isActivityDestroyed(context) || TextUtils.isEmpty(url)) return
    Glide.with(context).load(url).into(this)
}

fun ImageView.loadUrl(url: String, callback: (Drawable) -> Unit) {
    //you cannot load url from destory activity
    if (HiViewUtil.isActivityDestroyed(context) || TextUtils.isEmpty(url)) return
    Glide.with(context).load(url).into(object : SimpleTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            callback(resource)
        }
    })
}

@BindingAdapter(value = ["circleUrl"])
fun ImageView.loadCircle(circleUrl: String?) {
    if (HiViewUtil.isActivityDestroyed(context) || TextUtils.isEmpty(circleUrl)) return
    Glide.with(this).load(circleUrl)
        .transform(CircleCrop()).into(this)
}

//巨坑，glide 的 图片裁剪 和 imageview scaleType 有冲突。 centerCrop .
@BindingAdapter(value = ["url", "corner"], requireAll = false)
fun ImageView.loadCorner(url: String, corner: Int) {
    if (HiViewUtil.isActivityDestroyed(context) || TextUtils.isEmpty(url)) return
    val transform = Glide.with(this).load(url).transform(CenterCrop())
    if (corner > 0) {
        RoundedCorners(corner)
    }
    transform.into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {
    if (HiViewUtil.isActivityDestroyed(context) || TextUtils.isEmpty(url))
        Glide.with(this).load(url).transform(CircleBorderTransform(borderWidth, borderColor))
            .into(this)

}

class CircleBorderTransform(private val borderWidth: Float, borderColor: Int) : CircleCrop() {
    private var borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)

        val halfWidth = outWidth / 2.toFloat()
        val halfHeight = outHeight / 2.toFloat()


        canvas.drawCircle(
            halfWidth,
            halfHeight,
            halfWidth.coerceAtMost(halfHeight) - borderWidth / 2,
            borderPaint
        )

        canvas.setBitmap(null)

        return transform
    }
}
