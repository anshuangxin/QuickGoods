package com.zmsoft.TestTool.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.zmsoft.TestTool.bluetooth.BluetoothConnectUtil;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.SharedPreferencesUtil;
import com.zmsoft.TestTool.utils.ToastUtil;

import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;
    public static String userId = "10827";
    public BluetoothConnectUtil bluetoothService;
    public static boolean debug = true;
    private int mVersionCode;
    private String mVersionName;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (shouldInit()) {
            getCurrentVersion(this);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                builder.detectFileUriExposure();
            }
            Logger.log("init_main_process:");
            CrashReport.initCrashReport(getApplicationContext(), "c5d68181f5", false);
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);// 初始化 JPush
            initJpushNotifycation();
            initBlueTooth();
            initUpdate();
            initSopHix();
        }
    }

    private void initSopHix() {
        Logger.log("mVersionName: " +mVersionName);
        SophixManager.getInstance().setContext(this)
                .setAppVersion(mVersionName)
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            ToastUtil.showToast("补丁加载成功:" + info);
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
    }


    private void initUpdate() {
        UpdateConfig.getConfig()
                // 必填：数据更新接口,url与checkEntity两种方式任选一种填写
                .url("http://119.23.209.169/apkUpdaterService/getUploadMessage?name=getApkpath")
//                .checkEntity(new CheckEntity().setMethod(HttpMethod.GET).setUrl("http://www.baidu.com"))
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws Exception {
                        /* 此处根据上面url或者checkEntity设置的检查更新接口的返回数据response解析出
                         * 一个update对象返回即可。更新启动时框架内部即可根据update对象的数据进行处理
                         */
                        Logger.log(response);
                        JSONObject object = new JSONObject(response);
                        Update update = new Update();
                        // 此apk包的下载地址
                        update.setUpdateUrl(object.optString("update_url"));
                        // 此apk包的版本号
                        update.setVersionCode(object.optInt("update_ver_code"));
                        // 此apk包的版本名称
                        update.setVersionName(object.optString("update_ver_name"));
                        // 此apk包的更新内容
                        update.setUpdateContent(object.optString("update_content"));
                        // 此apk包是否为强制更新
                        update.setForced(false);
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(object.optBoolean("ignore_able", false));
                        return update;
                    }
                })
                // TODO: 2016/5/11 除了以上两个参数为必填。以下的参数均为非必填项。
                // 检查更新接口是否有新版本更新的回调。
//                .checkCB(callback)
                // apk下载的回调
//                .downloadCB(callback)
                // 自定义更新检查器。
                /*.updateChecker(new UpdateChecker() {
                    @Override
                    public boolean check(Update update) {
//                           此处根据上面jsonParser解析出的update对象来判断是否此update代表的
//                           版本应该被更新。返回true为需要更新。返回false代表不需要更新
                        return false;
                    }
                })
                 // 自定义更新接口的访问任务
                .checkWorker(new UpdateWorker() {
                    @Override
                    protected String check(CheckEntity url) throws Exception {
                        // TODO: 2016/5/11 此处运行于子线程。在此进行更新接口访问
                        return null;
                    }
                })
                // 自定义apk下载任务
                .downloadWorker(new DownloadWorker() {
                    @Override
                    protected void download(String url, File file) throws Exception {
                        // TODO: 2016/5/11 此处运行于子线程，在此进行文件下载任务
                    }
                })
                // 自定义下载文件缓存,默认下载至系统自带的缓存目录下
                .fileCreator(new ApkFileCreator() {
                    @Override
                    public File create(String versionName) {
                        // TODO: 2016/5/11 versionName 为解析的Update实例中的update_url数据。在些可自定义下载文件缓存路径及文件名。放置于File中
                        return null;
                    }
                })
                // 自定义更新策略，默认WIFI下自动下载更新
             */.strategy(new UpdateStrategy() {
            @Override
            public boolean isShowUpdateDialog(Update update) {
                // 是否在检查到有新版本更新时展示Dialog。
                return true;
            }

            @Override
            public boolean isAutoInstall() {
                // 是否自动更新.当为自动更新时。代表下载成功后不通知用户。直接调起安装。
                return false;
            }

            @Override
            public boolean isShowDownloadDialog() {
                // 在APK下载时。是否显示下载进度的Dialog
                return true;
            }
        })/*
                // 自定义检查出更新后显示的Dialog，
                .updateDialogCreator(new DialogCreator() {
                    @Override
                    public Dialog create(Update update, Activity context) {
                        // TODO: 2016/5/11 此处为检查出有新版本需要更新时的回调。运行于主线程，在此进行更新Dialog的创建
                        return null;
                    }
                })
                // 自定义下载时的进度条Dialog
                .downloadDialogCreator(new DownloadCreator() {
                    @Override
                    public UpdateDownloadCB create(Update update, Activity activity) {
                        // TODO: 2016/5/11 此处为正在下载APK时的回调。运行于主线程。在此进行Dialog自定义与显示操作。
                        // TODO: 2016/5/11 需要在此创建并返回一个UpdateDownloadCB回调。用于对Dialog进行更新。
                        return null;
                    }
                })
                // 自定义下载完成后。显示的Dialog
                .installDialogCreator(new InstallCreator() {
                    @Override
                    public Dialog create(Update update, String s, Activity activity) {
                        // TODO: 2016/5/11 此处为下载APK完成后的回调。运行于主线程。在此创建Dialog
                        return null;
                    }
                })*/;


    }

    public void getCurrentVersion(Context context) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            mVersionCode = packageInfo.versionCode;
            mVersionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initBlueTooth() {
        bluetoothService = BluetoothConnectUtil.getInstance(this);
        boolean isOpenBlueTooth = SharedPreferencesUtil.getBoolean(this, "isOpenBlueTooth");
        if (isOpenBlueTooth) {
            if (!bluetoothService.isOpen()) {
                MyApplication.getInstance().bluetoothService.openBluetooth(this);
            } else {
                MyApplication.getInstance().bluetoothService.searchDevices();
            }
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
