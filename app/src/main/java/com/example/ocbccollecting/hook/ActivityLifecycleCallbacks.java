package com.example.ocbccollecting.hook;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.activity.BaseActivity;
import com.example.ocbccollecting.hook.activity.DashboardActivity;
import com.example.ocbccollecting.hook.activity.ErrorActivity;
import com.example.ocbccollecting.hook.activity.LoginActivity;
import com.example.ocbccollecting.hook.activity.N23Activity;
import com.example.ocbccollecting.hook.activity.PinTransactionActivity;
import com.example.ocbccollecting.hook.activity.PreLoginActivity;
import com.example.ocbccollecting.hook.activity.PurchaseLandingPageActivity;
import com.example.ocbccollecting.hook.activity.PurchaseSummaryActivity;
import com.example.ocbccollecting.hook.activity.ReceiptTransactionActivity;
import com.example.ocbccollecting.hook.activity.SavingAccountHoldingActivity;
import com.example.ocbccollecting.hook.activity.TransferActivity;
import com.example.ocbccollecting.hook.activity.WebViewActivity;
import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.hook.dialog.HookDialog;
import com.example.ocbccollecting.task.RunTask;
import com.example.ocbccollecting.task.TakeLatestOrderRun;
import com.example.ocbccollecting.task.bean.DeviceBean;
import com.example.ocbccollecting.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private final RunTask runTask;
    private final TakeLatestOrderRun takeLatestOrderRun;
    private final APPConfig appConfig;
    private DeviceBean deviceBean;
    private final List<BaseActivity> activities = new ArrayList<>() {
        {
            add(new PreLoginActivity());
            add(new LoginActivity());
            add(new DashboardActivity());
            add(new SavingAccountHoldingActivity());
            add(new ErrorActivity());
            add(new TransferActivity());
            add(new PinTransactionActivity());
            add(new ReceiptTransactionActivity());
            add(new WebViewActivity());
            add(new N23Activity());
            add(new PurchaseSummaryActivity());
            add(new PurchaseLandingPageActivity());
        }
    };

    private final List<BaseActivity> RunActivities = new ArrayList<>();

    public ActivityLifecycleCallbacks(APPConfig appConfig) {
        this.appConfig = appConfig;
        XposedBridge.hookAllMethods(Dialog.class, "show", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.thisObject instanceof Dialog dialog) {
                    HookDialog hookDialog = new HookDialog(dialog, ActivityLifecycleCallbacks.this);
                    hookDialog.start();
                }
            }
        });
        this.runTask = new RunTask(this);
        this.takeLatestOrderRun = new TakeLatestOrderRun(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activities.forEach(baseActivity -> {
            if (baseActivity.isActivity(activity)) {
                BaseActivity baseActivity1 = newInstance(baseActivity.getClass());
                baseActivity1.setActivityLifecycleCallbacks(this);
                baseActivity1.onCreated(activity);
                RunActivities.add(baseActivity1);
            }
        });
        String name = activity.getClass().getName();
        Logs.d("界面：" + name);
    }

    public BaseActivity newInstance(Class<? extends BaseActivity> baseActivity) {
        try {
            return baseActivity.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            return null;
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        RunActivities.forEach(baseActivity -> {
            if (baseActivity.isActivity(activity)) {
                baseActivity.onResumed(activity);
            }
        });
    }

    /**
     * 判断是否是代收
     *
     * @return true=是代收 false=是归集
     */
    public boolean isPayMode() {
        return getAppConfig().getMode().equals("0");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        for (int i = 0; i < RunActivities.size(); i++) {
            BaseActivity baseActivity = RunActivities.get(i);
            if (baseActivity.isActivity(activity)) {
                baseActivity.onDestroyed();
                RunActivities.remove(i);
            }
        }
    }

    public void onMessageEvent(MessageEvent messageEvent) {
        RunActivities.forEach(baseActivity -> {
            //登录失效
            if (!baseActivity.getActivityName().equals("com.ocbcnisp.byon.ui.dashboard.DashboardActivity")) {
                if (messageEvent.getCode() == 4) {
                    baseActivity.finish();
                }
            }
            baseActivity.onMessageEvent(messageEvent);
        });
    }
}
