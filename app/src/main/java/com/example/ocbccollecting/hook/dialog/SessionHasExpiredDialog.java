package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;

import com.example.ocbccollecting.eventbus.event.MessageEvent;

public class SessionHasExpiredDialog extends BaseDialog{
    @Override
    public String getName() {
        return "Session Has Expired";
    }

    @Override
    public void onCreated(Dialog dialog) {
        super.onCreated(dialog);
        getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
    }
}
