package org.devio.`as`.proj.main.degrade
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.layout_global_degrade.*
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.main.R

/**
 * 全局统一错误页
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var degrade_title: String? = null
    @JvmField
    @Autowired
    var degrade_desc: String? = null
    @JvmField
    @Autowired
    var degrade_action: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ARouter.getInstance().inject(this)
        HiRoute.inject(this)

        setContentView(R.layout.layout_global_degrade)
        empty_view.setIcon(R.string.if_empty)

        if (degrade_title != null) {
            empty_view.setTitle(degrade_title!!)
        }

        if (degrade_desc != null) {
            empty_view.setDesc(degrade_desc!!)
        }

        if (degrade_action != null) {
            empty_view.setHelpAction(listener = View.OnClickListener {
                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(degrade_action))
                startActivity(intent)
            })
        }

        action_back.setOnClickListener { onBackPressed() }
    }
}