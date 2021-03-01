package org.devio.as.proj.common.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

/**
 * 1.将Fragment的操作内聚
 * 2.提供通用的一些API
 */
public class HiFragmentTabView extends FrameLayout {

    private HiTabViewAdapter mAdapter;
    private int currentPosition;

    public HiFragmentTabView(Context context) {
        super(context);
    }

    public HiFragmentTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HiFragmentTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HiTabViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(HiTabViewAdapter adapter) {
        if (this.mAdapter != null || adapter == null) {
            return;
        }
        this.mAdapter = adapter;
        currentPosition = -1;
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position >= mAdapter.getCount()) {
            return;
        }
        if (currentPosition != position) {
            currentPosition = position;
            mAdapter.instantiateItem(this, position);
        }
    }


    public int getCurrentItem() {
        return currentPosition;
    }

    public Fragment getCurrentFragment() {
        if (this.mAdapter == null) {
            throw new IllegalArgumentException("please call setAdapter first.");
        }
        return mAdapter.getCurrentFragment();
    }
}
