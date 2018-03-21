package com.xuexiang.devicemonitor.activity;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.xuexiang.devicemonitor.R;
import com.xuexiang.devicemonitor.adapter.AppTrafficAdapter;
import com.xuexiang.devicemonitor.adapter.BaseRecyclerAdapter;
import com.xuexiang.devicemonitor.entity.AppTrafficInfo;
import com.xuexiang.devicemonitor.service.NetWorkMonitorService;
import com.xuexiang.devicemonitor.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xuexiang
 * @date 2018/3/21 下午11:10
 */
public class AppTrafficActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private AppTrafficAdapter mAppTrafficAdapter;

    private List<AppTrafficInfo> mAppInfos = new ArrayList<>();
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_traffic;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAppTrafficAdapter = new AppTrafficAdapter(this, mAppInfos);
        mRecyclerView.setAdapter(mAppTrafficAdapter);


        updateAppTrafficInfo();

    }


    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {
        mAppTrafficAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                ApplicationInfo applicationInfo = mAppTrafficAdapter.getItem(pos).getApplicationInfo();
                NetWorkMonitorService.start(AppTrafficActivity.this, applicationInfo);
                Utils.openApp(AppTrafficActivity.this, applicationInfo.packageName);
            }
        });
    }

    private void updateAppTrafficInfo() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packageInfos) {
            String[] permissions = info.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String permission : permissions) {
                    if (Manifest.permission.INTERNET.equals(permission)) {
                        int uid = info.applicationInfo.uid;
                        long upLoadBytes = TrafficStats.getUidTxBytes(uid);
                        long downLoadBytes = TrafficStats.getUidRxBytes(uid);

                        mAppInfos.add(new AppTrafficInfo(info.applicationInfo, upLoadBytes, downLoadBytes));

//                        /** 获取手机通过 2G/3G 接收的字节流量总数 */
//                        TrafficStats.getMobileRxBytes();
//                        /** 获取手机通过 2G/3G 接收的数据包总数 */
//                        TrafficStats.getMobileRxPackets();
//                        /** 获取手机通过 2G/3G 发出的字节流量总数 */
//                        TrafficStats.getMobileTxBytes();
//                        /** 获取手机通过 2G/3G 发出的数据包总数 */
//                        TrafficStats.getMobileTxPackets();
//                        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
//                        TrafficStats.getTotalRxBytes();
//                        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
//                        TrafficStats.getTotalRxPackets();
//                        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
//                        TrafficStats.getTotalTxBytes();
//                        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
//                        TrafficStats.getTotalTxPackets();
//                        /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
//                        TrafficStats.getUidRxBytes(uid);
//                        /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
//                        TrafficStats.getUidTxBytes(uid);
                    }
                }
            }
        }
        mAppTrafficAdapter.notifyDataSetChanged();
    }
}
