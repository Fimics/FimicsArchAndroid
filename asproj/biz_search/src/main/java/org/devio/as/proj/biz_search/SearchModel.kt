package org.devio.`as`.proj.biz_search

import org.devio.`as`.proj.pub_mod.model.model.GoodsModel
import java.io.Serializable

data class QuickSearchList(
    val list: List<KeyWord>,
    val total: Int
)

data class KeyWord(
    val id: String?,
    val keyWord: String
) : Serializable

data class GoodsSearchList(val total: Int, val list: List<GoodsModel>)