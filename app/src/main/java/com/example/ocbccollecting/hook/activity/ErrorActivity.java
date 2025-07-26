package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.content.Intent;

import com.example.ocbccollecting.utils.Logs;

public class ErrorActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.common_ui.base.common.ErrorActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        Logs.d("错误界面");
        startMainActivity();
    }

    //重新打开
    public void startMainActivity() {
        Intent intent = new Intent();
        intent.setClassName(getActivity().getPackageName(), "com.ocbcnisp.byon.ui.splashscreen.SplashScreenActivity");
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
