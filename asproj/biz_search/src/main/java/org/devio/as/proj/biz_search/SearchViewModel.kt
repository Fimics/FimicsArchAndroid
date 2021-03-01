package org.devio.`as`.proj.biz_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.`as`.proj.pub_mod.model.model.GoodsModel
import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

class SearchViewModel : ViewModel() {
    var keywords: java.util.ArrayList<KeyWord>? = null
    var pageIndex = PAGE_INIT_INDEX

    companion object {
        const val PAGE_SIZE = 10
        const val PAGE_INIT_INDEX = 1

        const val KEY_SEARCH_HISTORY = "search_history"
        const val MAX_HISTORY_SIZE = 10
    }

    fun quickSearch(key: String): LiveData<List<KeyWord>?> {
        val liveData = MutableLiveData<List<KeyWord>?>()
        ApiFactory.create(SearchApi::class.java).quickSearch(key)
            .enqueue(object : HiCallback<QuickSearchList> {
                override fun onSuccess(response: HiResponse<QuickSearchList>) {
                    liveData.postValue(response.data?.list)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }


    var goodsSearchLiveData = MutableLiveData<List<GoodsModel>?>()
    fun goodsSearch(keyword: String, loadInit: Boolean) {
        if (loadInit) pageIndex = PAGE_INIT_INDEX
        ApiFactory.create(SearchApi::class.java).goodsSearch(keyword, pageIndex, PAGE_SIZE)
            .enqueue(object : HiCallback<GoodsSearchList> {
                override fun onSuccess(response: HiResponse<GoodsSearchList>) {
                    goodsSearchLiveData.value=response.data?.list
                    pageIndex++
                }

                override fun onFailed(throwable: Throwable) {
                    goodsSearchLiveData.postValue(null)
                }
            })
    }

    fun saveHistory(keyWord: KeyWord) {
        if (keywords == null) {
            keywords = ArrayList()
        }
        keywords?.apply {
            if (contains(keyWord)) {
                remove(keyWord)
            }
            add(0, keyWord)
            if (this.size > MAX_HISTORY_SIZE) {
                dropLast(this.size - MAX_HISTORY_SIZE)
            }

            HiExecutor.execute(runnable = Runnable {
                HiStorage.saveCache(KEY_SEARCH_HISTORY, keywords)
            })
        }
    }

    fun queryLocalHistory(): LiveData<ArrayList<KeyWord>> {
        val liveData = MutableLiveData<ArrayList<KeyWord>>()
        HiExecutor.execute(runnable = Runnable {
            keywords = HiStorage.getCache<ArrayList<KeyWord>>(KEY_SEARCH_HISTORY)
            liveData.postValue(keywords)
        })
        return liveData
    }

    fun clearHistory() {
        HiExecutor.execute(runnable = Runnable {
            HiStorage.deleteCache(KEY_SEARCH_HISTORY)
        })
    }

}