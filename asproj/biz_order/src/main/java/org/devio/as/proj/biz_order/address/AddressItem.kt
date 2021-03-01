package org.devio.`as`.proj.biz_order.address

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_address_list_item.*
import org.devio.`as`.proj.biz_order.R
import org.devio.`as`.proj.biz_order.address.AddEditingDialogFragment.*
import org.devio.hi.library.util.HiRes
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class AddressItem(
    var address: Address,
    val fm: FragmentManager,
    val removeItemCallback: (Address, AddressItem) -> Unit,
    val itemClickCallback: (Address) -> Unit,
    val viewModel: AddressViewModel
) : HiDataItem<Address, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.user_name.text = address.receiver
        holder.user_phone.text = address.phoneNum
        holder.user_address.text = "${address.province} ${address.city} ${address.area}"
        holder.edit_address.setOnClickListener {
            val dialog = AddEditingDialogFragment.newInstance(address)
            dialog.setSavedAddressListener(object : onSavedAddressListener {
                override fun onSavedAddress(newAddress: Address) {
                    address = newAddress
                    refreshItem()
                }
            })
            dialog.show(fm, "edit_address")
        }

        holder.delete.setOnClickListener {
            AlertDialog.Builder(context).setMessage(HiRes.getString(R.string.address_delete_title))
                .setNegativeButton(R.string.address_delete_cancel, null)
                .setPositiveButton(R.string.address_delete_ensure) { dialog, which ->
                    dialog.dismiss()
                    removeItemCallback(address, this)
                }.show()
        }
        holder.itemView.setOnClickListener {
            itemClickCallback(address)
        }

        holder.default_address.setOnClickListener {
            viewModel.checkedPosition = position
            viewModel.checkedAddressItem?.refreshItem()
            viewModel.checkedAddressItem = this
            refreshItem()
        }


        val select = viewModel.checkedAddressItem == this && viewModel.checkedPosition == position
        holder.default_address.setTextColor(HiRes.getColor(if (select) R.color.color_dd2 else R.color.color_999))
        holder.default_address.text =
            HiRes.getString(if (select) R.string.address_default else R.string.set_default_address)
        holder.default_address.setCompoundDrawablesWithIntrinsicBounds(
            if (select) R.drawable.ic_checked_red else 0,
            0,
            0
            , 0
        )
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.activity_address_list_item
    }
}