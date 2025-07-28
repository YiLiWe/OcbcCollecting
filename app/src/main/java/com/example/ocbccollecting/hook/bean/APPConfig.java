package com.example.ocbccollecting.hook.bean;

import android.content.Context;
import android.text.TextUtils;

import lombok.Data;

@Data
public class APPConfig {
    private String url;//后台地址
    private String cardNumber;//卡号
    private Context context;//上下文
    private String mode;//模式 0=代收归集 1=代付
    private String payURL;

    public boolean isData() {
        return !TextUtils.isEmpty(url) && !TextUtils.isEmpty(cardNumber);
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(url) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(payURL);
    }
}
