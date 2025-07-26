package com.example.ocbccollecting.task.bean;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeviceBean {
    @JSONField(name = "pass")
    private String pass;
    @JSONField(name = "payPass")
    private long payPass;
}
