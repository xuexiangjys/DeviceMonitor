package com.xuexiang.devicemonitor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import com.xuexiang.devicemonitor.utils.Utils;

/**
 * 悬浮窗基类
 * @author xuexiang
 * @date 2018/3/21 下午2:05
 */
public abstract class BaseFloatView implements OnTouchListener {
    private Context mContext;
    private LayoutParams wmParams;
    /**
     * 创建浮动窗口设置布局参数的对象
     */
    private WindowManager mWindowManager;
    /**
     * 浮动窗口的父布局
     */
    private View mFloatRootView;
    /**
     * 系统状态栏的高度
     */
    private int mStatusBarHeight;

    private Location mLocation;
    private OnClickListener mOnClickListener;
    private OnFloatViewMoveListener mOnFloatViewMoveListener;
    /**
     * 悬浮窗口是否吸附
     */
    private boolean mIsAdsorb = false;
    /**
     * 吸附旋转的控件
     */
    private ImageView mRotateView;
    private Bitmap mBitmap;
    /**
     * 悬浮窗口是否显示
     */
    private boolean mIsShow = false;

    /**
     * 构造器
     */
    public BaseFloatView(Context context) {
        init(context);

        initFloatRootView(getLayoutId());

        initFloatView();

        initListener();
    }

    /**
     * 获取根布局的ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化悬浮控件
     */
    protected abstract void initFloatView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化悬浮窗Window
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        wmParams = new LayoutParams();
        // 获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =
                // LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE
        // LayoutParams.FLAG_NOT_TOUCHABLE
        ;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        mStatusBarHeight = Utils.getStatusBarHeight();
        setWindowManagerParams(0, 0, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLocation = new Location();
    }

    /**
     * 设置悬浮框的初始位置、尺寸参数
     *
     * @param Posx
     * @param Posy
     * @param width
     * @param height
     */
    public void setWindowManagerParams(int Posx, int Posy, int width, int height) {
        wmParams.x = Posx;
        wmParams.y = Posy;
        // 设置悬浮窗口长宽数据
        wmParams.width = width;
        wmParams.height = height;
    }

    /**
     * 设置悬浮框的初始位置
     */
    public void initFloatViewPosition() {
        wmParams.x = Utils.getScreenWidth(getContext());
        wmParams.y = (Utils.getScreenHeight(getContext()) - mFloatRootView.getMeasuredHeight()) / 2 - mStatusBarHeight;
    }

    /**
     * 设置悬浮框的初始位置
     */
    public void initFloatViewPosition(int Posx, int Posy) {
        wmParams.x = Posx;
        wmParams.y = Posy;
    }

    /**
     * 初始化父布局
     *
     * @param layoutId 布局的资源ID（最好是LinearLayout)
     * @return
     */
    public View initFloatRootView(int layoutId) {
        //获取浮动窗口视图所在布局
        mFloatRootView = LayoutInflater.from(mContext).inflate(layoutId, null);
        mFloatRootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return mFloatRootView;
    }

    public void setRotateView(ImageView rotateView, int resId) {
        mRotateView = rotateView;
        mBitmap = Utils.getBitmapFromDrawable(mContext.getResources().getDrawable(resId));
        mRotateView.setImageBitmap(mBitmap);
    }

    /**
     * 更新悬浮框的位置参数
     *
     * @param Posx
     * @param Posy
     */
    public void updateViewPosition(int Posx, int Posy) {
        wmParams.x = Posx;
        wmParams.y = Posy;
        mWindowManager.updateViewLayout(mFloatRootView, wmParams);
    }

    /**
     * 显示悬浮框
     */
    public void showFloatView() {
        if (mFloatRootView != null && wmParams != null && !mIsShow) {
            mWindowManager.addView(mFloatRootView, wmParams);
            mIsShow = true;
        }
    }

    /**
     * 隐藏悬浮框
     */
    public void dismissFloatView() {
        if (mFloatRootView != null && mIsShow) {
            mWindowManager.removeView(mFloatRootView);
            mIsShow = false;
        }
    }

    /**
     * 销毁悬浮框
     */
    public void clear() {
        dismissFloatView();
        if (mRotateView != null) {
            mRotateView = null;
            if (mBitmap != null) {
                mBitmap.recycle();
                mBitmap = null;
            }
        }
        if (mFloatRootView != null) {
            mFloatRootView = null;
            wmParams = null;
            mWindowManager = null;
            mLocation = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // 手指按下时记录必要的数据,纵坐标的值都减去状态栏的高度
            case MotionEvent.ACTION_DOWN:
                // 获取相对与小悬浮窗的坐标
                mLocation.mXInView = event.getX();
                mLocation.mYInView = event.getY();
                // 按下时的坐标位置，只记录一次
                mLocation.mXDownInScreen = event.getRawX();
                mLocation.mYDownInScreen = event.getRawY() - mStatusBarHeight;
                break;
            case MotionEvent.ACTION_MOVE:
                // 时时的更新当前手指在屏幕上的位置
                mLocation.mXInScreen = event.getRawX();
                mLocation.mYInScreen = event.getRawY() - mStatusBarHeight;
                // 手指移动的时候更新小悬浮窗的位置
                if (mOnFloatViewMoveListener != null) {
                    mOnFloatViewMoveListener.onMove(mLocation);
                } else {
                    updateViewPosition((int) (mLocation.mXInScreen - mLocation.mXInView), (int) (mLocation.mYInScreen - mLocation.mYInView));
                }
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，按下坐标与当前坐标相等，则视为触发了单击事件
                if (Math.abs(mLocation.getXDownInScreen() - event.getRawX()) < 10 && Math.abs(mLocation.getYDownInScreen() - (event.getRawY() - mStatusBarHeight)) < 10) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(v);
                    }
                } else {
                    if (mIsAdsorb) {
                        updateGravity(event);
                    }
                }
                break;
        }
        return true;
    }

    public void setOnFloatViewClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnFloatViewMoveListener(OnFloatViewMoveListener onFloatViewMoveListener) {
        mOnFloatViewMoveListener = onFloatViewMoveListener;
    }

    public Context getContext() {
        return mContext;
    }

    public <T extends View> T findViewById(int resId) {
        return mFloatRootView != null ? (T) mFloatRootView.findViewById(resId) : null;
    }

    public View getFloatRootView() {
        return mFloatRootView;
    }

    public LayoutParams getWmParams() {
        return wmParams;
    }

    public void setWmParams(LayoutParams wmParams) {
        this.wmParams = wmParams;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    public void setWindowManager(WindowManager windowManager) {
        mWindowManager = windowManager;
    }

    public void setFloatRootLayout(View floatRootLayout) {
        mFloatRootView = floatRootLayout;
    }

    public int getStatusBarHeight() {
        return mStatusBarHeight;
    }

    public boolean isAdsorb() {
        return mIsAdsorb;
    }

    /**
     * 设置悬浮框是否吸附在屏幕边缘
     *
     * @param isAdsorb
     */
    public void setIsAdsorb(boolean isAdsorb) {
        mIsAdsorb = isAdsorb;
    }

    /**
     * 悬浮框移动监听
     *
     * @author xx
     */
    public interface OnFloatViewMoveListener {
        /**
         * 移动
         * @param location
         */
        void onMove(Location location);
    }

    /**
     * 控件位置类型
     *
     * @author xx
     */
    public enum PositionType {
        LEFT, RIGHT, TOP, BOTTOM
    }

    /**
     * 获取控件的位置类型
     *
     * @param event
     * @return
     */
    private PositionType getPositionType(MotionEvent event) {
        PositionType type;
        int height = Utils.getScreenHeight(getContext()) / 5;
        int width = Utils.getScreenWidth(getContext()) / 2;
        if (event.getRawY() < height) {
            type = PositionType.TOP;
        } else if (event.getRawY() > (height * 4)) {
            type = PositionType.BOTTOM;
        } else {
            if (event.getRawX() > width) {
                type = PositionType.RIGHT;
            } else {
                type = PositionType.LEFT;
            }
        }
        return type;
    }

    private void updateGravity(MotionEvent event) {
        PositionType type = getPositionType(event);
        switch (type) {
            case TOP:
                updateRotateView(-90);
                updateViewPosition((int) (event.getRawX() - event.getX()), 0);
                break;
            case BOTTOM:
                updateRotateView(90);
                updateViewPosition((int) (event.getRawX() - event.getX()), Utils.getScreenHeight(getContext()));
                break;
            case RIGHT:
                updateRotateView(0);
                updateViewPosition(Utils.getScreenWidth(getContext()), (int) (event.getRawY() - event.getY()) - mStatusBarHeight);
                break;
            case LEFT:
                updateRotateView(180);
                updateViewPosition(0, (int) (event.getRawY() - event.getY()) - mStatusBarHeight);
                break;
            default:
                break;
        }
    }

    /**
     * 旋转悬浮框图标
     *
     * @param degress
     */
    private void updateRotateView(int degress) {
        if (mRotateView != null) {
            if (degress != 0) {
                mRotateView.setImageBitmap(Utils.rotate(mBitmap, degress));
            } else {
                mRotateView.setImageBitmap(mBitmap);
            }
        }
    }

    /**
     * 悬浮控件的位置信息
     *
     * @author xx
     * @Date 2016-11-23 上午10:11:11
     */
    public class Location {
        /**
         * 记录当前手指位置在屏幕上的横坐标
         */
        public float mXInScreen;
        /**
         * 记录当前手指位置在屏幕上的纵坐标
         */
        public float mYInScreen;
        /**
         * 记录手指按下时在屏幕上的横坐标,用来判断单击事件
         */
        public float mXDownInScreen;
        /**
         * 记录手指按下时在屏幕上的纵坐标,用来判断单击事件
         */
        public float mYDownInScreen;
        /**
         * 记录手指按下时在小悬浮窗的View上的横坐标
         */
        public float mXInView;
        /**
         * 记录手指按下时在小悬浮窗的View上的纵坐标
         */
        public float mYInView;

        public float getXInScreen() {
            return mXInScreen;
        }

        public void setXInScreen(float mXInScreen) {
            this.mXInScreen = mXInScreen;
        }

        public float getYInScreen() {
            return mYInScreen;
        }

        public void setYInScreen(float mYInScreen) {
            this.mYInScreen = mYInScreen;
        }

        public float getXDownInScreen() {
            return mXDownInScreen;
        }

        public void setXDownInScreen(float mXDownInScreen) {
            this.mXDownInScreen = mXDownInScreen;
        }

        public float getYDownInScreen() {
            return mYDownInScreen;
        }

        public void setYDownInScreen(float mYDownInScreen) {
            this.mYDownInScreen = mYDownInScreen;
        }

        public float getXInView() {
            return mXInView;
        }

        public void setXInView(float mXInView) {
            this.mXInView = mXInView;
        }

        public float getYInView() {
            return mYInView;
        }

        public void setYInView(float mYInView) {
            this.mYInView = mYInView;
        }
    }

}
