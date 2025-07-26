package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.ocbccollecting.utils.Logs;
import com.example.ocbccollecting.utils.ViewUtil;

public class ReceiptTransactionActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.receipt.ReceiptTransactionActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        positioningView("status_transaction_text_view");
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        switch (ViewUtil.getResourceEntryName(getActivity(), view.getId())) {
            case "status_transaction_text_view":
                handlerStatus(view);
                break;
            case "dashboard_btn":
                ViewUtil.performClick(view);
                break;
        }
    }

    private void handlerStatus(View view) {
        if (view instanceof TextView textView) {
            String text = textView.getText().toString();
            if (!text.isEmpty()) {
                Logs.d("状态：" + text);
                positioningView("dashboard_btn");
                return;
            }
        }
        getHandler().postDelayed(() -> handlerStatus(view), 1000);
    }
}
