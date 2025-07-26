package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.ViewUtil;

/**
 * 登录界面
 * com.ocbcnisp.byon.ui.auth.login.LoginActivity
 */
public class LoginActivity extends BaseActivity implements Handler.Callback {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.auth.login.LoginActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        if (getActivityLifecycleCallbacks().getDeviceBean() == null) return;
        positioningView("edit_txt");
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        if (event.getCode() == 6) {
            positioningView("edit_txt");
        }else if (event.getCode() == 7) {
            Toast.makeText(getActivity(), "获取后台设备信息设备", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        if (view instanceof EditText editText) {
            login(editText);
        }
    }

    public void login(EditText editText) {
        if (ViewUtil.editText(editText, getActivityLifecycleCallbacks().getDeviceBean().getPass())) {
            View view = ViewUtil.findViewById(getActivity(), "btn_view");
            ViewUtil.performClick(view);
        }
    }
}
