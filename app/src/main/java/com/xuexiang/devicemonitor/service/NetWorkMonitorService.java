package com.xuexiang.devicemonitor.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xuexiang.devicemonitor.view.MonitorView;

/**
 * 网络状态监听服务
 * @author xuexiang
 * @date 2018/3/21 下午4:29
 */
public class NetWorkMonitorService extends Service {

    public final static String ACTION_NETWORK_SPEED_INFO = "com.xuexiang.devicemonitor.ACTION_NETWORK_SPEED_INFO";

    public final static String KEY_UPLOAD_SPEED = "com.xuexiang.devicemonitor.key_upload_speed";

    public final static String KEY_DOWNLOAD_SPEED = "com.xuexiang.devicemonitor.key_download_speed";

    public final static String KEY_MONITOR_TYPE = "com.xuexiang.devicemonitor.key_monitor_type";
    public final static String KEY_APP_UID = "com.xuexiang.devicemonitor.key_app_uid";
    public final static String KEY_APP_NAME = "com.xuexiang.devicemonitor.key_app_name";

    public final static int MONITOR_TYPE_ALL_APP = 0;
    public final static int MONITOR_TYPE_SINGLE_APP = 1;

    private MonitorView mMonitorView;

    private NetWorkMonitorThread mMonitorThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int monitorType = intent.getIntExtra(KEY_MONITOR_TYPE, MONITOR_TYPE_ALL_APP);
            int uid = intent.getIntExtra(KEY_APP_UID, 0);
            String appName = intent.getStringExtra(KEY_APP_NAME);
            init(monitorType, uid, appName);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启监测
     * @param info
     */
    public static void start(Context context, ApplicationInfo info) {
        Intent intent = new Intent(context, NetWorkMonitorService.class);
        if (info == null) {
            intent.putExtra(KEY_MONITOR_TYPE, MONITOR_TYPE_ALL_APP);
        } else {
            intent.putExtra(KEY_MONITOR_TYPE, MONITOR_TYPE_SINGLE_APP);
            intent.putExtra(KEY_APP_UID, info.uid);
            intent.putExtra(KEY_APP_NAME, context.getPackageManager().getApplicationLabel(info));
        }
        context.startService(intent);
    }

    /**
     * 停止监测
     * @param context
     */
    public static void stop(Context context) {
        context.stopService(new Intent(context, NetWorkMonitorService.class));
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void init(int monitorType, int uid, String appName) {
        if (mMonitorView == null) {
            mMonitorView = new MonitorView(this);
            mMonitorView.showFloatView();
        }
        mMonitorView.updateMonitorAppName(appName);

        if (mMonitorThread == null) {
            mMonitorThread = new NetWorkMonitorThread(this, 1, new NetWorkMonitorThread.OnNetWorkListener() {
                /**
                 * 更新速度
                 *
                 * @param upLoadSpeed   上行速度
                 * @param downLoadSpeed 下行速度
                 */
                @Override
                public void onUpdateSpeed(double upLoadSpeed, double downLoadSpeed) {
                    if (mMonitorView != null) {
                        mMonitorView.updateNetWorkInfo(upLoadSpeed, downLoadSpeed);
                    }
                }
            });
            mMonitorThread.updateMonitorType(monitorType, uid).start();
        } else {
            mMonitorThread.updateMonitorType(monitorType, uid);
        }

    }

    @Override
    public void onDestroy() {
        if (mMonitorThread != null) {
            mMonitorThread.closeThread();
            mMonitorThread = null;
        }
        if (mMonitorView != null) {
            mMonitorView.clear();
            mMonitorView = null;
        }
        super.onDestroy();
    }

}
