package com.example.ocbccollecting.hook.dialog;

import android.app.Dialog;
import android.view.View;

import com.example.ocbccollecting.utils.Logs;
import com.example.ocbccollecting.utils.ViewUtil;

public class ChooseSourceDialog extends BaseDialog {
    @Override
    public String getName() {
        return "Choose source of fund";
    }

    @Override
    public void onCreated(Dialog dialog) {
        super.onCreated(dialog);
        positioningView("card_currency_view");
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        Logs.d("选择钱包");
        ViewUtil.performClick(view);
    }
}
