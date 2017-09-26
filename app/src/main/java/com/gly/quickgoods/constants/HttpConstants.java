package com.gly.quickgoods.constants;

/**
 * @author: gly
 * @function: 所有请求相关地址
 * @date: 16/8/12
 */
public class HttpConstants {

    public static final String ROOT_URL = "http://www.kh95.com";


    /**
     * 登录
     */
    public static String LOGIN = ROOT_URL + "/index.php/home/logion";
    /**
     * 验证码请求连接
     */
    public static String GET_CODE = ROOT_URL + "/index.php/home/forSms";
    /**
     * 请求数据接口
     */
    public static String GET_GOODS = ROOT_URL + "/index.php/cashOrder/goods";
    /**
     * 分页
     */
    public static String IFY_CLASS = ROOT_URL + "/index.php/cashOrder/ifyclass";

    /**
     * 分类
     */
    public static String FIRST_ALL = ROOT_URL + "/admin.php/android/android/Firstall";

    /**
     * 条码收银
     */
    public static String CALCULATE = ROOT_URL + "/admin.php/android/android/goods";
    /**
     * 微信支付宝收银
     */
    public static String AJAXPAY = ROOT_URL + "/admin.php/android/android/Ajaxpay";
    /**
     * 现金收银
     */
    public static String AJAXCASH = ROOT_URL + "/admin.php/android/android/Ajaxcash";
    /**
     * 点单收银
     */
    public static String POSSESS = ROOT_URL + "/admin.php/android/android/possess";
    /**
     * 订单核验
     */
    public static String CHECK = ROOT_URL + "/admin.php/android/android/check";

    /**
     * 确认收款
     */
    public static final String COLLECTION = ROOT_URL + "/admin.php/android/android/collection";
    /**
     * 提交订单
     */
    public static final String COMMITORDER = ROOT_URL + "/admin.php/android/android/Calculate";
}


