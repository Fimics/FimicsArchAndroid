package org.devio.`as`.proj.common.city

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.ui.cityselector.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 获取城市数据,优先本地缓存，否则接口请求再更新本地缓存，同时一刻多次调用只有一次生效
 *
 * 内存常驻，所以可以考虑手动清理LiveData.value
 */
object CityMgr {
    private const val KEY_CITY_DATA_SET = "city_data_set"
    private val liveData = MutableLiveData<List<Province>?>()

    //是否正在加载数据...，同一时刻只有一个线程，或者一处能够发起数据的加载
    private val isFetching = AtomicBoolean(false)
    fun getCityData(): LiveData<List<Province>?> {
        //发送过一次数据之后，这个数据List<Province> 会存储在value
        if (isFetching.compareAndSet(false, true) && liveData.value == null) {
            getCache { cache ->
                if (cache != null) {
                    liveData.postValue(cache)
                    isFetching.set(false)
                } else {
                    fetchRemote { remote ->
                        liveData.postValue(remote)
                        isFetching.set(false)
                    }
                }
            }
        }
        return liveData
    }


    private fun fetchRemote(callback: (List<Province>?) -> Unit) {
        ApiFactory.create(CityApi::class.java).listCities().enqueue(object : HiCallback<CityModel> {
            override fun onSuccess(response: HiResponse<CityModel>) {
                if (response.data?.list?.isNullOrEmpty() == false) {
                    //分组处理之后的数据，三级数据结构
                    groupByProvince(response.data!!.list) { groupList ->
                        saveGroupProvince(groupList)
                        callback(groupList)
                    }
                } else {
                    HiLog.e("response data list is empty or null")
                    callback(null)
                }
            }

            override fun onFailed(throwable: Throwable) {
                if (!TextUtils.isEmpty(throwable.message)) HiLog.e(throwable.message)
                callback(null)
            }
        })
    }

    //持久化到本地
    private fun saveGroupProvince(groupList: List<Province>?) {
        if (groupList.isNullOrEmpty()) return
        HiExecutor.execute(runnable = Runnable {
            HiStorage.saveCache(KEY_CITY_DATA_SET, groupList)
        })
    }

    //对数据进行分组，生成三级数据结构
    private fun groupByProvince(list: List<District>, callback: (List<Province>?) -> Unit) {
        //是为了收集所有的省，同时也是为了TYPE_CITY，快速找到自己所在的province对象
        val provinceMaps = hashMapOf<String, Province>()
        //是为了TYPE_DISTRICT 快速找到自己所在的city对象
        val cityMaps = hashMapOf<String, City>()
        HiExecutor.execute(runnable = Runnable {
            for (element in list) {
                if (TextUtils.isEmpty(element.id)) continue
                when (element.type) {
                    TYPE_COUNTRY -> {
                        //ignore
                    }
                    TYPE_PROVINCE -> {
                        val province = Province()
                        District.copyDistrict(element, province)
                        provinceMaps[element.id!!] = province
                    }
                    TYPE_CITY -> {
                        val city = City()
                        District.copyDistrict(element, city)
                        val province = provinceMaps[element.pid]
                        province?.cities?.add(city)
                        cityMaps[element.id!!] = city
                    }
                    TYPE_DISTRICT -> {
                        val city = cityMaps[element.pid]
                        city?.districts?.add(element)
                    }
                }
                callback(ArrayList(provinceMaps.values))
            }
        })
    }

    private fun getCache(callback: (List<Province>?) -> Unit) {
        HiExecutor.execute(runnable = Runnable {
            val cache = HiStorage.getCache<List<Province>?>(KEY_CITY_DATA_SET)
            callback(cache)
        })
    }
}