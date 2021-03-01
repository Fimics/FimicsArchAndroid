package org.devio.`as`.proj.common.city

import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.GET

internal interface CityApi {
    @GET("cities")
    fun listCities(): HiCall<CityModel>
}
