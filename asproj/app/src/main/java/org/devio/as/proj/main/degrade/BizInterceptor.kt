package org.devio.`as`.proj.main.degrade

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import org.devio.`as`.proj.common.route.RouteFlag
import org.devio.`as`.proj.service_login.LoginServiceProvider
import org.devio.hi.library.util.MainHandler

/**
 * 业务的拦截器，判断目标页是否具备预先定义好的属性
 * @see RouteFlag
 */
@Interceptor(name = "biz_interceptor", priority = 9)
open class BizInterceptor : IInterceptor {
    private var context: Context? = null

    override fun init(context: Context?) {
        this.context = context;
    }

    /**
     * note:该方法运行在arouter的线程池中
     */
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra

        if ((flag and (RouteFlag.FLAG_LOGIN) != 0)) {
            //login
            loginIfNecessary(postcard, callback)
        } else {
            callback!!.onContinue(postcard)
        }
    }

    private fun loginIfNecessary(postcard: Postcard?, callback: InterceptorCallback?) {
        MainHandler.post(runnable = Runnable {
            if (LoginServiceProvider.isLogin()) {
                callback?.onContinue(postcard)
            } else {
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
                val observer = Observer<Boolean> {
                    callback?.onContinue(postcard)
                }
                LoginServiceProvider.login(context, observer)
            }
        })
    }
}