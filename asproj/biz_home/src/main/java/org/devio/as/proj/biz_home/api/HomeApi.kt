package org.devio.`as`.proj.biz_home.api

import org.devio.`as`.proj.biz_home.model.HomeModel
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.CacheStrategy
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface HomeApi {
    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>


    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<HomeModel>
}