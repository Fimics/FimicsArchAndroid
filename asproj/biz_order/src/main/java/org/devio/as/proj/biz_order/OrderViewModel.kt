package org.devio.`as`.proj.biz_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.biz_order.address.Address
import org.devio.`as`.proj.biz_order.address.AddressApi
import org.devio.`as`.proj.biz_order.address.AddressModel
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

class OrderViewModel : ViewModel() {
    fun queryMainAddress(): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).queryAddress(1, 1)
            .enqueue(object : HiCallback<AddressModel> {
                override fun onSuccess(response: HiResponse<AddressModel>) {
                    val list = response.data?.list
                    val firstElement = if (list?.isNotEmpty() == true) list[0] else null
                    liveData.postValue(firstElement)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }
}