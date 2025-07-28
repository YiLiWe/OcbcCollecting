package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ScrollView;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.ViewUtil;

public class SavingAccountHoldingActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.dashboard.finance.saving.account_holding.SavingAccountHoldingActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        positioningView("currency_constraint_layout");
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        if (event.getCode() == 0) {
            getHandler().postDelayed(this::finish, 5_000);
        }
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        switch (ViewUtil.getResourceEntryName(view)){
            case "currency_constraint_layout":
                click(view);
                positioningView("nested_scroll_view");
                break;
            case "nested_scroll_view":
                if (view instanceof ScrollView scrollView){
                    scrollView.post(()->scrollView.fullScroll(View.FOCUS_DOWN));
                }
                break;
        }
    }

}
