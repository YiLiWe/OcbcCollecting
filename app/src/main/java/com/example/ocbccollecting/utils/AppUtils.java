package com.example.ocbccollecting.utils;

import android.app.Activity;
import android.content.Intent;

public class AppUtils {

    //重新打开
    public static void startMainActivity(Activity activity) {
        Logs.d("跳转界面");
        Intent intent = new Intent();
        intent.setClassName(activity.getPackageName(), "com.ocbcnisp.byon.ui.splashscreen.SplashScreenActivity");
        activity.startActivity(intent);
        activity.finish();
    }
}
