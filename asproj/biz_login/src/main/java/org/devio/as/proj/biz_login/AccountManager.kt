package org.devio.`as`.proj.biz_login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.devio.`as`.proj.biz_login.api.AccountApi
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.`as`.proj.service_login.UserProfile
import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.AppGlobals
import org.devio.hi.library.util.MainHandler

object AccountManager {
    private val lock = Any()
    private var userProfile: UserProfile? = null
    private var boardingPass: String? = null
    private const val KEY_USER_PROFILE = "user_profile"
    private const val KEY_BOARDING_PASS = "boarding_pass"
    private val loginLiveData = ObserverLiveData<Boolean>()
    private val profileLiveData = ObserverLiveData<UserProfile>()

    @Volatile
    private var isFetching = false

    init {
        HiExecutor.execute(runnable = Runnable {
            val local = HiStorage.getCache<UserProfile?>(KEY_USER_PROFILE)
            synchronized(lock) {
                if (userProfile == null && local == null) {
                    userProfile = local
                }
            }
        })
    }

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(observer)
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            throw IllegalStateException("context must not be null.")
        }

        context.startActivity(intent)
    }

    internal fun loginSuccess(boardingPass: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boardingPass)
        this.boardingPass = boardingPass
        loginLiveData.value = true
    }

    fun getBoardingPass(): String? {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }


    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = false
    ) {
        if (lifecycleOwner == null) {
            profileLiveData.observeForever(observer)
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }

        if (isFetching) return
        isFetching = true

        if (onlyCache) {
            synchronized(lock) {
                if (userProfile != null) {
                    MainHandler.post(Runnable {
                        this.profileLiveData.value = userProfile
                    })
                    return
                }
            }
        }

        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    if (response.successful() && response.data != null) {
                        userProfile = response.data
                        HiExecutor.execute(runnable = Runnable {
                            HiStorage.saveCache(KEY_USER_PROFILE, userProfile)
                            isFetching = false
                        })
                        profileLiveData.value = userProfile
                    } else {
                        profileLiveData.value = null
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    isFetching = false
                    profileLiveData.value = null
                }
            })
    }


    private class ObserverLiveData<T> : MutableLiveData<T>() {
        private val observers = arrayListOf<Observer<in T>>()
        override fun observeForever(observer: Observer<in T>) {
            super.observeForever(observer)
            if (!observers.contains(observer)) {
                observers.add(observer)
            }
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            if (!observers.contains(observer)) {
                observers.add(observer)
            }
        }

        fun removeAllObservers() {
            for (observer in observers) {
                removeObserver(observer)
            }
            observers.clear()
        }
    }

    //主动清除observers防止 foreverObserver 释放不掉,
    //同时也能规避 多次打开登录页，多次注册observer的问题
    internal fun clearObservers() {
        loginLiveData.removeAllObservers()
        profileLiveData.removeAllObservers()
    }
}