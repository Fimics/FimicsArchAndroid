package org.devio.`as`.proj.biz_order.address

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.`as`.proj.common.ext.showToast

class AddressViewModel : ViewModel() {
    //列表上需要共享的数据
    var checkedPosition: Int = -1
    var checkedAddressItem: AddressItem? = null

    fun queryAddressList(): LiveData<List<Address>?> {
        val liveData = MutableLiveData<List<Address>?>()
        ApiFactory.create(AddressApi::class.java)
            .queryAddress(1, 100)
            .enqueue(object : HiCallback<AddressModel> {
                override fun onSuccess(response: HiResponse<AddressModel>) {
                    liveData.postValue(response.data?.list)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }


    fun saveAddress(
        province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java)
            .addAddress(province, city, area, detail, receiver, phone)
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.successful()) {
                        val address = Address(province, city, area, detail, receiver, phone, "", "")
                        liveData.postValue(address)
                    } else {
                        if (!TextUtils.isEmpty(response.msg)) showToast(response.msg!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    if (!TextUtils.isEmpty(throwable.message)) showToast(throwable.message!!)
                }
            })
        return liveData
    }

    fun updateAddress(
        id: String, province: String,
        city: String,
        area: String,
        detail: String,
        receiver: String,
        phone: String
    ): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java)
            .updateAddress(id, province, city, area, detail, receiver, phone)
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.successful()) {
                        val address = Address(province, city, area, detail, receiver, phone, "", "")
                        liveData.postValue(address)
                    } else {
                        if (!TextUtils.isEmpty(response.msg)) showToast(response.msg!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    if (!TextUtils.isEmpty(throwable.message)) showToast(throwable.message!!)
                }
            })
        return liveData
    }

    fun deleteAddress(addressId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        ApiFactory.create(AddressApi::class.java).deleteAddress(addressId)
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.successful()) {
                        liveData.postValue(response.successful())
                    } else {
                        showToast("地址删除失败")
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast("地址删除失败:" + throwable.message)
                }
            })
        return liveData
    }

    override fun onCleared() {
        checkedAddressItem = null
        checkedPosition = -1
        super.onCleared()
    }
}