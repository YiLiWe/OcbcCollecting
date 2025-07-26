package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.example.ocbccollecting.utils.ViewUtil;

/**
 * 引导页
 * com.ocbcnisp.byon.ui.prelogin.PreLoginActivity
 */
public class PreLoginActivity extends BaseActivity {

    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.prelogin.PreLoginActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        positioningView("login_btn");
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        ViewUtil.performClick(view);
    }
}
