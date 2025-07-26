package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.example.ocbccollecting.utils.ViewUtil;

public class PinTransactionActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.softwaretoken.ui.PinTransactionActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        positioningView("pin_view");
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        switch (ViewUtil.getResourceEntryName(getActivity(), view.getId())) {
            case "pin_view":
                inputPin(view);
                break;
        }
    }

    private void inputPin(View view) {
        if (view instanceof EditText editText) {
            ViewUtil.editText(editText, String.valueOf(getActivityLifecycleCallbacks().getDeviceBean().getPayPass()));
        }
    }
}
