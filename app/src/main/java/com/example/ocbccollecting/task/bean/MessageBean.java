package com.example.ocbccollecting.task.bean;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

@Data
public class MessageBean {
    private int code;
    private String msg;
    private JSONObject data;
}
