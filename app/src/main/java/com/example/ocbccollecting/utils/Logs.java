package com.example.ocbccollecting.utils;

import android.util.Log;

public class Logs {
    private static final String TAG = "代码";

    public static void d(String log){
        logLargeString(log);
    }

    public static void log(String log) {
        logLargeString(log);
    }

    public static void logLargeString(String str) {
        if (str.length() > 4000) {
            Log.i(TAG, str.substring(0, 4000));
            logLargeString(str.substring(4000));
        } else {
            Log.i(TAG, str);
        }
    }
}
