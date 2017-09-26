package com.gly.quickgoods.dao;

import com.gly.quickgoods.modle.CommitOrderInfo;
import com.gly.quickgoods.utils.Logger;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataHandle;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.constants.HttpConstants;
import com.gly.quickgoods.utils.okhttp.CommonOkHttpClient;
import com.gly.quickgoods.utils.okhttp.request.CommonRequest;
import com.gly.quickgoods.utils.okhttp.request.RequestParams;
import com.gly.quickgoods.utils.okhttp.response.CommonSimpleCalback;

import okhttp3.Request;

/**
 * Created by gly on 2017/9/13.
 */

public class ConnectDao {
    public static void Login(String phone, String code, String pwd, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("telephone", phone);
        params.put("captch", code);
        params.put("passwords", pwd);
        final Request loginRequest = CommonRequest.
                createPostRequest(HttpConstants.LOGIN, params);
        CommonOkHttpClient.get(loginRequest, new DisposeDataHandle(disposeDataListener));

//
//        final OkHttpClient client = CommonOkHttpClient.getOkHttpClient();
//        final Call loginCall = client.newCall(loginRequest);
//        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(MyApplication.getInstance());
//
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //非异步执行
//                    Response loginResponse = loginCall.execute();
//                    //测试是否登陆成功
//                    System.out.println(loginResponse.body().string());
//                    //获取返回数据的头部
//                    Headers headers = loginResponse.headers();
//                    HttpUrl loginUrl = loginRequest.url();
//                    //获取头部的Cookie,注意：可以通过Cooke.parseAll()来获取
//                    List<Cookie> cookies = Cookie.parseAll(loginUrl, headers);
//                    //防止header没有Cookie的情况
//                    if (cookies != null) {
//                        //存储到Cookie管理器中
//                        client.cookieJar().saveFromResponse(loginUrl, cookies);//这样就将Cookie存储到缓存中了
//                        Logger.log("cookie:" + cookies);
//                    }
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }.start();

    }

    /**
     * 获取验证码
     */
    public static void getVerCode(String phone, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("mobile", phone);
        CommonOkHttpClient.get(CommonRequest.
                createPostRequest(HttpConstants.GET_CODE, params), new DisposeDataHandle(disposeDataListener));
    }

    /*
      条码收银
     */
    public static void Calculate(String sku_id, String user_id, DisposeDataListener<String> disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("sku_id", sku_id);
        params.put("user_id", user_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.CALCULATE, params), new CommonSimpleCalback(new DisposeDataHandle(disposeDataListener)));
    }

    /*
      提交订单
     */
    public static void commitOrder(RequestParams params, DisposeDataListener<CommitOrderInfo> disposeDataListener) {
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.COMMITORDER, params), new DisposeDataHandle(disposeDataListener));
    }

    /*
      现金收银
     */
    public static void Ajaxcash(String order_price, String phone, String user_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("order_price", order_price);
        params.put("phone", phone);
        params.put("user_id", user_id);
        params.put("uid", user_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.AJAXCASH, params), new DisposeDataHandle(disposeDataListener));
    }

    /*
     *支付宝微信收银
     */
    public static void Ajaxpay(String pay, String codes_txt, String order_price, String phone, String user_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("order_price", order_price);
        params.put("pay", pay);
        params.put("codes_txt", codes_txt);
        params.put("phone", phone);
        params.put("user_id", user_id);
        params.put("uid", user_id);
//        ?pay=5&codes_txt=3654&order_price=321.00&phone=13823579661&user_id=10827
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.AJAXPAY, params), new DisposeDataHandle(disposeDataListener));
    }

    /**
     * 分类
     */
    public static void firstAll(String user_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.FIRST_ALL, params), new DisposeDataHandle(disposeDataListener));
    }

    /**
     * 分页
     */
    public static void ifyClass(String parent, String user_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("parent", parent);
        params.put("user_id", user_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.IFY_CLASS, params), new DisposeDataHandle(disposeDataListener));
    }

    /**
     * 请求数据接口
     */
    public static void getGoods(String sku_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("Sku_id", sku_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.GET_GOODS, params), new DisposeDataHandle(disposeDataListener));
    }


    /**
     * 点单收银
     */
    public static void getPossess(String u_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("uid", u_id);
        params.put("user_id", u_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.POSSESS, params), new DisposeDataHandle(disposeDataListener));
    }

    /**
     * 订单核验
     */
    public static void check(String goods_id, String user_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("goods_id", goods_id);
        params.put("user_id", user_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.CHECK, params), new DisposeDataHandle(disposeDataListener));
    }

    /*
    *确认收款
    */
    public static void collection(String codes_txt, String pay, String order_id, String price, String order_price, String phone, String userId, DisposeDataListener<String> disposeDataListener) {
        //pay=1&
        // order_id=20170925090751494
        // &price=0.01
        // &order_price=0.01
        // &uid=10827
        // &user_id=10827
        // &phone=13823579661
        RequestParams params = new RequestParams();
        params.put("pay", pay);
        params.put("codes_txt", codes_txt);
        Logger.log(order_id);
        params.put("order_id", order_id);
        params.put("price", price);
        params.put("order_price", order_price);
        params.put("uid", userId);
        params.put("user_id", userId);
        params.put("phone", phone);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.COLLECTION, params), new DisposeDataHandle(disposeDataListener));
    }
}
