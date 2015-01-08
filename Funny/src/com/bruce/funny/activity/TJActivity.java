package com.bruce.funny.activity;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouxinzhijian on 2015/1/6.
 */
public class TJActivity extends Activity{
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName()); //统计页面
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName()); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
