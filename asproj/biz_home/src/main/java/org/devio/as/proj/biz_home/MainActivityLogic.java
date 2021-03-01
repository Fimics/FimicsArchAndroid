package org.devio.as.proj.biz_home;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import org.devio.as.proj.biz_home.category.CategoryFragment;
import org.devio.as.proj.biz_home.favorite.FavoriteFragment;
import org.devio.as.proj.biz_home.home.HomePageFragment;
import org.devio.as.proj.biz_home.profile.ProfileFragment;
import org.devio.as.proj.biz_home.recommend.RecommendFragment;
import org.devio.as.proj.common.tab.HiFragmentTabView;
import org.devio.as.proj.common.tab.HiTabViewAdapter;
import org.devio.hi.config.HiConfig;
import org.devio.hi.config.core.ConfigListener;
import org.devio.hi.library.aspectj.MethodTrace;
import org.devio.hi.library.util.HiViewUtil;
import org.devio.hi.ui.tab.bottom.HiTabBottomInfo;
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout;
import org.devio.hi.ui.tab.common.IHiTabLayout;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 将MainActivity的一些逻辑内聚在这，让MainActivity更加清爽
 */

public class MainActivityLogic {
    private HiFragmentTabView fragmentTabView;
    private HiTabBottomLayout hiTabBottomLayout;
    private List<HiTabBottomInfo<?>> infoList;
    private ActivityProvider activityProvider;
    private final static String SAVED_CURRENT_ID = "SAVED_CURRENT_ID";
    private int currentItemIndex;

    public MainActivityLogic(ActivityProvider activityProvider, @Nullable Bundle savedInstanceState) {
        this.activityProvider = activityProvider;
        //fix 不保留活动导致的Fragment重叠问题
        if (savedInstanceState != null) {
            currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID);
        }
        initTabBottom();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVED_CURRENT_ID, currentItemIndex);
    }

    public HiFragmentTabView getFragmentTabView() {
        return fragmentTabView;
    }

    public List<HiTabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    public HiTabBottomLayout getHiTabBottomLayout() {
        return hiTabBottomLayout;
    }

    @MethodTrace
    private void initTabBottom() {
        hiTabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);
        hiTabBottomLayout.setTabAlpha(HiViewUtil.lightMode() ? 0.85f : 0f);


        infoList = new ArrayList<>();
        int defaultColor = activityProvider.getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = activityProvider.getResources().getColor(R.color.tabBottomTintColor);
        HiTabBottomInfo homeInfo = new HiTabBottomInfo<Integer>(
                "首页",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null,
                defaultColor,
                tintColor
        );
        homeInfo.fragment = HomePageFragment.class;
        HiTabBottomInfo infoFavorite = new HiTabBottomInfo<Integer>(
                "收藏",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite),
                null,
                defaultColor,
                tintColor
        );
        infoFavorite.fragment = FavoriteFragment.class;
        HiTabBottomInfo infoCategory = new HiTabBottomInfo<Integer>(
                "分类",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null,
                defaultColor,
                tintColor
        );
        infoCategory.fragment = CategoryFragment.class;
        HiTabBottomInfo infoRecommend = new HiTabBottomInfo<Integer>(
                "推荐",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null,
                defaultColor,
                tintColor
        );
        infoRecommend.fragment = RecommendFragment.class;
        HiTabBottomInfo infoProfile = new HiTabBottomInfo<Integer>(
                "我的",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null,
                defaultColor,
                tintColor
        );
        infoProfile.fragment = ProfileFragment.class;
        infoList.add(homeInfo);
        infoList.add(infoFavorite);
        infoList.add(infoCategory);
        infoList.add(infoRecommend);
        infoList.add(infoProfile);
        hiTabBottomLayout.inflateInfo(infoList);
        initFragmentTabView();
        hiTabBottomLayout.addTabSelectedChangeListener(new IHiTabLayout.OnTabSelectedListener<HiTabBottomInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable HiTabBottomInfo<?> prevInfo, @NonNull HiTabBottomInfo<?> nextInfo) {
                fragmentTabView.setCurrentItem(index);
                MainActivityLogic.this.currentItemIndex = index;
                String tips = HiConfig.getInstance().getStringConfig("tips");
                Toast.makeText(hiTabBottomLayout.getContext(), tips, Toast.LENGTH_SHORT).show();
            }
        });
        HiConfig.getInstance().addListener(new ConfigListener() {
            @Override
            public void onConfigUpdate(@NotNull Map<String, ?> configMap) {
                String homeItem = HiConfig.getInstance().getStringConfig("homeItem");
                if (homeItem != null)
                    hiTabBottomLayout.findTab(homeInfo).getTabNameView().setText(homeItem);
            }
        });
        hiTabBottomLayout.defaultSelected(infoList.get(currentItemIndex));
    }

    private void initFragmentTabView() {
        HiTabViewAdapter tabViewAdapter = new HiTabViewAdapter(activityProvider.getSupportFragmentManager(), infoList);
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        fragmentTabView.setAdapter(tabViewAdapter);
    }

    public interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);

        Window getWindow();
    }

}
