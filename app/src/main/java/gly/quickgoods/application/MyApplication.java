package gly.quickgoods.application;

import android.app.Application;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import gly.quickgoods.utils.TtsHelper;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;
    public String is_one = "";

    @Override
    public void onCreate() {
        super.onCreate();
        TtsHelper.getInstance().init(this);
        instance = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);            // 初始化 JPush
        initJpushNotifycation();
        Set<String> tag = new HashSet<>();
        tag.add("tag2");
        JPushInterface.setAliasAndTags(this, "gly", tag, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.d(TAG, s + set.toString());
            }
        });
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
