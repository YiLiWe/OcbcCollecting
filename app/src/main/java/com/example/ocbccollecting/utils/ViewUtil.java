package com.example.ocbccollecting.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ViewUtil {

    public static <t extends View> t findViewById(View activity, String name) {
        int viewId = getViewId(activity, name);
        if (viewId <= 0) return null;
        return activity.findViewById(viewId);
    }

    public static boolean equalsBank(TextView textView, String bank) {
        if (textView == null) return false;
        String text = textView.getText().toString();
        return text.equals(bank);
    }

    public static <t extends View> t findViewById(Dialog activity, String name) {
        int viewId = getViewId(activity, name);
        if (viewId <= 0) return null;
        return activity.findViewById(viewId);
    }


    public static <t extends View> t findViewById(Dialog activity, int id) {
        Window window = activity.getWindow();
        if (window == null) return null;
        return window.findViewById(id);
    }


    public static String getText(TextView textView) {
        if (textView == null) return null;
        return textView.getText().toString();
    }


    public static void setVisibility(View view, int v) {
        if (view == null) return;
        view.setVisibility(v);
    }

    public static <t extends View> t findViewById(Activity activity, String name) {
        int viewId = getViewId(activity, name);
        if (viewId <= 0) return null;
        return activity.findViewById(viewId);
    }

    public static List<View> getRecyclerItem(View view) throws Exception {
        List<View> views = new ArrayList<>();
        Class<?> mClass = view.getClass();
        Method getLayoutManager = mClass.getMethod("getLayoutManager");
        Object LayoutManager = getLayoutManager.invoke(view);
        if (LayoutManager == null) return views;
        Class<?> LayoutManagerClass = LayoutManager.getClass();
        Method getChildCount = LayoutManagerClass.getMethod("getChildCount");
        Object ChildCount = getChildCount.invoke(LayoutManager);
        if (ChildCount instanceof Integer integer) {
            if (integer == 0) {
                return views;
            } else {
                Method method = LayoutManagerClass.getMethod("getChildAt", int.class);
                for (int i = 0; i < integer; i++) {
                    Object object = method.invoke(LayoutManager, i);
                    if (object instanceof View view1) {
                        views.add(view1);
                    }
                }
            }
        }
        return views;
    }


    /**
     * 递归查找所有EditText控件
     * @param viewGroup 父容器
     * @return 包含所有EditText的列表
     */
    public static List<EditText> findAllEditTexts(ViewGroup viewGroup) {
        if (viewGroup == null) return new ArrayList<>();
        List<EditText> editTexts = new ArrayList<>();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof EditText) {
                editTexts.add((EditText) child);
            } else if (child instanceof ViewGroup) {
                editTexts.addAll(findAllEditTexts((ViewGroup) child));
            }
        }
        return editTexts;
    }

    public static void printViewInfo(View view, int depth) {
        // 生成缩进（根据层级深度）
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  ");
        }

        // 获取类名
        String className = view.getClass().getName();

        // 获取ID名称
        String idName = "null";
        if (view.getId() != View.NO_ID) {
            try {
                idName = view.getResources().getResourceEntryName(view.getId());
            } catch (Resources.NotFoundException e) {
                idName = "0x" + Integer.toHexString(view.getId());
            }
        }

        // 打印信息
        Logs.d(indent.toString() + className + " id=" + idName);

        // 递归处理子View
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                printViewInfo(viewGroup.getChildAt(i), depth + 1);
            }
        }
    }

    public static boolean click(View view) {
        try {
            View view1 = recursionTravelView(view);
            if (view1 == null) {
                Logs.log("获取可点击控件失败");
                return false;
            }
            View.OnClickListener clickListener = getOnClickListenerReflectively(view1);
            if (clickListener != null) {
                clickListener.onClick(view1);
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static View recursionTravelView(View rootView) {
        if (rootView.hasOnClickListeners()) {
            return rootView;
        }
        if (rootView instanceof ViewGroup viewGroup) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                View view = recursionTravelView(child);
                if (view != null) return view;
            }
        }
        return null;
    }

    public static View.OnClickListener getOnClickListenerReflectively(View view) {
        try {
            // 获取 View 的私有字段 mListenerInfo
            Field listenerInfoField = View.class.getDeclaredField("mListenerInfo");
            listenerInfoField.setAccessible(true);
            Object listenerInfo = listenerInfoField.get(view);

            if (listenerInfo != null) {
                // 获取 ListenerInfo 类中的 mOnClickListener 字段
                Class<?> listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
                Field onClickListenerField = listenerInfoClass.getDeclaredField("mOnClickListener");
                onClickListenerField.setAccessible(true);

                return (View.OnClickListener) onClickListenerField.get(listenerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean performClick(View myButton) {
        if (myButton == null) {
            Logs.log("按钮不存在");
            return false;
        }
        return click(myButton);
    }

    public static int getViewId(Dialog activity, String name) {
        if (activity == null) return 0;
        Resources r = activity.getContext().getResources();
        return r.getIdentifier(name, "id", activity.getContext().getPackageName());
    }

    public static int getViewId(Activity activity, String name) {
        if (activity == null) return 0;
        Resources r = activity.getResources();
        return r.getIdentifier(name, "id", activity.getPackageName());
    }

    public static String getResourceEntryName(Context activity, int id) {
        return activity.getResources().getResourceEntryName(id);
    }
    public static String getResourceEntryName(View view) {
        return view.getResources().getResourceEntryName(view.getId());
    }


    public static int getViewId(View activity, String name) {
        if (activity == null) return 0;
        Resources r = activity.getResources();
        return r.getIdentifier(name, "id", activity.getContext().getPackageName());
    }

    public static boolean editText(EditText editText, String text) {
        if (editText == null) return false;
        String str = editText.getText().toString();
        if (str.equals(text)) return true;
        editText.setText(text);
        return true;
    }

    public static void print(String text) {
        Log.d("代码", text);
    }

    public static void debug(String text, Throwable e) {
        Log.d("代码", text, e);
    }

}
