package com.bruce.funny.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bruce.funny.Config;
import com.bruce.funny.R;
import com.umeng.analytics.MobclickAgent;

@Deprecated
public class TJWelcomeActivity extends TJFragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(mActivity);
        MobclickAgent.setDebugMode(Config.debug);
        MobclickAgent.openActivityDurationTrack(false);
        setContentView(R.layout.activity_welcome);
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                startActivity(new Intent(mActivity, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }
}