package org.devio.`as`.proj.biz_detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_detail.*
import org.devio.`as`.proj.biz_detail.items.*
import org.devio.`as`.proj.biz_detail.model.DetailModel
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.ui.empty.EmptyView
import org.devio.`as`.proj.pub_mod.model.items.GoodsItem
import org.devio.`as`.proj.pub_mod.model.model.GoodsModel
import org.devio.`as`.proj.pub_mod.model.model.selectPrice
import org.devio.`as`.proj.service_login.LoginServiceProvider
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.library.util.HiRes
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.library.util.MainHandler
import org.devio.hi.ui.item.HiAdapter
import org.devio.hi.ui.item.HiDataItem

/**
 * 商品详情页
 */
@Route(path = "/detail/main")
class DetailActivity : HiBaseActivity() {

    private lateinit var viewModel: DetailViewModel
    private var emptyView: EmptyView? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, translucent = true)
        HiRoute.inject(this)
        assert(!TextUtils.isEmpty(goodsId)) { " goodsId must bot be null" }

        setContentView(R.layout.activity_detail)
        initView()
        preBindData()
        queryDetailData()

        //message--messeagequqe--mainThread--detailActivity---Message--Runnable(持有detailActivity对象)
        //Handler().postDelayed(Runnable { showToast("1111") }, 1000 * 10)

        //MainHandler.postDelay(1000*10, Runnable { showToast("1111") })
    }

    private fun queryDetailData() {
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })
    }

    private fun preBindData() {
        if (goodsModel == null) return
        val hiAdapter = recycler_view.adapter as HiAdapter
        hiAdapter.addItemAt(
            0, HeaderItem(
                goodsModel!!.sliderImages,
                selectPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice),
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )
    }

    private fun bindData(detailModel: DetailModel) {
        recycler_view.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE

        val hiAdapter = recycler_view.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        //头部模块
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        //评论item
        dataItems.add(CommentItem(detailModel))
        //店铺模块
        dataItems.add(ShopItem(detailModel))
        //商品描述lauyou
        dataItems.add(GoodsAttrItem(detailModel))
        //图库
        detailModel.gallery?.forEach {
            dataItems.add(GalleryItem(it))
        }
        //相似商品
        detailModel.similarGoods?.let {
            dataItems.add(SimilarTitleItem())
            it.forEach {
                dataItems.add(GoodsItem(it, false))
            }
        }

        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)

        updateFavoriteActionFace(detailModel.isFavorite)
        updateOrderActionFace(detailModel)
    }

    @SuppressLint("SetTextI18n")
    private fun updateOrderActionFace(detailModel: DetailModel) {
        action_order.text = "${selectPrice(
            detailModel.groupPrice,
            detailModel.marketPrice
        )}" + getString(R.string.detail_order_action)
        action_order.setOnClickListener {
            //点击立即购买跳转 下单页
            val bundle = Bundle()
            bundle.putString("shopName", detailModel.shop.name)
            bundle.putString("shopLogo", detailModel.shop.logo)
            bundle.putString("goodsId", detailModel.goodsId)
            bundle.putString("goodsImage", detailModel.sliderImage)
            bundle.putString("goodsName", detailModel.goodsName)
            bundle.putString(
                "goodsPrice",
                selectPrice(detailModel.groupPrice, detailModel.marketPrice)
            )

            HiRoute.startActivity(this@DetailActivity, bundle = bundle, destination = "/order/main")
        }
    }

    private fun updateFavoriteActionFace(favorite: Boolean) {
        action_favorite.setOnClickListener {
            toggleFavorite()
        }
        action_favorite.setTextColor(
            ContextCompat.getColor(
                this,
                if (favorite) R.color.color_dd2 else R.color.color_999
            )
        )
    }

    private fun toggleFavorite() {
        if (!LoginServiceProvider.isLogin()) {
            LoginServiceProvider.login(this, Observer { loginSuccess ->
                if (loginSuccess) {
                    toggleFavorite()
                }
            })
        } else {
            action_favorite.isClickable = false
            viewModel.toggleFavorite().observe(this, Observer { success ->
                if (success != null) {
                    //网络成功
                    updateFavoriteActionFace(success)
                    val message =
                        if (success) getString(R.string.detail_favorite_success) else getString(R.string.detail_cancel_favorite)
                    showToast(message)
                } else {
                    //网络失败
                }
                action_favorite.isClickable = true
            })
        }

    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams = ConstraintLayout.LayoutParams(-1, -1)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryDetailData()
            })

            root_container.addView(emptyView)
        }

        recycler_view.visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

    private fun initView() {
        nav_bar.setPadding(0, HiDisplayUtil.getStatusBarDimensionPx(), 0, 0)
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        nav_bar.addRightTextButton(HiRes.getString(R.string.if_share), R.integer.id_if_share)
            .setOnClickListener {
                showToast("share,not support for now.")
            }

        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = HiAdapter(this)
        recycler_view.addOnScrollListener(TitleScrollListener(callback = {
            nav_bar.setBackgroundColor(it)
        }))
    }
}