package com.gly.quickgoods.request;

import com.gly.quickgoods.utils.okhttp.listener.DisposeDataHandle;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.constants.HttpConstants;
import com.gly.quickgoods.utils.okhttp.CommonOkHttpClient;
import com.gly.quickgoods.utils.okhttp.request.CommonRequest;
import com.gly.quickgoods.utils.okhttp.request.RequestParams;
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

    /**
     * 提交订单
     */
    public static void Calculate(String id, int num, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("name", id);
        params.put("num", num + "");
        CommonOkHttpClient.get(CommonRequest.
                createPostRequest(HttpConstants.CALCULATE, params), new DisposeDataHandle(disposeDataListener));
    }

    /**
     * 分类
     */
    public static void firstAll(DisposeDataListener disposeDataListener) {
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.FIRST_ALL, null), new DisposeDataHandle(disposeDataListener));
    }

    /**
     * 分页
     */
    public static void ifyClass(int pager, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("page", pager + "");
        CommonOkHttpClient.get(CommonRequest.
                createPostRequest(HttpConstants.IFY_CLASS, params), new DisposeDataHandle(disposeDataListener));
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
     * 请求全部数据接口
     */
    public static void getPossess(String u_id, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("uid", u_id);
        CommonOkHttpClient.get(CommonRequest.
                createGetRequest(HttpConstants.POSSESS, params), new DisposeDataHandle(disposeDataListener));
    }
}
