package com.gc.speedometer.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by gc on 16/10/10.
 */
public class MyApplication extends Application {

    private static Context context;//全局的上下文
    /**
     * app的入口函数
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this; //初始化Context
    }
    /**
     * 获取全局的上下文
     * @return
     */
    public static Context getContext(){
        return context;
    }

}
