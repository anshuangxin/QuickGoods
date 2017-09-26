package com.gly.quickgoods.dao;

import android.content.Context;

import com.gly.quickgoods.constants.SpeechConstants;
import com.gly.quickgoods.modle.SpeechInfo;
import com.gly.quickgoods.utils.Logger;
import com.gly.quickgoods.utils.Mp4Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.getNumericValue;

/**
 * Created by gly on 2017/9/22.
 */

public class SpeechDao {
    static Map<SpeechConstants, String> data = new HashMap<>();

    static {
        data.put(SpeechConstants.RMB, "songs/元.mp3");
        data.put(SpeechConstants.SHITANG_DINGDAN, "songs/堂食订单.mp3");
        data.put(SpeechConstants.WAI_MAI, "songs/外卖订单.mp3");
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

    public static void CustomRead1(Context mContext, String num) {
        Mp4Util instance = Mp4Util.getInstance(mContext);
        List<SpeechInfo> speechInfos = new ArrayList<>();
        speechInfos.add(new SpeechInfo(data.get(SpeechConstants.SHITANG_DINGDAN)));
        speechInfos.add(new SpeechInfo(data.get(SpeechConstants.IS_RECEIVE)));
        speechInfos.addAll(getNumBerSpeechs(num));
        speechInfos.add(new SpeechInfo(data.get(SpeechConstants.RMB)));
        instance.startPlay(speechInfos);

    }

    public static List<SpeechInfo> getNumBerSpeechs(String num) {
        char[] chars = String.valueOf(num).toCharArray();
        List<SpeechInfo> speechInfos = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            Logger.log("chart: " + c);
            if (c == '.') {
                speechInfos.add(new SpeechInfo(data.get(SpeechConstants.POINT)));
            } else {
                int number = getNumericValue((int) c);
                Logger.log("number: " + number);
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
        return speechInfos;
    }

}
