package org.devio.`as`.proj.biz_home.category

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import kotlinx.android.synthetic.main.fragment_category.*
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.biz_home.model.Subcategory
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.`as`.proj.common.ext.loadUrl
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.hi.ui.empty.EmptyView
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout

class CategoryFragment : HiBaseFragment() {
    private var viewModel: CategoryViewModel? = null
    private var emptyView: EmptyView? = null
    private val SPAN_COUNT = 3
    private val subcategoryListCache = mutableMapOf<String, List<Subcategory>>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_category
    }

    override fun getPageName(): String {
        return "category_fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(root_container)
        content_loading.visibility = View.VISIBLE
        search_container.setOnClickListener {
            HiRoute.startActivity(
                context,
                destination = "/search/main"
            )
        }

        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        queryCategoryList()
    }

    private fun onQueryCategoryListSuccess(data: List<TabCategory>) {
        if (!isAlive) return
        emptyView?.visibility = View.GONE
        content_loading.visibility = View.GONE
        slider_view.visibility = View.VISIBLE

        slider_view.bindMenuView(itemCount = data.size,
            onBindView = { holder, position ->
                val category = data[position]
                // holder.menu_item_tilte    无法直接访问
                //  holder.itemView.menu_item_title.  findviewbyid
                holder.findViewById<TextView>(R.id.menu_item_title)?.text = category.categoryName
            }, onItemClick = { holder, position ->
                val category = data[position]
                val categoryId = category.categoryId
                if (subcategoryListCache.containsKey(categoryId)) {
                    onQuerySubcategoryListSuccess(subcategoryListCache[categoryId]!!)
                } else {
                    querySubcategoryList(categoryId)
                }
            })
    }

    private val subcategoryList = mutableListOf<Subcategory>()
    private val decoration = CategoryItemDecoration({ position ->
        subcategoryList[position].groupName
    }, SPAN_COUNT)
    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)
    private val groupSpanSizeOffset = SparseIntArray()
    private val spanSizeLookUp = object : SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName: String = subcategoryList[position].groupName
            val nextGroupName: String? =
                if (position + 1 < subcategoryList.size) subcategoryList[position + 1].groupName else null

            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                //当前位置和 下一个位置 不再同一个分组
                //1 .要拿到当前组 position （所在组）在 groupSpanSizeOffset 的索引下标
                //2 .拿到 当前组前面一组 存储的 spansizeoffset 偏移量
                //3 .给当前组最后一个item 分配 spansize count

                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                val lastGroupOffset = if (size <= 0) 0
                else if (indexOfKey >= 0) {
                    //说明当前组的偏移量记录，已经存在了 groupSpanSizeOffset ，这个情况发生在上下滑动，
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
                } else {
                    //说明当前组的偏移量记录，还没有存在于 groupSpanSizeOffset ，这个情况发生在 第一次布局的时候
                    //得到前面所有组的偏移量之和
                    groupSpanSizeOffset.valueAt(size - 1)
                }
                //          3       -     (6     +    5               % 3  )第几列=0  ，1 ，2
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT
                if (indexOfKey < 0) {
                    //得到当前组 和前面所有组的spansize 偏移量之和
                    val groupOffset = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }
            return spanSize
        }
    }

    private fun querySubcategoryList(categoryId: String) {
        viewModel?.querySubcategoryList(categoryId)?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                onQuerySubcategoryListSuccess(it)
                if (!subcategoryListCache.containsKey(categoryId)) {
                    subcategoryListCache.put(categoryId, it)
                }
            }
        })
    }


    private fun onQuerySubcategoryListSuccess(data: List<Subcategory>) {
        decoration.clear()
        groupSpanSizeOffset.clear()
        subcategoryList.clear()
        subcategoryList.addAll(data)

        if (layoutManager.spanSizeLookup != spanSizeLookUp) {
            layoutManager.spanSizeLookup = spanSizeLookUp
        }
        slider_view.bindContentView(
            itemCount = data.size,
            itemDecoration = decoration,
            layoutManager = layoutManager,
            onBindView = { holder, position ->
                val subcategory = data[position]
                holder.findViewById<ImageView>(R.id.content_item_image)
                    ?.loadUrl(subcategory.subcategoryIcon)
                holder.findViewById<TextView>(R.id.content_item_title)?.text =
                    subcategory.subcategoryName
            },
            onItemClick = { holder, position ->
                //是应该跳转到类目的商品列表页的
                val subcategory = data[position]
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRoute.startActivity(context!!, bundle, "/goods/list")
            }
        )
    }

    private fun queryCategoryList() {
        viewModel?.queryCategoryList()?.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                onQueryCategoryListSuccess(it)
            }
        })
    }


    private fun showEmptyView() {

        if (emptyView == null) {
            emptyView = EmptyView(context!!)
            emptyView?.setIcon(R.string.if_empty3)
            emptyView?.setDesc(getString(R.string.list_empty_desc))
            emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                queryCategoryList()
            })
            root_container.addView(emptyView)
        }

        content_loading.visibility = View.GONE
        slider_view.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        slider_view.contentView.adapter?.notifyDataSetChanged()
    }
}