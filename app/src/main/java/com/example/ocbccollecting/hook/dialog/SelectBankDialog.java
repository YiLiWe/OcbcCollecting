package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.Logs;
import com.example.ocbccollecting.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class SelectBankDialog extends BaseDialog {

    @Override
    public void onCreated(Dialog dialog) {
        super.onCreated(dialog);
        if (getOcbcImputationBean() == null && getTakeLatestOrderBean() == null) {
            dialog.dismiss();
            return;
        }
        positioningView("list_selection_rv");
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        switch (ViewUtil.getResourceEntryName(view.getContext(), view.getId())) {
            case "list_selection_rv":
                handlerSelectBank(view);
                break;
        }
    }


    @SneakyThrows(Exception.class)
    private List<View> getBank(View view) {
        List<View> views = new ArrayList<>();
        if (view instanceof ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                views.add(childView);
            }
        }
        return views;
    }

    @SneakyThrows(Exception.class)
    private void handlerSelectBank(View view) {
        if (view.getLayoutParams() instanceof LinearLayout.LayoutParams layoutParams) {
            layoutParams.height = 30000;
            view.setLayoutParams(layoutParams);
        }
        List<View> views = getBank(view);
        if (views.isEmpty()) {
            getHandler().postDelayed(() -> handlerSelectBank(view), 1000);
        } else {
            selectBank(views);
            Logs.d("成功获取数量:" + views.size());
        }
    }

    private void selectBank(List<View> views) {
        views.forEach(view -> {
            TextView textView = ViewUtil.findViewById(view, "textView");
            if (getOcbcImputationBean() != null) {
                if (ViewUtil.equalsBank(textView, getOcbcImputationBean().getBank())) {
                    ViewUtil.performClick(view);
                    getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(1));
                }
            } else {
                if (ViewUtil.equalsBank(textView, getTakeLatestOrderBean().getBankName())) {
                    ViewUtil.performClick(view);
                    getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(1));
                }
            }
        });
    }

    @Override
    public String getName() {
        return "Choose Destination Bank";
    }
}
