package org.devio.`as`.proj.biz_home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.biz_home.api.HomeApi
import org.devio.`as`.proj.biz_home.model.HomeModel
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.restful.annotation.CacheStrategy

class HomeViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    fun queryCategoryTabs(): LiveData<List<TabCategory>?> {
        val liveData = MutableLiveData<List<TabCategory>?>()
        val memCache = savedState.get<List<TabCategory>?>("categoryTabs")
        if (memCache != null) {
            liveData.postValue(memCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java)
            .queryTabList().enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        liveData.value = data
                        savedState.set("categoryTabs", data)
                    }
                }
            })
        return liveData
    }


    fun queryTabCategoryList(
        categoryId: String?,
        pageIndex: Int,
        cacheStrategy: Int
    ): LiveData<HomeModel?> {
        val liveData = MutableLiveData<HomeModel?>()

        val memCache = savedState.get<HomeModel>("categoryList")
        //只有是第一次加载时  才需要从内存中取
        if (memCache != null && pageIndex == 1 && cacheStrategy == CacheStrategy.CACHE_FIRST) {
            liveData.postValue(memCache)
            return liveData
        }
        ApiFactory.create(HomeApi::class.java)
            .queryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
            .enqueue(object : HiCallback<HomeModel> {
                override fun onSuccess(response: HiResponse<HomeModel>) {
                    val data = response.data;
                    if (response.successful() && data != null) {
                        //一次缓存数据，一次接口数据
                        liveData.value = data
                        //只有在刷新的时候，且不是本地缓存的数据 才存储到内容中
                        if (cacheStrategy != CacheStrategy.NET_ONLY && response.code == HiResponse.SUCCESS) {
                            savedState.set("categoryList", data)
                        }
                    } else {
                        liveData.postValue(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })

        return liveData
    }
}