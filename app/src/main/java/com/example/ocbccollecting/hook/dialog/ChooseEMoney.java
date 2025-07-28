package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class ChooseEMoney extends BaseDialog {
    @Override
    public String getName() {
        return "Choose e-money";
    }

    @Override
    public void onCreated(Dialog dialog) {
        super.onCreated(dialog);
        positioningView("list_selection_rv");
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
        List<View> views = getBank(view);
        if (views.isEmpty()) {
            getHandler().postDelayed(() -> handlerSelectBank(view), 1000);
        } else {
            selectBank(views);
        }
    }

    private void selectBank(List<View> views) {
        views.forEach(view -> {
            TextView textView = ViewUtil.findViewById(view, "textView");
            if (getOcbcImputationBean() != null) {
                if (ViewUtil.equalsBank(textView, getOcbcImputationBean().getBank())) {
                    ViewUtil.performClick(view);
                    getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(89));
                }
            } else {
                if (ViewUtil.equalsBank(textView, getTakeLatestOrderBean().getBankName())) {
                    ViewUtil.performClick(view);
                    getActivityLifecycleCallbacks().onMessageEvent(new MessageEvent(89));
                }
            }
        });
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
}
