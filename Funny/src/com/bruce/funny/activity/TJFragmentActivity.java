package com.bruce.funny.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouxinzhijian on 2015/1/6.
 */
public class TJFragmentActivity extends FragmentActivity{
    public FragmentActivity mActivity;
    private boolean mIncludeFragment;
    public void setIncludeFragment(boolean includeFragment){
        mIncludeFragment = includeFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mIncludeFragment){
            MobclickAgent.onPageStart(mActivity.getClass().getName()); //统计页面
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!mIncludeFragment){
            MobclickAgent.onPageEnd(mActivity.getClass().getName());
        }
        MobclickAgent.onPause(this);
    }
}
