package com.example.ocbccollecting.hook.bean;

import android.content.Context;
import android.text.TextUtils;

import lombok.Data;

@Data
public class APPConfig {
    private String url;//后台地址
    private String cardNumber;//卡号
    private Context context;//上下文

    public boolean isData() {
        return !TextUtils.isEmpty(url) && !TextUtils.isEmpty(cardNumber);
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(url) || TextUtils.isEmpty(cardNumber);
    }
}
