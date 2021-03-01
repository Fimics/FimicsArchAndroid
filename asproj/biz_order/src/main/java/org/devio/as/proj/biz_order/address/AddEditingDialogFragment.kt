package org.devio.`as`.proj.biz_order.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_add_new_address.*
import org.devio.`as`.proj.biz_order.R
import org.devio.`as`.proj.common.city.CityMgr
import org.devio.hi.library.util.HiRes
import org.devio.`as`.proj.common.ext.showToast
import org.devio.hi.ui.cityselector.CitySelectorDialogFragment
import org.devio.hi.ui.cityselector.CitySelectorDialogFragment.onCitySelectListener
import org.devio.hi.ui.cityselector.Province

class AddEditingDialogFragment : AppCompatDialogFragment() {
    private var savedAddressListener: onSavedAddressListener? = null

    //对现有地址进行编辑
    private var address: Address? = null
    private var selectProvince: Province? = null

    companion object {
        const val KEY_ADDRESS_PARAMS = "key_address"
        fun newInstance(address: Address?): AddEditingDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_ADDRESS_PARAMS, address)
            val fragment = AddEditingDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = arguments?.getParcelable(KEY_ADDRESS_PARAMS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        //使用android.R.id.content 作为 父容器，在inflate 布局的时候，
        // 会使用它的layoutParams 来设置子view的宽高
        val root = window?.findViewById(android.R.id.content) ?: container
        val contentView = inflater.inflate(R.layout.dialog_add_new_address, root, false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //右侧关闭按钮
        val closeBtn = nav_nar.addRightTextButton(R.string.if_close, R.id.nav_id_close)
        closeBtn.textSize = 25f
        closeBtn.setOnClickListener { dismiss() }

        //选择城市区域
        address_pick.getEditText()
            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0)
        //既不会拉起软键盘，还能够响应点击事件，而不是用isenable=false
        address_pick.getEditText().isFocusable = false
        address_pick.getEditText().isFocusableInTouchMode = false
        address_pick.getEditText().setOnClickListener {

            val liveData = CityMgr.getCityData()
            liveData.removeObservers(viewLifecycleOwner)
            liveData.observe(viewLifecycleOwner, Observer {
                //拉起城市选择器
                if (it != null) {
                    val citySelector = CitySelectorDialogFragment.newInstance(selectProvince, it)
                    citySelector.setCitySelectListener(object : onCitySelectListener {
                        override fun onCitySelect(province: Province) {
                            updateAddressPick(province)
                        }
                    })
                    citySelector.show(childFragmentManager, "city_selector")
                } else {
                    showToast(HiRes.getString(R.string.city_data_set_empty))
                }
            })
        }

        //详细地址
        address_detail.getTitleView().gravity = Gravity.TOP
        address_detail.getEditText().gravity = Gravity.TOP
        address_detail.getEditText().isSingleLine = false


        //数据回填
        if (address != null) {
            user_name.getEditText().setText(address!!.receiver)
            user_phone.getEditText().setText(address!!.phoneNum)
            address_pick.getEditText()
                .setText("${address!!.province} ${address!!.city} ${address!!.area}")
            address_detail.getEditText().setText(address!!.detail)
        }

        action_save_address.setOnClickListener {
            //保存地址
            savedAddress()
        }
    }

    private fun updateAddressPick(province: Province) {
        this.selectProvince = province
        address_pick.getEditText()
            .setText("${province.districtName} ${province.selectCity?.districtName} ${province.selectDistrict?.districtName}")
    }

    private fun savedAddress() {
        val phone = user_phone.getEditText().text.toString().trim()
        val receiver = user_name.getEditText().text.toString().trim()
        val detail = address_detail.getEditText().text.toString().trim()
        val cityArea = address_pick.getEditText().text.toString().trim()

        if (TextUtils.isEmpty(phone)
            || TextUtils.isEmpty(receiver)
            || TextUtils.isEmpty(detail)
            || TextUtils.isEmpty(cityArea)
        ) {
            showToast(HiRes.getString(R.string.address_info_too_simple))
            return
        }

        val province = selectProvince?.districtName ?: address?.province
        val city = selectProvince?.selectCity?.districtName ?: address?.city
        val district = selectProvince?.selectDistrict?.districtName ?: address?.area
        if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(district)) {
            showToast(HiRes.getString(R.string.address_info_too_simple))
            return
        }
        if (address == null) {
            //新增地址保存
            viewModel.saveAddress(province!!, city!!, district!!, detail, receiver, phone)
                .observe(viewLifecycleOwner, observer)
        } else {
            //更新地址
            viewModel.updateAddress(
                address!!.id,
                province!!,
                city!!,
                district!!,
                detail,
                receiver,
                phone
            )
                .observe(viewLifecycleOwner, observer)
        }
    }

    private val observer = Observer<Address?> {
        if (it != null) {
            //回传结果
            savedAddressListener?.onSavedAddress(it)
            dismiss()
        }
    }

    fun setSavedAddressListener(listener: onSavedAddressListener) {
        this.savedAddressListener = listener
    }


    interface onSavedAddressListener {
        fun onSavedAddress(address: Address)
    }

}