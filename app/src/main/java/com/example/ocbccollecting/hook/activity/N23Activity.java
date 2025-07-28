package com.example.ocbccollecting.hook.activity;

import android.app.Activity;

import com.example.ocbccollecting.utils.AppUtils;

public class N23Activity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.apps.N23";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        AppUtils.startMainActivity(activity);
    }
}
