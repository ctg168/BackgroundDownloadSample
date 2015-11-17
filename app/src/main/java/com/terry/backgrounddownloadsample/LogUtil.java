package com.terry.backgrounddownloadsample;

import android.util.Log;

/**
 * Created by terry on 2015/11/9.
 */
public class LogUtil {
    private static final String TAG = "terryLog";

    public static void LogE(String logInfo) {
        Log.e(TAG, logInfo + "");
    }

    public static void LogD(String logInfo) {
        Log.d(TAG, logInfo);
    }
}
