package com.xuexiang.devicemonitor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.devicemonitor.R;
import com.xuexiang.devicemonitor.activity.BaseActivity;
import com.xuexiang.devicemonitor.service.NetWorkMonitorService;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {

    }

    @OnClick({R.id.btn_start_check_network, R.id.btn_stop_check_network, R.id.btn_choose_app})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_check_network:
                NetWorkMonitorService.start(this, null);
                break;

            case R.id.btn_stop_check_network:
                NetWorkMonitorService.stop(this);
                break;
            case R.id.btn_choose_app:
                startActivity(new Intent(this, AppTrafficActivity.class));
                break;

            default:
                break;
        }
    }
}
