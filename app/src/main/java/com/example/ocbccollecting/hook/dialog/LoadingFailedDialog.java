package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.Logs;

//加载信息失败
public class LoadingFailedDialog extends BaseDialog {
    @Override
    public String getName() {
        return "Loading Failed";
    }

    @Override
    public void onCreated(Dialog dialog) {
        super.onCreated(dialog);
        Logs.d("登录失效");
        dialog.dismiss();
        getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(4));
    }
}
