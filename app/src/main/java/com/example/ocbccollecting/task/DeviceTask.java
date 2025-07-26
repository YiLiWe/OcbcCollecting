package com.example.ocbccollecting.task;

import android.os.Handler;

import com.alibaba.fastjson2.JSONObject;
import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.ActivityLifecycleCallbacks;
import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.rest.OkhttpUtils;
import com.example.ocbccollecting.task.bean.DeviceBean;

import lombok.Data;

//获取设备信息
@Data
public class DeviceTask implements Runnable {
    //是否运行
    private boolean isRun = false;
    //配置
    private final APPConfig appConfig;
    //生命周期回调
    private final ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private final Handler handler;

    public DeviceTask(APPConfig appConfig, ActivityLifecycleCallbacks activityLifecycleCallbacks, Handler handler) {
        this.appConfig = appConfig;
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
        this.handler = handler;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        JSONObject jsonObject = OkhttpUtils.getLoginPassword(appConfig);
        if (jsonObject == null) {
            handler.post(() -> activityLifecycleCallbacks.onMessageEvent(new MessageEvent(7)));
            handler.postDelayed(this::start, 5000);
            return;
        }
        DeviceBean deviceBean = jsonObject.to(DeviceBean.class);
        activityLifecycleCallbacks.setDeviceBean(deviceBean);
        handler.post(() -> activityLifecycleCallbacks.onMessageEvent(new MessageEvent(6)));
    }
}
