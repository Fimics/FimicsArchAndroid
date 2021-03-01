package org.devio.`as`.proj.biz_home.home

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.devio.`as`.proj.biz_home.model.HomeModel
import org.devio.`as`.proj.common.ui.component.HiAbsListFragment
import org.devio.`as`.proj.pub_mod.model.items.GoodsItem
import org.devio.hi.library.restful.annotation.CacheStrategy
import org.devio.hi.library.util.FoldableDeviceUtil
import org.devio.hi.ui.item.HiDataItem

class HomeTabFragment : HiAbsListFragment() {
    private lateinit var viewModel: HomeViewModel
    private var categoryId: String? = null
    val DEFAULT_HOT_TAB_CATEGORY_ID = "1"

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment =
                HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_HOT_TAB_CATEGORY_ID)

        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        enableLoadMore { queryTabCategoryList(CacheStrategy.NET_ONLY) }

        queryTabCategoryList(CacheStrategy.CACHE_FIRST)
    }

    private fun queryTabCategoryList(cacheStrategy: Int) {
        viewModel.queryTabCategoryList(categoryId, pageIndex, cacheStrategy)
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    updateUI(it)
                } else {
                    finishRefresh(null)
                }
            })
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList(CacheStrategy.NET_CACHE)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(context, 2)
    }


    private fun updateUI(data: HomeModel) {

        val dataItems = mutableListOf<HiDataItem<*, *>>()
        data.bannerList?.let {
            dataItems.add(BannerItem(it))
        }

        data.subcategoryList?.let {
            dataItems.add(GridItem(it))
        }

        data.goodsList?.forEachIndexed { index, goodsModel ->
            dataItems.add(
                GoodsItem(
                    goodsModel,
                    TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
                )
            )
        }
        finishRefresh(dataItems)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (FoldableDeviceUtil.isFold()) {
            recyclerView?.layoutManager = LinearLayoutManager(context)
        } else {
            val manager = GridLayoutManager(context, 2)
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position <= 1) 2 else 1
                }
            }
            recyclerView?.layoutManager = manager
        }
    }

    override fun getPageName(): String {
        return "home_tab_page_$categoryId"
    }
}