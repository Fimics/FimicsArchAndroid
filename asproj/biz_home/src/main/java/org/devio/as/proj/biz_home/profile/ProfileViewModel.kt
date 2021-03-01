package org.devio.`as`.proj.biz_home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.biz_home.api.AccountApi
import org.devio.`as`.proj.biz_home.model.CourseNotice
import org.devio.`as`.proj.common.BuildConfig
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

class ProfileViewModel : ViewModel() {

    fun queryCourseNotice(): LiveData<CourseNotice> {
        val noticeData = MutableLiveData<CourseNotice>();
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : HiCallback<CourseNotice> {
                override fun onSuccess(response: HiResponse<CourseNotice>) {
                    if (response.data != null && response.data!!.total > 0) {
                        noticeData.postValue(response.data)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    //ignore
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace()
                    }
                }
            })
        return noticeData
    }
}