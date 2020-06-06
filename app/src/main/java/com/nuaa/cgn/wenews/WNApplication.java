package com.nuaa.cgn.wenews;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;


import com.nuaa.cgn.wenews.component.ApplicationComponent;
import com.nuaa.cgn.wenews.component.DaggerApplicationComponent;
import com.nuaa.cgn.wenews.module.ApplicationModule;
import com.nuaa.cgn.wenews.module.HttpModule;
import com.nuaa.cgn.wenews.utils.ContextUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * desc: .
 * author: wecent .
 * date: 2017/9/2 .
 */
public class WNApplication extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;

    private static WNApplication mWNApplication;

    private static Context mContext;//上下文

    private static Thread mMainThread;//主线程

    private static long mMainThreadId;//主线程id

    private static Looper mMainLooper;//循环队列

    private static Handler mHandler;//主线程Handler

    public static int width = 0;

    public static int height = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mWNApplication = this;
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
        BGASwipeBackManager.getInstance().init(this);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        LitePal.initialize(this);
        width = ContextUtils.getSreenWidth(WNApplication.getContext());
        height = ContextUtils.getSreenHeight(WNApplication.getContext());
    }

    public static WNApplication getInstance() {
        return mWNApplication;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    /**
     * 重启当前应用
     */
    public static void restartApp() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

}
