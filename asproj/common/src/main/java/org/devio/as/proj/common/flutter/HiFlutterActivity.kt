package org.devio.`as`.proj.common.flutter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import org.devio.`as`.proj.common.R
import org.devio.hi.library.util.HiStatusBar

@Route(path = "/flutter/main")
class HiFlutterActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var moduleName: String? = null
    var flutterFragment: MFlutterFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, translucent = true)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_flutter)
        initFragment()
    }

    private fun initFragment() {
        flutterFragment = MFlutterFragment(moduleName!!)
        supportFragmentManager.beginTransaction().add(R.id.root_view, flutterFragment!!)
            .commit()
    }

    class MFlutterFragment(private val moduleName: String) : HiFlutterFragment(moduleName) {
        override fun onDestroy() {
            super.onDestroy()
            //销毁Flutter引擎
            HiFlutterCacheManager.instance?.destroyCached(moduleName)
        }

        override fun getPageName(): String {
            return "MFlutterFragment"
        }
    }
}