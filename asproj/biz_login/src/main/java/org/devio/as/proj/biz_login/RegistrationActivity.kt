package org.devio.`as`.proj.biz_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.action_back
import kotlinx.android.synthetic.main.activity_register.input_item_username
import org.devio.`as`.proj.biz_login.api.AccountApi
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.HiStatusBar

@Route(path = "/account/registration")
class RegistrationActivity : HiBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)

        setContentView(R.layout.activity_register)

        action_back.setOnClickListener { onBackPressed() }

        action_submit.setOnClickListener { submit() }
    }

    private fun submit() {
        val orderId = input_item_orderId.getEditText().text.toString()
        val moocId = input_item_moocId.getEditText().text.toString()

        val username = input_item_username.getEditText().text.toString()
        val pwd = input_item_pwd.getEditText().text.toString()
        val pwdSec = input_item_pwd_check.getEditText().text.toString()

        if (TextUtils.isEmpty(orderId)
            || (TextUtils.isEmpty(moocId))
            || (TextUtils.isEmpty(username))
            || TextUtils.isEmpty(pwd)
            || (!TextUtils.equals(pwd, pwdSec))
        ) {
            return
        }


        ApiFactory.create(AccountApi::class.java).register(username, pwd, moocId, orderId)
            .enqueue(object : HiCallback<String> {
                override fun onSuccess(response: HiResponse<String>) {
                    if (response.code == HiResponse.SUCCESS) {
                        //注册成功
                        var intent = Intent()
                        intent.putExtra("username", username)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        showToast(response.msg)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showToast(throwable.message)
                }
            })
    }
}