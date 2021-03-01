package org.devio.`as`.proj.biz_home.api

import org.devio.`as`.proj.biz_home.model.Subcategory
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>


    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}