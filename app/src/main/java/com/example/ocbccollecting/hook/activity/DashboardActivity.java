package com.example.ocbccollecting.hook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ocbccollecting.eventbus.event.MessageEvent;
import com.example.ocbccollecting.task.bean.TakeLatestOrderBean;
import com.example.ocbccollecting.utils.ViewUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import lombok.Data;
import lombok.SneakyThrows;

public class DashboardActivity extends BaseActivity implements Handler.Callback {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private View home_swipe_layout;

    @Override
    public String getActivityName() {
        return "com.ocbcnisp.byon.ui.dashboard.DashboardActivity";
    }

    @Override
    public void onCreated(Activity activity) {
        super.onCreated(activity);
        getActivityLifecycleCallbacks().getTakeLatestOrderRun().start();
        getActivityLifecycleCallbacks().getRunTask().start();

        if (getActivityLifecycleCallbacks().isPayMode()) {
            positioningView("home_swipe_layout");
        } else {
            positioningView("navigation_finance");
        }
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getCode() == 4) {
            startMainActivity();
        } else if (event.getCode() == 5) {//点击查看数据
            positioningView("navigation_home");
        } else if (event.getCode() == 888) {
            RunTransfer_menu();
        } else if (event.getCode() == 999) {
            RunMenuRecycler();
        }
    }


    //刷新
    private void refresh() {
        Class<? extends View> classA = home_swipe_layout.getClass();
        Stream.of(classA.getDeclaredFields())
                .filter(field -> field.getName().equals("e"))
                .forEach(this::handleRefresh);
    }

    @SneakyThrows(Exception.class)
    private void handleRefresh(Field field) {
        Object object = field.get(home_swipe_layout);
        if (object == null) return;
        Class<?> classA = object.getClass();
        Stream.of(classA.getDeclaredMethods())
                .filter(method -> method.getName().equals("a"))
                .forEach(method -> getHandler().sendMessage(getHandler().obtainMessage(1, new MessageEntity(object, method))));
    }

    private void invoke(MessageEntity messageEntity) {
        try {
            messageEntity.method.invoke(messageEntity.object);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == 1 && msg.obj instanceof MessageEntity entity) {
            invoke(entity);
            return true;
        }
        return super.handleMessage(msg);
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        scheduler.shutdownNow();
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
            positioningView("navigation_home");
        } else {
            positioningView("navigation_finance", 10_000);
        }
    }

    @Data
    private class MessageEntity {
        private final Object object;
        private final Method method;
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
                ViewUtil.performClick(view);
                break;
            case "navigation_home"://首页
                ViewUtil.performClick(view);
                RunTransfer_menu();
                break;
            case "home_swipe_layout":
                this.home_swipe_layout = view;
                scheduler.scheduleWithFixedDelay(this::refresh, 10, 15, TimeUnit.SECONDS);
                initData();
                break;
        }
    }


    //判断是否有订单
    private void initData() {
        TakeLatestOrderBean takeLatestOrderBean1 = getTakeLatestOrderBean();
        if (takeLatestOrderBean1 != null) {
            if (takeLatestOrderBean1.isMoney()) {//钱包转账
                onMessageEvent(new MessageEvent(999));
            } else {
                onMessageEvent(new MessageEvent(888));
            }
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


    /**
     * 钱包转账
     */
    private void RunMenuRecycler() {
        View transfer_menu = getMenu("e-Money");
        if (transfer_menu != null) {
            ViewUtil.performClick(transfer_menu);
            return;
        }
        getHandler().postDelayed(this::RunMenuRecycler, 1000);
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
