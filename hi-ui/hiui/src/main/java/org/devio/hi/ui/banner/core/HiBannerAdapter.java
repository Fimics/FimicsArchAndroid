package org.devio.hi.ui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.devio.hi.ui.banner.HiBanner;

import java.util.List;

/**
 * HiViewPager的适配器，为页面填充数据
 */
public class HiBannerAdapter extends PagerAdapter {
    private Context mContext;
    private SparseArray<HiBannerViewHolder> mCachedViews = new SparseArray<>();
    private HiBanner.OnBannerClickListener mBannerClickListener;
    private IBindAdapter mBindAdapter;
    private List<? extends HiBannerMo> models;
    /**
     * 是否开启自动轮
     */
    private boolean mAutoPlay = true;
    /**
     * 非自动轮播状态下是否可以循环切换
     */
    private boolean mLoop = false;
    private int mLayoutResId = -1;

    public HiBannerAdapter(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        this.models = models;
        initCachedView();
        notifyDataSetChanged();

    }

    private void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < models.size(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCachedViews.put(i, viewHolder);
        }
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.mBindAdapter = bindAdapter;
    }

    public void setOnBannerClickListener(HiBanner.OnBannerClickListener OnBannerClickListener) {
        this.mBannerClickListener = OnBannerClickListener;
    }

    public void setLayoutResId(@LayoutRes int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    /**
     * 获取Banner页面数量
     *
     * @return
     */
    public int getRealCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public int getCount() {
        //无限轮播关键点
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    /**
     * 获取初次展示的item位置
     *
     * @return
     */
    public int getFirstItem() {
        //这里是为了配合instantiateItem方法中realPosition = position % getRealCount();
        // - (Integer.MAX_VALUE / 2) % getRealCount()的主要目的是用于获取realPosition=0的位置
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        HiBannerViewHolder viewHolder = mCachedViews.get(realPosition);
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }

        onBind(viewHolder, models.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }


    private View createView(LayoutInflater layoutInflater, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set setLayoutResId first");
        }

        return layoutInflater.inflate(mLayoutResId, parent, false);
    }

    protected void onBind(@NonNull final HiBannerViewHolder viewHolder, @NonNull final HiBannerMo bannerMo, final int position) {
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerClickListener != null) {
                    mBannerClickListener.onBannerClick(viewHolder, bannerMo, position);
                }
            }
        });
        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, bannerMo, position);
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让item每次都会刷新
        return POSITION_NONE;
    }

    public static class HiBannerViewHolder {
        private SparseArray<View> viewHolderSparseArr;
        View rootView;

        HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.viewHolderSparseArr == null) {
                this.viewHolderSparseArr = new SparseArray<>(1);
            }

            V childView = (V) viewHolderSparseArr.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                this.viewHolderSparseArr.put(id, childView);
            }

            return childView;
        }

    }
}