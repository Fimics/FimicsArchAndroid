package org.devio.`as`.proj.biz_home.goodslist

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_goods_list.*
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.util.HiStatusBar

@Route(path = "/goods/list")
class GoodsListActivity : HiBaseActivity() {

    @JvmField
    @Autowired
    var categoryTitle: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    private val FRAGMENT_TAG = "GOODS_LIST_FRAGMENT"
    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
        HiRoute.inject(this)

        nav_bar.setTitle(categoryTitle)
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })

        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            fragment = GoodsListFragment.newInstance(categoryId, subcategoryId)
        }
        val ft = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            ft.add(R.id.container, fragment, FRAGMENT_TAG)
        }
        ft.show(fragment).commitNowAllowingStateLoss()
    }
}