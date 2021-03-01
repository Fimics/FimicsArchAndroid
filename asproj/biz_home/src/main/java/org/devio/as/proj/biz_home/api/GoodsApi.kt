package org.devio.`as`.proj.biz_home.api

import org.devio.`as`.proj.biz_home.model.GoodsList
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface GoodsApi {
    @GET("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Filed("subcategoryId") subcategoryId: String,
        @Filed("pageSize") pageSize: Int,
        @Filed("pageIndex") pageIndex: Int
    ): HiCall<GoodsList>
}