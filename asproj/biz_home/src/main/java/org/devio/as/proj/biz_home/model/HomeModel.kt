package org.devio.`as`.proj.biz_home.model

import androidx.annotation.Keep
import org.devio.`as`.proj.pub_mod.model.model.GoodsModel
import org.devio.`as`.proj.service_login.Notice
import java.io.Serializable


@Keep
data class HomeModel(
    val bannerList: List<HomeBanner>?,
    val subcategoryList: List<Subcategory>?,
    val goodsList: List<GoodsModel>?
) : Serializable

/**
 * {
"categoryId": "1",
"categoryName": "热门",
"goodsCount": "1"
}
 */
data class TabCategory(val categoryId: String, val categoryName: String, val goodsCount: String) :
    Serializable


/**
 *  {
"id": "5",
"sticky": 1,
"type": "goods",
"title": "商品推荐",
"subtitle": "2019新款X27水滴屏6.5英寸全网通4G指纹人脸美颜游戏一体智能手机",
"url": "1574920889775",
"cover": "https://o.devio.org/images/as/goods/images/2019-11-14/8ad2ad11-afa3-4ae8-a816-860ec82f7b37.jpeg",
"createTime": "2020-03-23 11:24:57"
}
 */
data class HomeBanner(
    val cover: String,
    val createTime: String,
    val id: String,
    val sticky: Int,
    val subtitle: String,
    val title: String,
    val type: String,
    val url: String
) : Serializable {
    companion object {
        const val TYPE_GOODS = "goods"
        const val TYPE_RECOMMEND = "recommend"
    }
}


/**
 * {
"subcategoryId": "1",
"groupName": null,
"categoryId": "1",
"subcategoryName": "限时秒杀",
"subcategoryIcon": "https://o.devio.org/images/as/images/2018-05-16/26c916947489c6b2ddd188ecdb54fd8d.png",
"showType": "1"
}
 */
data class Subcategory(
    val categoryId: String,
    val groupName: String,
    val showType: String,
    val subcategoryIcon: String,
    val subcategoryId: String,
    val subcategoryName: String
) : Serializable

data class GoodsList(val total: Int, val list: List<GoodsModel>) {

}

data class Favorite(val goodsId: String, var isFavorite: Boolean)

data class CourseNotice(val total: Int, val list: List<Notice>?)