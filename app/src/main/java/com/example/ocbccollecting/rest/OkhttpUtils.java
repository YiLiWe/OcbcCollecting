package com.example.ocbccollecting.rest;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.hook.bean.ReceiptBean;
import com.example.ocbccollecting.hook.entity.OnlineTransactionEntity;
import com.example.ocbccollecting.task.bean.ImputationBeanOrder;
import com.example.ocbccollecting.task.bean.MessageBean;
import com.example.ocbccollecting.task.bean.OcbcImputationBean;
import com.example.ocbccollecting.utils.Logs;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkhttpUtils {
    //通过卡号获取登录密码和支付密码
    public static JSONObject getLoginPassword(APPConfig appConfig) {
        Request request = new Request.Builder()
                .url(appConfig.getUrl() + "writeAuthentication/getDevice?card=" + appConfig.getCardNumber())
                .build();
        String result = result(request);
        if (result == null) return null;
        MessageBean messageBean = JSON.to(MessageBean.class, result);
        if (messageBean.getCode() == 200 && messageBean.getData() != null) {
            return messageBean.getData();
        }
        return null;
    }

    //获取归集订单
    public static JSONObject getOcbcImputation(APPConfig appConfig, long money) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardNumber", appConfig.getCardNumber());
        jsonObject.put("balance", money);
        RequestBody requestBody = RequestBody.Companion.create(jsonObject.toString(), MediaType.Companion.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .post(requestBody)
                .url(appConfig.getUrl() + "writeAuthentication/ImputationOrder")
                .build();
        String result = result(request);
        if (result == null) return null;
        Logs.d(result);
        MessageBean messageBean = JSON.to(MessageBean.class, result);
        if (messageBean.getCode() == 200 && messageBean.getData() != null) {
            return messageBean.getData();
        }
        return null;
    }

    //提交归集成功
    public static void postOcbcImputation(APPConfig appConfig, ImputationBeanOrder order, int state, String error) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", order.getId());
        jsonObject.put("state", state);
        jsonObject.put("error", error);
        RequestBody requestBody = RequestBody.Companion.create(jsonObject.toString(), MediaType.Companion.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .post(requestBody)
                .url(appConfig.getUrl() + "writeAuthentication/ImputationState")
                .build();
        resultCall(request);
    }

    //提交代收
    public static void postOcbcReceive(APPConfig appConfig, OnlineTransactionEntity listTransactionDTO) {
        Logs.d(appConfig.getUrl());
        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody = RequestBody.Companion.create(JSON.toJSONString(listTransactionDTO), mediaType);
        Request request = new Request.Builder()
                .post(stringBody)
                .url(String.format("%s%s", appConfig.getUrl(), "writeAuthentication/postOcbcReceive"))
                .build();
        resultCall(request);
    }

    //提交归集成功
    public static void postOcbcImputation(APPConfig appConfig, ReceiptBean.ListTransactionDTO listTransactionDTO) {
        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody = RequestBody.Companion.create(JSON.toJSONString(listTransactionDTO), mediaType);
        Request request = new Request.Builder()
                .post(stringBody)
                .url(String.format("%s%s", appConfig.getUrl(), "authentication/postOcbcImputation"))
                .build();
        resultCall(request);
    }

    //获取归集数据
    public static OcbcImputationBean getOcbcImputationBean(long money, APPConfig appConfig) {
        RequestBody requestBody = new FormBody.Builder()
                .add("balance", String.valueOf(money))
                .add("deviceCardNumber", appConfig.getCardNumber())
                .build();
        Request request = new Request.Builder()
                .url(String.format("%s%s", appConfig.getUrl(), "authentication/getOcbcImputation"))
                .post(requestBody)
                .build();
        String result = result(request);
        if (result == null) return null;
        MessageBean ocbcImputationBeanMessageBean = JSON.to(MessageBean.class, result);
        if (ocbcImputationBeanMessageBean.getCode() == 0 && ocbcImputationBeanMessageBean.getData() != null) {
            return ocbcImputationBeanMessageBean.getData().to(OcbcImputationBean.class);
        }
        return null;
    }

    public static void resultCall(Request request) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Logs.d("提交失败" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (responseBody == null) return;
                String text = responseBody.string();
                Logs.d("提交结果:" + text);
            }
        });
    }

    public static String result(Request request) {
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) return responseBody.string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
