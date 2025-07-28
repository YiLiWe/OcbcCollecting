package com.example.ocbccollecting.hook.activity;

import android.app.Activity;

import com.example.ocbccollecting.utils.AppUtils;

public class ErrorActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.common_ui.base.common.ErrorActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        AppUtils.startMainActivity(activity);
    }
}
