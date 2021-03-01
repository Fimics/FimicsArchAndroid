package org.devio.`as`.proj.biz_home.notice

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.biz_home.api.AccountApi
import org.devio.`as`.proj.biz_home.model.CourseNotice
import org.devio.`as`.proj.common.http.ApiFactory
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.ui.item.HiAdapter


@Route(path = "/notice/list")
class NoticeListActivity : HiBaseActivity() {
    private lateinit var adapter: HiAdapter
    private lateinit var courseNotice: CourseNotice
    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)

        initUI()
        queryCourseNotice()
    }

    private fun initUI() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })

        val llm = LinearLayoutManager(this)
        adapter = HiAdapter(this)
        list.layoutManager = llm
        list.adapter = adapter
    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : HiCallback<CourseNotice> {
                override fun onSuccess(response: HiResponse<CourseNotice>) {
                    response.data?.let { bindData(it) }
                }

                override fun onFailed(throwable: Throwable) {

                }
            })
    }

    private fun bindData(data: CourseNotice) {
        courseNotice = data
        data.list?.map {
            adapter.addItemAt(
                0,
                NoticeItem(it), true
            )
        }

    }
}
