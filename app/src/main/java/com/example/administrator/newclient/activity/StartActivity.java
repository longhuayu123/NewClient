package com.example.administrator.newclient.activity;

import android.content.Intent;
import android.os.SystemClock;

import com.example.administrator.newclient.R;
import com.example.administrator.newclient.base.BaseActivity;
import com.example.administrator.newclient.utils.SharedPrefUtil;

/**
 * Created by Administrator on 2017/6/26.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_start;
    }

    @Override
    public void initData() {
        new Thread() {
            public void run() {
                SystemClock.sleep(1500);

                // 读取不到key为firstRun的值，则默认返回true，表示第一次启动应用
                boolean firstRun = SharedPrefUtil.getBoolean(
                        getApplicationContext(), "firstRun", true);
                if (firstRun) {
                    SharedPrefUtil.saveBoolean(StartActivity.this,
                            "firstRun", false);
                    enterGuideActivity();
                } else {
                    enterMainActivity();
                }
            }
        }.start();
    }

    private void enterMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initView() {
        new Thread() {
            public void run() {
                SystemClock.sleep(1500);

                //进入引导页面
                enterGuideActivity();
            }
        }.start();
    }

    /**
     * 进入引导页面
     */
    private void enterGuideActivity() {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        finish();
    }
}
