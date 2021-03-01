package org.devio.`as`.proj.biz_home.api

import org.devio.`as`.proj.biz_home.model.CourseNotice
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.POST

interface AccountApi {

    @GET("notice")
    fun notice(): HiCall<CourseNotice>
}