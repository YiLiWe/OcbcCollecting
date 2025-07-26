package com.example.ocbccollecting.task;

import com.alibaba.fastjson2.JSONObject;
import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.ActivityLifecycleCallbacks;
import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.rest.OkhttpUtils;
import com.example.ocbccollecting.task.bean.ImputationBeanOrder;
import com.example.ocbccollecting.utils.Logs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RunTask implements Runnable {
    private ImputationBeanOrder imputationBeanOrder;
    private boolean isRun = false;
    private final APPConfig appConfig;
    private final ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private long money;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RunTask(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.appConfig = activityLifecycleCallbacks.getAppConfig();
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
    }

    public void start() {
        Logs.d("RunTask startX");
        if (getRun()) return;
        Logs.d("RunTask start");
        scheduler.scheduleWithFixedDelay(this, 20, 20, TimeUnit.SECONDS);
        setRun(true);
    }

    @Override
    public void run() {
        try {
            if (activityLifecycleCallbacks.getRunActivities().isEmpty()) {
                Logs.d("stop RunTask runX");
                setRun(false);
                setImputationBeanOrder(null);
                scheduler.shutdownNow();
                return;
            }
            if (getOcbcImputationBean() != null || money == 0) {
                Logs.d("stop RunTask run");
                return;
            }
            JSONObject jsonObject = OkhttpUtils.getOcbcImputation(appConfig, money);
            if (jsonObject == null) return;
            setImputationBeanOrder(jsonObject.to(ImputationBeanOrder.class));
            if (getOcbcImputationBean() == null) return;
            activityLifecycleCallbacks.onMessageEvent(new MessageEvent(5));
        } catch (Throwable e) {
            Logs.log(e.getMessage());
        }
    }

    private synchronized void setRun(Boolean bool) {
        this.isRun = bool;
    }

    private synchronized boolean getRun() {
        return this.isRun;
    }

    public synchronized void setMoney(long money) {
        this.money = money;
    }

    public synchronized ImputationBeanOrder getOcbcImputationBean() {
        return imputationBeanOrder;
    }


}
