package org.devio.`as`.proj.biz_detail

import android.text.TextUtils
import androidx.lifecycle.*
import com.alibaba.android.arouter.BuildConfig
import org.devio.`as`.proj.biz_detail.api.DetailApi
import org.devio.`as`.proj.biz_detail.model.DetailModel
import org.devio.`as`.proj.biz_detail.model.Favorite
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.`as`.proj.biz_detail.api.FavoriteApi
import java.lang.Exception

class DetailViewModel(val goodsId: String?) : ViewModel() {

    companion object {
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                try {
                    val constructor = modelClass.getConstructor(String::class.java)
                    if (constructor != null) {
                        return constructor.newInstance(goodsId)
                    }
                } catch (exception: Exception) {
                    //ignore
                }
                return super.create(modelClass)
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java
            )
        }
    }


    fun queryDetailData(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!)
                .enqueue(object : HiCallback<DetailModel> {
                    override fun onSuccess(response: HiResponse<DetailModel>) {
                        if (response.successful() && response.data != null) {
                            pageData.postValue(response.data)
                        } else {
                            pageData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.postValue(null)
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                    }

                })
        }
        return pageData
    }


    fun toggleFavorite(): LiveData<Boolean?> {
        val toggleFavoriteData = MutableLiveData<Boolean?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(FavoriteApi::class.java).favorite(goodsId!!)
                .enqueue(object : HiCallback<Favorite> {
                    override fun onSuccess(response: HiResponse<Favorite>) {
                        toggleFavoriteData.postValue(response.data?.isFavorite)
                    }

                    override fun onFailed(throwable: Throwable) {
                        toggleFavoriteData.postValue(null)
                    }
                })
        }
        return toggleFavoriteData
    }
}