package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.rest.OkhttpUtils;

public class SessionHasExpiredDialog extends BaseDialog {
    @Override
    public String getName() {
        return "Session Has Expired";
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
        getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
    }
}
