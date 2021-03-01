package org.devio.`as`.proj.biz_order.address

import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.*

interface AddressApi {
    @GET("address")
    fun queryAddress(
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<AddressModel>


    /**
     * "area": "西湖区",
    "city": "杭州",
    "detail": "小西湖123号",
    "phoneNum": 15888888888,
    "province": "浙江",
    "receiver": "张三"
     */
    @POST("address", formPost = false)
    fun addAddress(
        @Filed("province") province: String,
        @Filed("city") city: String,
        @Filed("area") area: String,
        @Filed("detail") detail: String,
        @Filed("receiver") receiver: String,
        @Filed("phoneNum") phoneNum: String
    ): HiCall<String>

    @PUT("address/{id}", formPost = false)
    fun updateAddress(
        @Path("id") id: String,
        @Filed("province") province: String,
        @Filed("city") city: String,
        @Filed("area") area: String,
        @Filed("detail") detail: String,
        @Filed("receiver") receiver: String,
        @Filed("phoneNum") phoneNum: String
    ): HiCall<String>

    @DELETE("address/{id}")
    fun deleteAddress(@Path("id") id: String): HiCall<String>

}