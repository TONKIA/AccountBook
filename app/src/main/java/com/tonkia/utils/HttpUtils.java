package com.tonkia.utils;


import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class HttpUtils {
    private static final String url = "http://192.168.43.208/Checkout";

    //获取验证码
    public static void requestIdentifyCode(String number, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("type", "requestCode")
                .add("phone", number)
                .build();

        final Request request = new Request.Builder()
                .url(url + "/UserServlet")//请求的url
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(callback);
    }

    //验证码登录
    public static void loginWithCode(String number, String code, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("type", "loginCode")
                .add("phone", number)
                .add("code", code)
                .build();
        final Request request = new Request.Builder()
                .url(url + "/UserServlet")//请求的url
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(callback);
    }

    //自动登录
    public static void login(String number, String secretCode, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("type", "login")
                .add("phone", number)
                .add("secretCode", secretCode)
                .build();

        final Request request = new Request.Builder()
                .url(url + "/UserServlet")//请求的url
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(callback);
    }
}
