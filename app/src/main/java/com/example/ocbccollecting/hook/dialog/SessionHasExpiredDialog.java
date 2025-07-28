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
            OkhttpUtils.postOcbcImputation(getActivityLifecycleCallbacks().getAppConfig(), getOcbcImputationBean(), 2, "Session Has Expired");
            setOcbcImputationBean(null);
        } else {
            OkhttpUtils.PullPost(0, "Session Has Expired", getActivityLifecycleCallbacks().getAppConfig(), getTakeLatestOrderBean());
            setTakeLatestOrderBean(null);
        }
        getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
    }
}
