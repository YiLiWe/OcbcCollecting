package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;
import android.widget.TextView;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.hook.ActivityLifecycleCallbacks;
import com.example.ocbccollecting.utils.Logs;
import com.example.ocbccollecting.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class HookDialog {
    private final Dialog dialog;
    private final ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private final List<BaseDialog> dialogs = new ArrayList<>() {{
        add(new SelectBankDialog());
        add(new AccountNotFoundDialog());
        add(new ChooseTransferPurposeDialog());
        add(new LoadingFailedDialog());
        add(new ChooseSourceDialog());
        add(new SessionHasExpiredDialog());
    }};

    public HookDialog(Dialog dialog, ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.dialog = dialog;
        this.activityLifecycleCallbacks = activityLifecycleCallbacks;
    }

    public void start() {
        Logs.d("弹窗显示");
        //选择之类的
        boolean isTitle = isDialogTitle("title_txt");
        boolean isText = isDialogTitle("middle_text_view");
        if (isTitle && isText) {
            if (activityLifecycleCallbacks.getRunTask().getOcbcImputationBean() == null) {
                //没有归集账单，直接重启
                activityLifecycleCallbacks.onMessageEvent(new MessageEvent(4));
                return;
            }
            return;
        }
    }

    private boolean isDialogTitle(String ids) {
        TextView view = ViewUtil.findViewById(dialog, ids);
        if (view != null) {
            Logs.d("弹窗标题：" + view.getText().toString());
            dialogs.forEach(baseDialog -> {
                if (baseDialog.isName(view)) {
                    baseDialog.setActivityLifecycleCallbacks(activityLifecycleCallbacks);
                    baseDialog.onCreated(dialog);
                }
            });
            return false;
        } else {
            return true;
        }
    }
}
