package org.devio.as.proj.common.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.devio.hi.ui.tab.bottom.HiTabBottomInfo;

import java.util.List;

public class HiTabViewAdapter {

    private List<HiTabBottomInfo<?>> mInfoList;
    private Fragment mCurFragment;
    private FragmentManager mFragmentManager;

    public HiTabViewAdapter(FragmentManager fragmentManager, List<HiTabBottomInfo<?>> infoList) {
        this.mFragmentManager = fragmentManager;
        this.mInfoList = infoList;
    }

    public void instantiateItem(View container, int position) {
        FragmentTransaction mCurTransaction = mFragmentManager.beginTransaction();
        if (mCurFragment != null) {
            mCurTransaction.hide(mCurFragment);
        }
        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            mCurTransaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                mCurTransaction.add(container.getId(), fragment, name);
            }
        }
        mCurFragment = fragment;
        mCurTransaction.commitNowAllowingStateLoss();
    }

    public Fragment getCurrentFragment() {
        return mCurFragment;
    }

    public Fragment getItem(int position) {
        try {
            return (Fragment) mInfoList.get(position).fragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

}
