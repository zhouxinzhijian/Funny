package com.bruce.funny.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouxinzhijian on 2015/1/6.
 */
public class TJFragment extends Fragment{
    public Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        this.mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName()); //统计页面
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MobclickAgent.onPageEnd(getClass().getName());
    }
}
