package org.devio.`as`.proj.biz_detail.api

import org.devio.`as`.proj.biz_detail.model.DetailModel
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface DetailApi {

    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId:String):HiCall<DetailModel>
}