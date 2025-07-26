package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.example.ocbccollecting.hook.ActivityLifecycleCallbacks;
import com.example.ocbccollecting.task.bean.ImputationBeanOrder;
import com.example.ocbccollecting.task.bean.OcbcImputationBean;
import com.example.ocbccollecting.utils.ViewUtil;

import lombok.Data;

@Data
public abstract class BaseDialog {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Dialog dialog;
    private ActivityLifecycleCallbacks activityLifecycleCallbacks;

    public void onCreated(Dialog dialog) {
        this.dialog = dialog;
    }

    public abstract String getName();

    public boolean isName(TextView textView) {
        return getName().equals(textView.getText().toString());
    }

    public void positioningView(String ids) {
        View view = ViewUtil.findViewById(dialog, ids);
        if (view == null) {
            handler.postDelayed(() -> positioningView(ids), 1000);
        } else {
            positioningView(view);
        }
    }

    public ImputationBeanOrder getOcbcImputationBean() {
        if (activityLifecycleCallbacks == null) {
            return null;
        }
        return activityLifecycleCallbacks.getRunTask().getOcbcImputationBean();
    }

    public void setOcbcImputationBean(ImputationBeanOrder ocbcImputationBean) {
        if (activityLifecycleCallbacks == null) return;
        activityLifecycleCallbacks.getRunTask().setImputationBeanOrder(ocbcImputationBean);
    }


    public void positioningView(View view) {

    }
}
