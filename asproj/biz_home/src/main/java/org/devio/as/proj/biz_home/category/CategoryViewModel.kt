package org.devio.`as`.proj.biz_home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.devio.`as`.proj.biz_home.api.CategoryApi
import org.devio.`as`.proj.biz_home.model.Subcategory
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse

class CategoryViewModel : ViewModel() {
    fun querySubcategoryList(categoryId: String): LiveData<List<Subcategory>?> {
        val subcategoryListData = MutableLiveData<List<Subcategory>?>()
        ApiFactory.create(CategoryApi::class.java).querySubcategoryList(categoryId)
            .enqueue(simpleCallback(subcategoryListData))

        return subcategoryListData
    }


    fun queryCategoryList(): LiveData<List<TabCategory>?> {
        val tabCategoryData = MutableLiveData<List<TabCategory>?>()
        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(simpleCallback<List<TabCategory>>(tabCategoryData))
        return tabCategoryData
    }

    private fun <T> simpleCallback(liveData: MutableLiveData<T?>): HiCallback<T> {
        return object : HiCallback<T> {
            override fun onSuccess(response: HiResponse<T>) {
                if (response.successful() && response.data != null) {
                    liveData.postValue(response.data)
                } else {
                    liveData.postValue(null)
                }
            }

            override fun onFailed(throwable: Throwable) {
                liveData.postValue(null)
            }
        }
    }

}