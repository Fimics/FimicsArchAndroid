package org.devio.`as`.proj.service_login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter

object LoginServiceProvider {
    private val iLoginService =
        ARouter.getInstance().build("/login/service").navigation() as? ILoginService

    fun login(context: Context?, observer: Observer<Boolean>) {
        iLoginService?.login(context, observer)
    }

    fun isLogin(): Boolean {
        return iLoginService?.isLogin() == true
    }

    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = false
    ) {
        iLoginService?.getUserProfile(lifecycleOwner, observer, onlyCache)
    }

    fun getBoardingPass(): String? {
        return iLoginService?.getBoardingPass()
    }
}