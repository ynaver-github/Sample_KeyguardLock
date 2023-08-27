package com.example.keyguardlock;

import android.util.Log;

public class AppLog {
    public static final String TAG = "Sample";

    public static boolean sLogOn = true;
    private static final int REAL_METHOD_POS = 2;
    private static int sUiHashCode = 0;

    private static String prefix() {
        StackTraceElement[] ste = new Throwable().getStackTrace();
        StackTraceElement realMethod = ste[REAL_METHOD_POS];

        int curHash = Thread.currentThread().hashCode();
//        String thread = "Other";
        String thread = "";
        if (curHash == sUiHashCode) {
            thread = "UI";
        }
        return "[" + realMethod.getFileName() + ":"
                + realMethod.getLineNumber() + ":"
                + realMethod.getMethodName() + "()-" + "[Thread:" + thread + "] ";
    }

    public static void setUiThreadHashCode(int uiHashCode) {
        sUiHashCode = uiHashCode;
    }

    public static void d(String tag, String msg) {
        if (sLogOn) {
            Log.d(tag, prefix() + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLogOn) {
            Log.i(tag, prefix() + msg);
        }
    }

    public static void e(String tag, String msg) {
        // if (LOG_ON) {
        Log.e(tag, prefix() + msg);
        // }
    }

    public static void v(String tag, String msg) {
        if (sLogOn) {
            Log.v(tag, prefix() + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sLogOn) {
            Log.w(tag, prefix() + msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (sLogOn) {
            Log.d(tag, prefix() + msg, tr);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (sLogOn) {
            Log.i(tag, prefix() + msg, tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (sLogOn) {
            Log.e(tag, prefix() + msg, tr);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (sLogOn) {
            Log.v(tag, prefix() + msg, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (sLogOn) {
            Log.w(tag, prefix() + msg, tr);
        }
    }

    public static void debugStackTrace(String from, boolean error) {
        Log.e(TAG, "[Debug] Printing stack trace : from - " + from);
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (int i = 3; i < elements.length; i++) {
            StackTraceElement s = elements[i];
            if (error) {
                Log.e(TAG, "[Debug] \tat " + from + s.getClassName() + "." +
                        s.getMethodName() + "(" +
                        s.getFileName() + ":" +
                        s.getLineNumber() + ")");
            } else {
                Log.i(TAG, "[Debug] \tat " + from + s.getClassName() + "." +
                        s.getMethodName() + "(" +
                        s.getFileName() + ":" +
                        s.getLineNumber() + ")");
            }
        }
    }
}
