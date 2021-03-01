package org.devio.`as`.proj.biz_order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_order.*
import org.devio.`as`.proj.biz_order.address.AddEditingDialogFragment
import org.devio.`as`.proj.biz_order.address.AddEditingDialogFragment.onSavedAddressListener
import org.devio.`as`.proj.biz_order.address.Address
import org.devio.`as`.proj.common.ext.loadUrl
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.common.route.RouteFlag
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.util.HiRes
import org.devio.hi.library.util.HiStatusBar

/**
 * 下单页
 *
 */
@Route(path = "/order/main", extras = RouteFlag.FLAG_LOGIN)
class OrderActivity : HiBaseActivity() {

    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null

    private val REQUEST_CODE_ADDRESS_LIST = 1000
    private val viewModel by viewModels<OrderViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        HiRoute.inject(this)
        setContentView(R.layout.activity_order)

        initView()
        updateTotalPayPrice(amount_view.getAmountValue())

        viewModel.queryMainAddress().observe(this) {
            updateAddress(it)
        }
    }

    private fun updateAddress(address: Address?) {
        val hasMainAddress = address != null && !TextUtils.isEmpty(address.receiver)
        add_address.visibility = if (hasMainAddress) View.GONE else View.VISIBLE
        main_address.visibility = if (hasMainAddress) View.VISIBLE else View.GONE
        if (hasMainAddress) {
            user_name.text = address!!.receiver
            user_phone.text = address.phoneNum
            user_address.text = "${address.province} ${address.city} ${address.area}"
            main_address.setOnClickListener {
                //地址列表页
                HiRoute.startActivity(
                    this,
                    destination = "/address/list",
                    requestCode = REQUEST_CODE_ADDRESS_LIST
                )
            }
        } else {
            add_address.setOnClickListener {
                //弹窗 新增地址
                val addEditDialog = AddEditingDialogFragment.newInstance(null)
                addEditDialog.setSavedAddressListener(object : onSavedAddressListener {
                    override fun onSavedAddress(address: Address) {
                        updateAddress(address)
                    }
                })
                addEditDialog.show(supportFragmentManager, "add_address")
            }
        }
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })

        //店铺logo ,名称，商品主图 单价 和名称
        shopLogo?.apply { shop_logo.loadUrl(this) }
        shop_title.text = shopName
        goodsImage?.apply { goods_image.loadUrl(this) }
        goods_title.text = goodsName
        goods_price.text = goodsPrice

        //计数器
        amount_view.setAmountValueChangedListener {
            updateTotalPayPrice(it)
        }

        //支付渠道
        channel_wx_pay.setOnClickListener(channelPayListener)
        channel_ali_pay.setOnClickListener(channelPayListener)

        //立即购买
        order_now.setOnClickListener {
            showToast("not support for now")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPayPrice(amount: Int) {
        //amount* goodsPrice.contains("¥")
        // goodsPrice =10.08 ，x ￥
        total_pay_price.text = String.format(
            HiRes.getString(
                R.string.free_transport,
                PriceUtil.calculate(goodsPrice, amount)
            )
        )

    }

    private val channelPayListener = View.OnClickListener {
        val aliPayChecked = it.id == channel_ali_pay.id
        channel_ali_pay.isChecked = aliPayChecked
        channel_wx_pay.isChecked = !aliPayChecked
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == REQUEST_CODE_ADDRESS_LIST && resultCode == Activity.RESULT_OK) {
            val address = data.getParcelableExtra<Address>("result")
            if (address != null) {
                updateAddress(address)
            }
        }
    }
}