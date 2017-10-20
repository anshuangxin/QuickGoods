package com.bjyzqs.kuaihuo_yunshouyin.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bjyzqs.kuaihuo_yunshouyin.utils.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;
    public static String userId = "10827";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (shouldInit()) {
            Logger.log("init_main_process");
            CrashReport.initCrashReport(getApplicationContext(), "c5d68181f5", false);
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);// 初始化 JPush
            initJpushNotifycation();
            Set<String> tag = new HashSet<>();
            tag.add("tag2");
            JPushInterface.setAliasAndTags(this, "gly", tag, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    Logger.log(s + set.toString());
                }
            });
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            Log.i("Jiaqi", "my.pid -> " + myPid + ",mainProcessName -> " + mainProcessName);
            Log.i("Jiaqi", "info.pid -> " + info.pid + ",info.processName -> " + info.processName);
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    private void initJpushNotifycation() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
//        builder.statusBarDrawable = R.drawable.ic_launcher;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(0, builder);
    }

    public static MyApplication getInstance() {
        return instance;
    }


}
