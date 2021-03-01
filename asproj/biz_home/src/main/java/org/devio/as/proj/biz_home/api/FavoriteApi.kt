package org.devio.`as`.proj.biz_home.api

import org.devio.`as`.proj.biz_home.model.Favorite
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.POST
import org.devio.hi.library.restful.annotation.Path

interface FavoriteApi{

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId")goodsId:String):HiCall<Favorite>
}