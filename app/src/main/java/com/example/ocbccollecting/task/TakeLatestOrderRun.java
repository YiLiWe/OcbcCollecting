package com.example.ocbccollecting.task;

import com.alibaba.fastjson2.JSON;
import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.ActivityLifecycleCallbacks;
import com.example.ocbccollecting.rest.OkhttpUtils;
import com.example.ocbccollecting.task.bean.MessageBean;
import com.example.ocbccollecting.task.bean.TakeLatestOrderBean;
import com.example.ocbccollecting.utils.Logs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class TakeLatestOrderRun implements Runnable {
    private TakeLatestOrderBean takeLatestOrderBean;
    private long balance = 0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ActivityLifecycleCallbacks activityLifecycleCallbacksX;
    private boolean isRun = false;
    private final List<String> Banks = List.of("GOPAY", "OVO", "SHOPEEPAY", "DANA", "LINKAJA");
    private final Map<String, String> Banks2 = new HashMap<>() {
        {
            put("OVO", "OVO");
            put("LinkAja", "LINKAJA");
            put("ShopeePay", "SHOPEEPAY");
            put("Gopay Customer", "GOPAY");
            put("DANA", "DANA");
        }
    };

    public TakeLatestOrderRun(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.activityLifecycleCallbacksX = activityLifecycleCallbacks;
    }

    public void start() {
        if (this.activityLifecycleCallbacksX.getAppConfig().getMode().equals("0")) return;
        if (getRun()) return;
        Logs.d("开始获取代付订单");
        scheduler.scheduleWithFixedDelay(this, 20, 20, TimeUnit.SECONDS);
        setRun(true);
    }

    @Override
    public void run() {
        try {
            if (activityLifecycleCallbacksX.getRunActivities().isEmpty()) {
                if (getTakeLatestOrderBean() != null) {
                    setTakeLatestOrderBean(null);
                }
                setRun(false);
                shutdownNow();
                return;
            }
            if (takeLatestOrderBean != null || balance == 0) {
                return;
            }
            TakeLatestOrderBean takeLatestOrderBean1 = getOrder();
            if (takeLatestOrderBean1 == null) return;
            setTakeLatestOrderBean(takeLatestOrderBean1);
            if (takeLatestOrderBean1.isMoney()) {//钱包转账
                activityLifecycleCallbacksX.onMessageEvent(new MessageEvent(999));
            } else {
                activityLifecycleCallbacksX.onMessageEvent(new MessageEvent(888));
            }
        } catch (Exception e) {
            Logs.d("任务异常:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public TakeLatestOrderBean getOrder() {
        String text = OkhttpUtils.takeLatestPayoutOrder(activityLifecycleCallbacksX.getAppConfig(), getBalance());
        if (text == null) return null;
        Logs.d(text);
        MessageBean messageBean = JSON.to(MessageBean.class, text);
        if (messageBean == null) return null;
        if (messageBean.getData() == null) return null;
        TakeLatestOrderBean takeLatestOrderBean1 = messageBean.getData().to(TakeLatestOrderBean.class);
        takeLatestOrderBean1.setBankName(getBank(takeLatestOrderBean1.getBankName()));
        if (Banks.contains(takeLatestOrderBean1.getBankName())) {
            takeLatestOrderBean1.setMoney(true);
        }
        Logs.d(JSON.toJSONString(takeLatestOrderBean1));
        return takeLatestOrderBean1;
    }

    private synchronized void setRun(Boolean bool) {
        this.isRun = bool;
    }

    private boolean getRun() {
        return this.isRun;
    }

    private String getBank(String key) {
        String value = Banks2.get(key);
        if (value == null) return key;
        return value;
    }

    //释放线程
    public void shutdownNow() {
        scheduler.shutdownNow();
    }

}