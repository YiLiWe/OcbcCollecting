package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

public class N23Activity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.apps.N23";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.ocbcnisp.onemobileapp", "com.ocbcnisp.byon.ui.splashscreen.SplashScreenActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }
}
