package org.devio.`as`.proj.biz_search

import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.POST

interface SearchApi {
    @GET("goods/search/quick")
    fun quickSearch(@Filed("keyWord") keyword: String): HiCall<QuickSearchList>

    @POST("goods/search", formPost = false)
    fun goodsSearch(
        @Filed("keyWord") keyword: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<GoodsSearchList>
}