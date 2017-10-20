package com.bjyzqs.kuaihuo_yunshouyin.dao;


import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.constants.SpeechConstants;
import com.bjyzqs.kuaihuo_yunshouyin.modle.SpeechInfo;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Logger;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Mp4Util;
import com.bjyzqs.kuaihuo_yunshouyin.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjyzqs.kuaihuo_yunshouyin.fragment.DingDanLiuFragment.IS_SPEECH;
import static java.lang.Character.getNumericValue;

/**
 * Created by gly on 2017/9/22.
 */

public class SpeechDao {
    static Map<SpeechConstants, String> data = new HashMap<>();
    private static boolean isOpen;

    static {
        isOpen = SharedPreferencesUtil.getBoolean(MyApplication.getInstance(), IS_SPEECH,true);
        data.put(SpeechConstants.DING_DONG, "songs/叮咚.mp3");
        data.put(SpeechConstants.SHITANG_DINGDAN, "songs/您有堂食订单，收款已成功，到账.mp3");
        data.put(SpeechConstants.BAIDU_DINGDAN, "songs/您有百度外卖订单.mp3");
        data.put(SpeechConstants.MEITUAN_DINGDAN, "songs/您有美团外卖订单.mp3");
        data.put(SpeechConstants.ZITI_DINGDAN, "songs/您有自提订单，收款已成功，到账.mp3");
        data.put(SpeechConstants.ZIYING_DINGDAN, "songs/您有自营外卖订单，收款已成功，到账.mp3");
        data.put(SpeechConstants.ELEME_DINGDAN, "songs/您有饿了么外卖订单.mp3");
        data.put(SpeechConstants.CHECK_SUCCESS, "songs/核验成功.mp3");
        data.put(SpeechConstants.CHECK_FAIL, "songs/核验失败.mp3");
        data.put(SpeechConstants.RMB, "songs/元.mp3");
        data.put(SpeechConstants.NEW_TO_RESOLVE, "songs/您有新的订单，请及时处理.mp3");
        data.put(SpeechConstants.NEW_PAY_SUCCESS, "songs/您有新订单，已支付成功.mp3");
        data.put(SpeechConstants.IS_RECEIVE, "songs/收款已成功，到账.mp3");
        data.put(SpeechConstants.HELLO_USE_SYSTEM, "songs/欢迎使用快货新零售云收银系统.mp3");
        data.put(SpeechConstants.ONE, "songs/1.mp3");
        data.put(SpeechConstants.TWO, "songs/2.mp3");
        data.put(SpeechConstants.THREE, "songs/3.mp3");
        data.put(SpeechConstants.FOUR, "songs/4.mp3");
        data.put(SpeechConstants.FIVE, "songs/5.mp3");
        data.put(SpeechConstants.SIX, "songs/6.mp3");
        data.put(SpeechConstants.SEVEN, "songs/7.mp3");
        data.put(SpeechConstants.EIGHT, "songs/8.mp3");
        data.put(SpeechConstants.NINE, "songs/9.mp3");
        data.put(SpeechConstants.ZERO, "songs/0.mp3");
        data.put(SpeechConstants.POINT, "songs/点.mp3");
    }

    /**
     * 打开：欢迎使用快货新零售云收银系统
     */
    public static void open() {
        Logger.log("open");
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            instance.startPlay(new SpeechInfo(data.get(SpeechConstants.HELLO_USE_SYSTEM)));
        }
    }

    /**
     * 自营外卖订单：“叮咚，您有自营外卖订单，收款已成功，到账**元”
     * 美团外卖订单：“叮咚，您有美团订单，收款已成功，到账**元”
     * 饿了么外卖订单：“叮咚，您有饿了么订单，收款已成功，到账**元”
     * 百度外卖订单：“叮咚，您有百度订单，收款已成功，到账**元”
     *
     * @param orderType
     * @param num
     */
    public static void newOutOrder(SpeechConstants orderType, String num) {
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            List<SpeechInfo> speechInfos = new ArrayList<>();
            speechInfos.add(new SpeechInfo(data.get(orderType)));
            speechInfos.addAll(getNumBerSpeechs(num));
            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.RMB)));
            instance.startPlay(speechInfos);
        }

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
            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.DING_DONG)));
            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.IS_RECEIVE)));
            speechInfos.addAll(getNumBerSpeechs(num));
            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.RMB)));
            instance.startPlay(speechInfos);
        }

    }


    /**
     * 核验成功：“核验成功”
     * 核验失败：“核验失败”
     *
     * @param isSuccess
     */
    public static void check(boolean isSuccess) {
        if (isOpen) {
            Mp4Util instance = Mp4Util.getInstance();
            String type = isSuccess ? data.get(SpeechConstants.CHECK_SUCCESS) : data.get(SpeechConstants.CHECK_FAIL);
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
                    speechInfos.add(new SpeechInfo(data.get(SpeechConstants.POINT)));
                } else {
                    int number = getNumericValue((int) c);
                    switch (number) {
                        case 0:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.ZERO)));
                            break;
                        case 1:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.ONE)));
                            break;
                        case 2:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.TWO)));
                            break;
                        case 3:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.THREE)));
                            break;
                        case 4:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.FOUR)));
                            break;
                        case 5:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.FIVE)));
                            break;
                        case 6:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.SIX)));
                            break;
                        case 7:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.SEVEN)));
                            break;
                        case 8:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.EIGHT)));
                            break;
                        case 9:
                            speechInfos.add(new SpeechInfo(data.get(SpeechConstants.NINE)));
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
}
