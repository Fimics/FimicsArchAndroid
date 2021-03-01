package org.devio.`as`.proj.pub_mod.model.model

import android.os.Parcelable
import android.text.TextUtils
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * "goodsId": "1580374361011",
"categoryId": "16",
"hot": true,
"sliderImages": [
{
"url": "https://o.devio.org/images/as/goods/images/2018-12-21/5c3672e33377b65d5f1bef488686462b.jpeg",
"type": 1
},
{
"url": "https://o.devio.org/images/as/goods/images/2018-12-21/117a40a6d63c5bac590080733512b89d.jpeg",
"type": 1
},
{
"url": "https://o.devio.org/images/as/goods/images/2018-12-21/7d4449179b509531414365460d80a87d.jpeg",
"type": 1
}
],
"marketPrice": "¥100",
"groupPrice": "14",
"completedNumText": "已拼1348件",
"goodsName": "吉祥鱼房间新年装饰客厅餐厅卧室玄背景墙亚克力3d立体自粘墙贴画",
"tags": "极速退款 全场包邮 7天无理由退货",
"joinedAvatars": null,
"createTime": "2020-01-30 16:52:41",
"sliderImage": "https://o.devio.org/images/as/goods/images/2018-12-21/5c3672e33377b65d5f1bef488686462b.jpeg"
 */
@Parcelize
@Keep
data class GoodsModel(
    val categoryId: String?,
    val completedNumText: String?,
    val createTime: String?,
    val goodsId: String?,
    val goodsName: String?,
    val groupPrice: String?,
    val hot: Boolean?,
    val joinedAvatars: List<SliderImage>?,
    val marketPrice: String?,
    val sliderImage: String?,
    val sliderImages: List<SliderImage>?,
    val tags: String?
) : java.io.Serializable, Parcelable

@Keep
@Parcelize
data class SliderImage(
    val type: Int,
    val url: String
) : java.io.Serializable, Parcelable

fun selectPrice(groupPrice: String?, marketPrice: String?): String? {
    var price: String? = if (TextUtils.isEmpty(marketPrice)) groupPrice else marketPrice
    if (price?.startsWith("¥") != true) {
        price = "¥".plus(price)
    }
    return price
}