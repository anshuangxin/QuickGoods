package com.zmsoft.TestTool.dao;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.modle.SpeechInfo;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.Mp4Util;
import com.zmsoft.TestTool.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zmsoft.TestTool.fragment.DingDanLiuFragment.IS_SPEECH;
import static java.lang.Character.getNumericValue;

/**
 * Created by gly on 2017/9/22.
 */

public class SpeechDao {
    static List<String> data = new ArrayList<>();
    private static boolean isOpen;

    static {
        isOpen = SharedPreferencesUtil.getBoolean(MyApplication.getInstance(), IS_SPEECH, true);
        data.add("songs/0.mp3");
        data.add("songs/1.mp3");
        data.add("songs/2.mp3");
        data.add("songs/3.mp3");
        data.add("songs/4.mp3");
        data.add("songs/5.mp3");
        data.add("songs/6.mp3");
        data.add("songs/7.mp3");
        data.add("songs/8.mp3");
        data.add("songs/9.mp3");
        data.add("songs/10.mp3");
        data.add("songs/点.mp3");
        data.add("songs/叮咚.mp3");
        data.add("songs/核验成功.mp3");
        data.add("songs/核验失败.mp3");
        data.add("songs/欢迎使用快货新零售云收银系统.mp3");
        data.add("songs/您有百度外卖订单.mp3");
        data.add("songs/您有饿了么外卖订单.mp3");
        data.add("songs/您有美团外卖订单.mp3");
        data.add("songs/您有堂食订单，收款已成功，到账.mp3");
        data.add("songs/您有新的订单，请及时处理.mp3");
        data.add("songs/您有新订单，已支付成功.mp3");
        data.add("songs/您有自提订单，收款已成功，到账.mp3");
        data.add("songs/您有自营外卖订单，收款已成功，到账.mp3");
        data.add("songs/收款已成功，到账.mp3");
        data.add("songs/堂食订单.mp3");
        data.add("songs/外卖订单.mp3");
        data.add("songs/元.mp3");
        data.add("songs/自提订单.mp3");
        data.add("songs/百.mp3");
        data.add("songs/万.mp3");
        data.add("songs/亿.mp3");
        data.add("songs/千.mp3");
        data.add("songs/微信收款成功.mp3");
        data.add("songs/挂单成功.mp3");
        data.add("songs/支付宝收款成功.mp3");
        data.add("songs/退单成功.mp3");
    }


    /**
     * 直接收款：“叮咚，收款已成功，到账**元”
     *
     * @param num
     */
    public static void receive(String num) {
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            List<SpeechInfo> speechInfos = new ArrayList<>();
            speechInfos.add(new SpeechInfo("songs/叮咚.mp3"));
            speechInfos.add(new SpeechInfo("songs/收款已成功，到账.mp3"));
            speechInfos.addAll(getNumBerSpeechs(num));
            speechInfos.add(new SpeechInfo("songs/元.mp3"));
            instance.startPlay(speechInfos);
        }
    }

    /**
     * 打开：欢迎使用快货新零售云收银系统
     */
    public static void open() {
        Logger.log("open");
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            instance.startPlay(new SpeechInfo("songs/欢迎使用快货新零售云收银系统.mp3"));
        }
    }

    private static AssetManager assets = MyApplication.getInstance().getAssets();

    private static List<MediaPlayer> players = new ArrayList<>();

    public static void speechByPosition(List<Integer> indexs) {
        players.clear();
        if (isOpen) {
            for (int i = 0; i < indexs.size(); i++) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                int index = indexs.get(i);
                try {
                    AssetFileDescriptor fileDescriptor = assets.openFd(data.get(index));
                    mediaPlayer
                            .setDataSource(fileDescriptor.getFileDescriptor(),
                                    fileDescriptor.getStartOffset(),
                                    fileDescriptor.getLength());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                players.add(mediaPlayer);
            }
            long delayed = 0;
            for (MediaPlayer player : players) {
                Message message = handler.obtainMessage();
                message.obj = player;
                message.what = 100;
                handler.sendMessageDelayed(message, delayed);
                delayed += player.getDuration() - 30;
            }
        }
    }

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    MediaPlayer mediaPlayer = (MediaPlayer) msg.obj;
                    mediaPlayer.start();
                    Message message = handler.obtainMessage();
                    message.what = 200;
                    message.obj = msg.obj;
                    handler.sendMessageDelayed(message, mediaPlayer.getDuration());
                    break;
                case 200:
                    MediaPlayer player = (MediaPlayer) msg.obj;
                    player.stop();
                    player.release();
                    players.remove(player);
                    break;
            }
        }
    };


    /**
     * 核验成功：“核验成功”
     * 核验失败：“核验失败”
     *
     * @param isSuccess
     */
    public static void check(boolean isSuccess) {
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            String type = isSuccess ? "songs/核验成功.mp3" : "songs/核验失败.mp3";
            instance.startPlay(new SpeechInfo(type));
        }

    }


    public static List<SpeechInfo> getNumBerSpeechs(String num) {
        List<SpeechInfo> speechInfos = new ArrayList<>();
        if (isOpen) {
            char[] chars = String.valueOf(num).toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (c == '.') {
                    speechInfos.add(new SpeechInfo("songs/点.mp3"));
                } else {
                    int number = getNumericValue((int) c);
                    switch (number) {
                        case 0:
                            speechInfos.add(new SpeechInfo("songs/0.mp3"));
                            break;
                        case 1:
                            speechInfos.add(new SpeechInfo("songs/1.mp3"));
                            break;
                        case 2:
                            speechInfos.add(new SpeechInfo("songs/2.mp3"));
                            break;
                        case 3:
                            speechInfos.add(new SpeechInfo("songs/3.mp3"));
                            break;
                        case 4:
                            speechInfos.add(new SpeechInfo("songs/4.mp3"));
                            break;
                        case 5:
                            speechInfos.add(new SpeechInfo("songs/5.mp3"));
                            break;
                        case 6:
                            speechInfos.add(new SpeechInfo("songs/6.mp3"));
                            break;
                        case 7:
                            speechInfos.add(new SpeechInfo("songs/7.mp3"));
                            break;
                        case 8:
                            speechInfos.add(new SpeechInfo("songs/8.mp3"));
                            break;
                        case 9:
                            speechInfos.add(new SpeechInfo("songs/9.mp3"));
                            break;
                    }
                }
            }
        }
        return speechInfos;
    }


    public static void isOpenSpeech(boolean b) {
        isOpen = b;
        SharedPreferencesUtil.putBoolean(MyApplication.getInstance(), IS_SPEECH, isOpen);
    }

    public static void guadan() {
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            instance.startPlay(new SpeechInfo("挂单成功.mp3"));
        }
    }
}
