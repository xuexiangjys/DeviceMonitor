package com.xuexiang.devicemonitor.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.xuexiang.devicemonitor.R;

import java.text.DecimalFormat;

/**
 * 监控悬浮控件
 * @author xuexiang
 * @date 2018/3/21 下午2:22
 */
public class MonitorView extends BaseFloatView {

    private TextView mTvAppName;

    private TextView mTvUpLoadInfo;

    private TextView mTvDownLoadInfo;

    private DecimalFormat mSpeedFormat = new DecimalFormat("0.00");

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    /**
     * 构造器
     *
     * @param context
     */
    public MonitorView(Context context) {
        super(context);
    }

    /**
     * 获取根布局的ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.layout_monitor;
    }

    /**
     * 初始化悬浮控件
     */
    @Override
    protected void initFloatView() {
        mTvAppName = findViewById(R.id.tv_app_name);
        mTvUpLoadInfo = findViewById(R.id.tv_upload_speed_info);
        mTvDownLoadInfo = findViewById(R.id.tv_download_speed_info);
        setIsAdsorb(true);
        initFloatViewPosition();
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListener() {
        getFloatRootView().setOnTouchListener(this);

//        getContext().registerReceiver(mNetWorkBroadcastReceiver, new IntentFilter(NetWorkMonitorService.ACTION_NETWORK_SPEED_INFO));
    }

//    private BroadcastReceiver mNetWorkBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (NetWorkMonitorService.ACTION_NETWORK_SPEED_INFO.equals(action)) {
//                double upLoadSpeed = intent.getDoubleExtra(NetWorkMonitorService.KEY_UPLOAD_SPEED, 0);
//                double downLoadSpeed = intent.getDoubleExtra(NetWorkMonitorService.KEY_DOWNLOAD_SPEED, 0);
//                showSpeedInfo(upLoadSpeed, downLoadSpeed);
//            }
//        }
//    };


    /**
     * 更新监测应用名
     * @param appName
     */
    public void updateMonitorAppName(final String appName) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mTvAppName.setText(String.format("应用：%s", appName));
        } else {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTvAppName.setText(String.format("应用：%s", appName));
                }
            });
        }
    }
    /**
     * 更新网络速度
     * @param upLoadSpeed 上行速度
     * @param downLoadSpeed 下行速度
     */
    public void updateNetWorkInfo(final double upLoadSpeed, final double downLoadSpeed) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showSpeedInfo(upLoadSpeed, downLoadSpeed);
        } else {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    showSpeedInfo(upLoadSpeed, downLoadSpeed);
                }
            });
        }
    }

    /**
     * 显示网络速度
     * @param upLoadSpeed
     * @param downLoadSpeed
     */
    private void showSpeedInfo(double upLoadSpeed, double downLoadSpeed) {
        mTvUpLoadInfo.setText("上传：" + mSpeedFormat.format(upLoadSpeed / 1024D) + "kb/s");
        mTvDownLoadInfo.setText("下载：" + mSpeedFormat.format(downLoadSpeed / 1024D) + "kb/s");
    }


    @Override
    public void clear() {
        super.clear();
        mMainHandler.removeCallbacksAndMessages(null);
//        if (mNetWorkBroadcastReceiver != null) {
//            getContext().unregisterReceiver(mNetWorkBroadcastReceiver);
//            mNetWorkBroadcastReceiver = null;
//        }

    }
}
