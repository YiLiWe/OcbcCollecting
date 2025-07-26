package com.example.ocbccollecting.hook.activity;

import android.app.Activity;

public class WebViewActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.webview.WebViewActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        activity.finish();
    }
}
