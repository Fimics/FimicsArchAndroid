package org.devio.`as`.proj.biz_home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.fragment_profile_page.*
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.biz_home.databinding.FragmentProfilePageBinding
import org.devio.`as`.proj.biz_home.databinding.FragmentProfilePageBinding.inflate
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.common.ext.loadCorner
import org.devio.`as`.proj.common.rn.HiRNActivity
import org.devio.`as`.proj.common.rn.HiRNCacheManager
import org.devio.`as`.proj.service_login.LoginServiceProvider
import org.devio.`as`.proj.service_login.Notice
import org.devio.`as`.proj.service_login.UserProfile
import org.devio.hi.config.HiConfig
import org.devio.hi.config.core.ConfigListener
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.banner.core.HiBannerAdapter
import org.devio.hi.ui.banner.core.HiBannerMo

class ProfileFragment : HiBaseFragment() {
    private lateinit var binding: FragmentProfilePageBinding
    private lateinit var viewModel: ProfileViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_page
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun getPageName(): String {
        return "profile_page"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        queryLoginUserData()
        queryCourseNotice()
    }

    private fun queryCourseNotice() {
        viewModel.queryCourseNotice().observe(viewLifecycleOwner, Observer {
            binding.courseNotice = it
        })
    }

    private fun queryLoginUserData() {
        LoginServiceProvider.getUserProfile(this, Observer { profile ->
            if (profile != null) {
                binding.userProfile = profile
                updateUI(profile)
            } else {
                showToast(getString(R.string.fetch_user_profile_failed))
            }
        })
    }

    private fun updateUI(userProfile: UserProfile) {
        if (!userProfile.isLogin) {
            user_avatar.setImageResource(R.drawable.ic_avatar_default)
            user_name.setOnClickListener {
                LoginServiceProvider.login(context, Observer { success ->
                    queryLoginUserData()
                })
            }
        }
        updateBanner(userProfile.bannerNoticeList)

        binding.llNotice.setOnClickListener {
            HiRoute.startActivity(context, destination = "/notice/list")
        }
        binding.itemPlayground.setOnClickListener {
            HiRoute.startActivity(context, destination = "/playground/main")
        }
        binding.tabItemHistory.setOnClickListener {
            goToBrowsing()
        }
        binding.itemHistory.setOnClickListener {
            goToBrowsing()
        }

    }

    private fun goToBrowsing() {
        if (!LoginServiceProvider.isLogin()) {
            LoginServiceProvider.login(context, Observer { loginSuccess ->
                if (loginSuccess) {
                    ARouter.getInstance().build("/rn/main")
                        .withString(
                            HiRNActivity.HI_RN_BUNDLE,
                            HiRNCacheManager.MODULE_NAME_BROWSING
                        ).navigation()
                }
            })
        } else {
            ARouter.getInstance().build("/rn/main")
                .withString(HiRNActivity.HI_RN_BUNDLE, HiRNCacheManager.MODULE_NAME_BROWSING)
                .navigation()
        }
    }

    private fun updateBanner(bannerList: List<Notice>?) {
        if (bannerList == null || bannerList.isEmpty()) return
        var models = mutableListOf<HiBannerMo>()
        bannerList.forEach {
            var hiBannerMo = object : HiBannerMo() {}
            hiBannerMo.url = it.cover
            models.add(hiBannerMo)
        }
        hi_banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            HiRoute.startActivity4Browser(bannerList[position].url)
        }
        hi_banner.setBannerData(R.layout.layout_profile_banner_item, models)
        hi_banner.setBindAdapter { viewHolder: HiBannerAdapter.HiBannerViewHolder?, mo: HiBannerMo?, position: Int ->
            if (viewHolder == null || mo == null) return@setBindAdapter
            val imageView = viewHolder.findViewById<ImageView>(R.id.banner_item_imageview)
            imageView.loadCorner(mo.url, HiDisplayUtil.dp2px(10f, resources))
        }
        val showProfileBanner = HiConfig.instance.getStringConfig("showProfileBanner")
        if ("true" == showProfileBanner) {
            hi_banner.visibility = View.VISIBLE
        } else {
            hi_banner.visibility = View.GONE
        }
        HiConfig.instance.addListener(object : ConfigListener {
            override fun onConfigUpdate(configMap: Map<String, Any>) {
                if ("true" == configMap["showProfileBanner"]) {
                    hi_banner.visibility = View.VISIBLE
                } else {
                    hi_banner.visibility = View.GONE
                }
            }
        })

    }
}