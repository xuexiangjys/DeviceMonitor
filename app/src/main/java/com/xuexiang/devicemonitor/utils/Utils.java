package com.xuexiang.devicemonitor.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author xuexiang
 * @date 2018/3/21 下午1:58
 */
public final class Utils {

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    /**
     * 计算状态栏高度高度 getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(),
                STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 旋转图片
     *
     * @param angle
     *            旋转角度
     * @param bitmap
     *            要旋转的图片
     * @return 旋转后的图片
     */
    public static Bitmap rotate(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     *            Drawable
     * @return Bitmap
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 页面跳转
     *
     * @param intent
     */
    public static void startActivity(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("Utils", Log.getStackTraceString(e));
            }
        } else {
            Log.e("Utils", "[resolveActivity failed]: " + intent.getComponent().getClassName() + " do not register in manifest");
        }
    }

    /**
     * 打开指定的应用
     *
     * @param context
     */
    public static void openApp(Context context, final String packageName) {
        startActivity(context, getLaunchAppIntent(context, packageName));
    }

    /**
     * 获取打开 App 的意图
     *
     * @param packageName 包名
     * @return 打开 App 的意图
     */
    public static Intent getLaunchAppIntent(final Context context, final String packageName) {
        return getLaunchAppIntent(context, packageName, false);
    }


    /**
     * 获取打开 App 的意图
     *
     * @param packageName 包名
     * @param isNewTask   是否开启新的任务栈
     * @return 打开 App 的意图
     */
    public static Intent getLaunchAppIntent(final Context context, final String packageName, final boolean isNewTask) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) return null;
        return getIntent(intent, isNewTask);
    }

    private static Intent getIntent(final Intent intent, final boolean isNewTask) {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }
}
