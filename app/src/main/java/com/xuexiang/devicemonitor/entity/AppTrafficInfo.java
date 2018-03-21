package com.xuexiang.devicemonitor.entity;

import android.content.pm.ApplicationInfo;

/**
 * 应用流量信息
 *
 * @author xuexiang
 * @date 2018/3/21 下午11:44
 */
public class AppTrafficInfo {

    /**
     * 应用信息
     */
    private ApplicationInfo mApplicationInfo;

    /**
     * 上传流量
     */
    private long mUpLoadBytes;
    /**
     * 下载流量
     */
    private long mDownLoadBytes;

    public AppTrafficInfo(ApplicationInfo applicationInfo, long upLoadBytes, long downLoadBytes) {
        mApplicationInfo = applicationInfo;
        mUpLoadBytes = upLoadBytes;
        mDownLoadBytes = downLoadBytes;
    }

    public ApplicationInfo getApplicationInfo() {
        return mApplicationInfo;
    }

    public AppTrafficInfo setApplicationInfo(ApplicationInfo applicationInfo) {
        mApplicationInfo = applicationInfo;
        return this;
    }


    public long getUpLoadBytes() {
        return mUpLoadBytes;
    }

    public AppTrafficInfo setUpLoadBytes(long upLoadBytes) {
        mUpLoadBytes = upLoadBytes;
        return this;
    }

    public long getDownLoadBytes() {
        return mDownLoadBytes;
    }

    public AppTrafficInfo setDownLoadBytes(long downLoadBytes) {
        mDownLoadBytes = downLoadBytes;
        return this;
    }
}
