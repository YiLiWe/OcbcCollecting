package com.example.ocbccollecting.hook;

import android.content.Context;
import android.util.Log;

import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.rest.OkhttpUtils;
import com.example.ocbccollecting.task.bean.ImputationBeanOrder;
import com.example.ocbccollecting.task.bean.TakeLatestOrderBean;
import com.example.ocbccollecting.utils.Logs;

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private static AppCrashHandler instance;
    private Context context;
    private ActivityLifecycleCallbacks activityLifecycleCallbacks;

    private AppCrashHandler() {
    }

    public static synchronized AppCrashHandler getInstance() {
        if (instance == null) {
            instance = new AppCrashHandler();
        }
        return instance;
    }

    public void init(ActivityLifecycleCallbacks activityLifecycleCallbacks, Context context) {
        this.context = context;
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logs.d("AppCrashHandler"+"Uncaught exception in thread " + t.getName()+"|"+e.getMessage());
        // 处理异常，如记录日志或上传到服务器
        handleException(e);
        // 退出应用
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    public ImputationBeanOrder getOcbcImputationBean() {
        if (activityLifecycleCallbacks == null) {
            return null;
        }
        return activityLifecycleCallbacks.getRunTask().getOcbcImputationBean();
    }

    public TakeLatestOrderBean getTakeLatestOrderBean() {
        if (activityLifecycleCallbacks == null) {
            return null;
        }
        return activityLifecycleCallbacks.getTakeLatestOrderRun().getTakeLatestOrderBean();
    }

    public void setTakeLatestOrderBean(TakeLatestOrderBean takeLatestOrderBean) {
        activityLifecycleCallbacks.getTakeLatestOrderRun().setTakeLatestOrderBean(takeLatestOrderBean);
    }

    public void setOcbcImputationBean(ImputationBeanOrder ocbcImputationBean) {
        activityLifecycleCallbacks.getRunTask().setImputationBeanOrder(ocbcImputationBean);
    }

    public APPConfig getAppConfig() {
        if (activityLifecycleCallbacks == null) {
            return null;
        }
        return activityLifecycleCallbacks.getAppConfig();
    }

    private void handleException(Throwable e) {
        if (getOcbcImputationBean() != null) {
            OkhttpUtils.postOcbcImputation(getAppConfig(), getOcbcImputationBean(), 2, e.getMessage());
            setOcbcImputationBean(null);
        } else {
            OkhttpUtils.PullPost(0, e.getMessage(), getAppConfig(), getTakeLatestOrderBean());
            setTakeLatestOrderBean(null);
        }
        // 记录异常信息到文件或上传到服务器
    }
}