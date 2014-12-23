package com.bruce.funny;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bruce.funny.view.PagerSlidingTabStrip;

/**
 * Created by zhouxinzhijian on 2014/12/21.
 */
public class TJPostContainerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TJPostFragment mPostFragment;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TJPostContainerFragment newInstance(int sectionNumber) {
        TJPostContainerFragment fragment = new TJPostContainerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        View rootView = inflater.inflate(R.layout.fragment_post_container, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewPager mViewPager = (ViewPager) getView().findViewById(R.id.pager);
        mViewPager.setAdapter(new PostViewPagerAdapter(getChildFragmentManager()));
        PagerSlidingTabStrip mPagerSlidingTabStrip = (PagerSlidingTabStrip)getActivity().getActionBar().getCustomView();
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        setPagerSlidingTabStripValue(mPagerSlidingTabStrip);
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setPagerSlidingTabStripValue(PagerSlidingTabStrip mPagerSlidingTabStrip) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        // 设置Tab是自动填充满屏幕的
        mPagerSlidingTabStrip.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics));
        // 设置Tab Indicator的高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics));
        // 设置Tab标题文字的大小
        mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics));
        // 设置Tab Indicator的颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor(getString(R.color.action_bar_indicator_color)));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor(getString(R.color.action_bar_indicator_color)));
//        // 取消点击Tab时的背景色
//        mPagerSlidingTabStrip.setTabBackground(android.R.color.transparent);
    }

    public class PostViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{
        public PostViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = { getString(R.string.action_bar_tab_hot), getString(R.string.action_bar_tab_new)};
        private final int[] icons = {R.drawable.selector_ab_ic_hot, R.drawable.selector_ab_ic_new};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getPageIconResId(int position) {
            return icons[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return new TJPostFragment();
        }

    }
}