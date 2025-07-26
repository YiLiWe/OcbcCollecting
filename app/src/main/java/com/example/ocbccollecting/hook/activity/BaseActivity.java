package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.ActivityLifecycleCallbacks;
import com.example.ocbccollecting.hook.bean.APPConfig;
import com.example.ocbccollecting.task.bean.ImputationBeanOrder;
import com.example.ocbccollecting.task.bean.OcbcImputationBean;
import com.example.ocbccollecting.utils.ViewUtil;


import java.util.Timer;

import lombok.Data;

@Data
public abstract class BaseActivity implements Handler.Callback {
    private Activity activity;
    private Handler handler = new Handler(Looper.getMainLooper(), this);
    private ActivityLifecycleCallbacks activityLifecycleCallbacks;

    public void onCreated(Activity activity) {
        this.activity = activity;
    }

    public void finish() {
        if (activity == null) return;
        activity.finish();
    }

    public void positioningView(String ids) {
        View view = ViewUtil.findViewById(activity, ids);
        if (view == null) {
            handler.postDelayed(() -> positioningView(ids), 1000);
        } else {
            positioningView(view);
        }
    }

    public void positioningView(String ids, long time) {
        handler.postDelayed(() -> positioningView(ids), time);
    }


    public void click(View view) {
        boolean isCLick = ViewUtil.performClick(view);
        if (!isCLick) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("点击异常,返回首页或重启")
                    .setPositiveButton("重启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    public void positioningView(View view) {

    }

    public void onResumed(Activity activity) {

    }

    public void onMessageEvent(MessageEvent event) {
    }

    public APPConfig getAppConfig() {
        return activityLifecycleCallbacks.getAppConfig();
    }

    public ImputationBeanOrder getOcbcImputationBean() {
        if (activityLifecycleCallbacks == null) {
            return null;
        }
        return activityLifecycleCallbacks.getRunTask().getOcbcImputationBean();
    }


    public abstract String getActivityName();

    public boolean isActivity(Activity activity) {
        return activity.getClass().getName().equals(getActivityName());
    }

    public void onDestroyed() {
        this.activity = null;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
}
