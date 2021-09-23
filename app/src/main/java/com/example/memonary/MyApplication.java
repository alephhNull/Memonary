package com.example.memonary;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static MyApplication getContext() {
        return mContext;
    }
}
