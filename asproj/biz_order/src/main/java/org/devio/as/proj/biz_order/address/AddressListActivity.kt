package org.devio.`as`.proj.biz_order.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_address_list.*
import org.devio.`as`.proj.biz_order.R
import org.devio.`as`.proj.biz_order.address.AddEditingDialogFragment.*
import org.devio.`as`.proj.common.route.RouteFlag
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.ui.empty.EmptyView
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.ui.item.HiAdapter
import javax.inject.Inject


@AndroidEntryPoint
@Route(path = "/address/list", extras = RouteFlag.FLAG_LOGIN)
class AddressListActivity : HiBaseActivity() {
    @Inject
    lateinit var emptyView: EmptyView
    private val viewModel: AddressViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        setContentView(R.layout.activity_address_list)

        initView()
        viewModel.queryAddressList().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                bindData(it)
            } else {
                showHideEmptyView(true)
            }
        })
    }

    private fun bindData(list: List<Address>) {
        val items = arrayListOf<AddressItem>()
        for (address in list) {
            items.add(newAddressItem(address))
        }
        val hiAdapter = recycler_view.adapter as HiAdapter
        hiAdapter.clearItems()
        hiAdapter.addItems(items, true)
    }

    private fun newAddressItem(address: Address): AddressItem {
        return AddressItem(address, supportFragmentManager, itemClickCallback = { address ->
            val intent = Intent()
            intent.putExtra("result", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, removeItemCallback = { address, addressItem ->
            viewModel.deleteAddress(addressId = address.id).observe(this@AddressListActivity,
                Observer { success ->
                    if (success) {
                        addressItem.removeItem()
                    }
                })
        }, viewModel = viewModel)
    }

    private fun initView() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        nav_bar.addRightTextButton(R.string.nav_add_address, R.id.nav_id_add_address)
            .setOnClickListener {
                showAddEditingDialog()
            }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = HiAdapter(this)
        recycler_view.adapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun showAddEditingDialog() {
        val addEditingDialog = AddEditingDialogFragment.newInstance(null)
        addEditingDialog.setSavedAddressListener(object : onSavedAddressListener {
            override fun onSavedAddress(address: Address) {
                val hiAdapter: HiAdapter? = recycler_view.adapter as HiAdapter?
                hiAdapter?.addItemAt(0, newAddressItem(address), true)
            }
        })
        addEditingDialog.show(supportFragmentManager, "add_address")
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showHideEmptyView(false)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            recycler_view.post {
                if (recycler_view.adapter!!.itemCount <= 0) {
                    showHideEmptyView(true)
                }
            }
        }
    }

    private fun showHideEmptyView(showEmptyView: Boolean) {
        recycler_view.isVisible = !showEmptyView
        emptyView.isVisible = showEmptyView
        if (emptyView.parent == null && showEmptyView) {
            root_layout.addView(emptyView)
        }
    }

    override fun onDestroy() {
        recycler_view?.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        super.onDestroy()
    }
}