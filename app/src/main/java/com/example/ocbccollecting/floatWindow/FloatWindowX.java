package com.example.ocbccollecting.floatWindow;

import android.app.Activity;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.example.ocbccollecting.view.SystemManagementLayout;
import com.limbo.floatwindow.FloatWindow;
import com.limbo.floatwindow.draggable.MovingDraggable;

import lombok.Getter;

@Getter
public class FloatWindowX {
    private static FloatWindowX floatWindow;
    private SystemManagementLayout systemManagementLayout;

    private int[] getScreenCenter(@NonNull Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;
        return new int[]{centerX, centerY};
    }

    public static FloatWindowX getInstance(Activity activity) {
        if (FloatWindowX.floatWindow == null) {
            FloatWindowX.floatWindow = new FloatWindowX();
            if (activity != null) {
                floatWindow.show(activity);
            }
        }
        return floatWindow;
    }

    public void printLog(String log) {
        if (systemManagementLayout == null) return;
        systemManagementLayout.printLog(log);
    }

    public void show(Activity activity) {
        int[] center = getScreenCenter(activity);
        int centerY = center[1];
        systemManagementLayout = new SystemManagementLayout(activity);
        FloatWindow.INSTANCE.init()
                .setDraggable(new MovingDraggable())
                .setContentView(systemManagementLayout)
                .setAbsoluteXY(0, centerY);
        FloatWindow.INSTANCE.getInstance().show(activity);
    }


    public void destroy() {
        if (FloatWindowX.floatWindow != null) {
            FloatWindowX.floatWindow = null;
            FloatWindow.INSTANCE.getInstance().hide();
        }
    }
}
