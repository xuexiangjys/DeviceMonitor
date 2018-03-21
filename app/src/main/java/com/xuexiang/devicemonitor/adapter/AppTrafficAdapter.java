package com.xuexiang.devicemonitor.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.format.Formatter;

import com.xuexiang.devicemonitor.R;
import com.xuexiang.devicemonitor.entity.AppTrafficInfo;

import java.util.List;

/**
 * 流量信息适配器
 * @author xuexiang
 * @date 2018/3/21 下午11:51
 */
public class AppTrafficAdapter extends BaseRecyclerAdapter<AppTrafficInfo> {
    private PackageManager mPackageManager;

    public AppTrafficAdapter(Context context, List<AppTrafficInfo> list) {
        super(context, list);
        mPackageManager = context.getPackageManager();
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_recycler_app_traffic_item;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, AppTrafficInfo item) {
        holder.getImageView(R.id.iv_app_launcher).setImageDrawable(item.getApplicationInfo().loadIcon(mPackageManager));
        holder.getTextView(R.id.tv_app_name).setText(mPackageManager.getApplicationLabel(item.getApplicationInfo()));
        holder.getTextView(R.id.tv_app_download).setText(String.format("下载：%s", Formatter.formatFileSize(getContext(), item.getDownLoadBytes())));
        holder.getTextView(R.id.tv_app_upload).setText(String.format("上传：%s", Formatter.formatFileSize(getContext(), item.getUpLoadBytes())));
    }
}
