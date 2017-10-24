package com.zmsoft.TestTool.dao;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gly on 2017/10/20.
 */

public class DingDanChannel {
    static Map<Integer, String> mPay = new HashMap<>();
    static Map<Integer, String> mSltmode = new HashMap<>();
    static Map<Integer, String> mSource = new HashMap<>();

    static {
        mSource.put(2, "公众号商城");
        mSource.put(3, "小程序");
        mSource.put(4, "实体店");
        mSource.put(5, "小程序");
        mSource.put(8, "美团外卖");
        mSource.put(9, "百度外卖");
        mSource.put(10, "饿了么外卖");

        mSltmode.put(0, "其他");
        mSltmode.put(1, "堂食");
        mSltmode.put(2, "外卖");
        mSltmode.put(3, "自提");
        mSltmode.put(4, "预定");
        mSltmode.put(5, "直接买单");
        mSltmode.put(6, "优惠买单");
        mSltmode.put(7, "充值");

        mPay.put(1, "微信支付");
        mPay.put(2, "货到付款");
        mPay.put(3, "");
        mPay.put(4, "现金支付");
        mPay.put(5, "支付宝支付");
        mPay.put(6, "快币支付");
        mPay.put(7, "三方外卖平台支付");
        mPay.put(8, "挂单");
    }

    public static String getChannelText(int source, int pay, int sltmode) {
        StringBuilder builder = new StringBuilder();
        String s = mSource.get(source);
        String s2 = mPay.get(pay);
        String s3 = mSltmode.get(sltmode);

        if (!TextUtils.isEmpty(s)) {
            builder.append(s + "·");
        }
        if (!TextUtils.isEmpty(s)) {
            builder.append(s2);
        }
        if (!TextUtils.isEmpty(s)) {
            builder.append("·" + s3);
        }

        return builder.toString();

    }
}
