package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.rest.OkhttpUtils;

//加载信息失败
public class LoadingFailedDialog extends BaseDialog {
    @Override
    public String getName() {
        return "Loading Failed";
    }

    @Override
    public void onCreated(Dialog dialog) {
        super.onCreated(dialog);
        if (getOcbcImputationBean() != null) {
            OkhttpUtils.postOcbcImputation(getActivityLifecycleCallbacks().getAppConfig(), getOcbcImputationBean(), 2, "Account Not Found");
            setOcbcImputationBean(null);
        } else {
            OkhttpUtils.PullPost(2, "Account Not Found", getActivityLifecycleCallbacks().getAppConfig(), getTakeLatestOrderBean());
            setTakeLatestOrderBean(null);
        }
        dialog.dismiss();
        getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
    }
}
