package org.devio.`as`.proj.biz_detail.model

import androidx.annotation.Keep
import org.devio.`as`.proj.pub_mod.model.model.GoodsModel
import org.devio.`as`.proj.pub_mod.model.model.SliderImage

@Keep
data class DetailModel(
    val categoryId: String,
    val commentCountTitle: String,
    val commentModels: List<CommentModel>?,
    val commentTags: String,
    val completedNumText: String,
    val createTime: String,
    val flowGoods: List<GoodsModel>?,
    val gallery: List<SliderImage>?,
    val goodAttr: List<MutableMap<String, String>>?,
    val goodDescription: String,
    val goodsId: String,
    val goodsName: String,
    val isFavorite: Boolean,
    val groupPrice: String,
    val hot: Boolean,
    val marketPrice: String,
    val shop: Shop,
    val similarGoods: List<GoodsModel>?,
    val sliderImage: String,
    val sliderImages: List<SliderImage>?,
    val tags: String
)

@Keep
data class CommentModel(
    val avatar: String,
    val content: String,
    val nickName: String
)

@Keep
data class Shop(
    val completedNum: String,
    val evaluation: String,
    val goodsNum: String,
    val logo: String,
    val name: String
)

@Keep
data class Favorite(val goodsId: String, var isFavorite: Boolean)