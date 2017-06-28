package com.example.administrator.newclient.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/6/26.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        initView();
        initListener();
        initData();

    }

    /**
     * 设置子控件
     */
    public abstract void initView();


    /**
     * 设置监听器
     */
    public abstract void initListener();

    /**
     * 返回Activity界面的布局文件
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化数据
     */
    public abstract void initData();


    private Toast mToast;

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
