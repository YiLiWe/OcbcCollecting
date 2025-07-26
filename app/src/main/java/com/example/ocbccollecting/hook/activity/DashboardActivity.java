package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.utils.Logs;
import com.example.ocbccollecting.utils.ViewUtil;

import java.util.Timer;

public class DashboardActivity extends BaseActivity {
    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.dashboard.DashboardActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        Logs.d("DashboardActivity");
        getActivityLifecycleCallbacks().getRunTask().start();
        positioningView("navigation_finance");
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getCode() == 4) {
            startMainActivity();
        } else if (event.getCode() == 5) {//点击查看数据
            positioningView("navigation_home");
        }
    }

    //重新打开
    public void startMainActivity() {
        Intent intent = new Intent();
        intent.setClassName(getActivity().getPackageName(), "com.ocbcnisp.byon.ui.splashscreen.SplashScreenActivity");
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResumed(Activity activity) {
        super.onResumed(activity);
        if (getOcbcImputationBean() != null) {
            Logs.d("点击首页");
            positioningView("navigation_home");
        } else {
            Logs.d("点击查看订单");
            positioningView("navigation_finance", 10_000);
        }
    }

    @Override
    public void positioningView(View view) {
        super.positioningView(view);
        switch (ViewUtil.getResourceEntryName(getActivity(), view.getId())) {
            case "navigation_finance"://底部导航栏。准备进入账单
                ViewUtil.performClick(view);
                positioningView("child_recycler_view");
                break;
            case "child_recycler_view"://进入账单
                Logs.d("测试账单");
                ViewUtil.performClick(view);
                break;
            case "navigation_home"://首页
                ViewUtil.performClick(view);
                RunTransfer_menu();
                break;
        }
    }

    /**
     * 普通转账
     */
    private void RunTransfer_menu() {
        View transfer_menu = getMenu("Transfer");
        if (transfer_menu != null) {
            ViewUtil.performClick(transfer_menu);
            return;
        }
        getHandler().postDelayed(this::RunTransfer_menu, 1000);
    }

    private View getMenu(String text) {
        ViewGroup quick_menu_shortcut_top_layout = ViewUtil.findViewById(getActivity(), "quick_menu_shortcut_top_layout");
        ViewGroup quick_menu_shortcut_bottom_layout = ViewUtil.findViewById(getActivity(), "quick_menu_shortcut_bottom_layout");
        if (quick_menu_shortcut_bottom_layout != null && quick_menu_shortcut_top_layout != null) {
            for (int i = 0; i < quick_menu_shortcut_top_layout.getChildCount(); i++) {
                View child = quick_menu_shortcut_top_layout.getChildAt(i);
                TextView menu_title_text_view = ViewUtil.findViewById(child, "menu_title_text_view");
                if (menu_title_text_view == null) continue;
                String name = menu_title_text_view.getText().toString();
                if (text.equals(name)) return child;
            }
            for (int i = 0; i < quick_menu_shortcut_bottom_layout.getChildCount(); i++) {
                View child = quick_menu_shortcut_bottom_layout.getChildAt(i);
                TextView menu_title_text_view = ViewUtil.findViewById(child, "menu_title_text_view");
                if (menu_title_text_view == null) continue;
                String name = menu_title_text_view.getText().toString();
                if (text.equals(name)) return child;
            }
        }
        return null;
    }
}
