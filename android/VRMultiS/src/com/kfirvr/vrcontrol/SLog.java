package com.kfirvr.vrcontrol;

import com.kfirvr.vrcontrols.utils.Constants;

import android.util.Log;

public class SLog {

    public static void v(String msg) {
        if (Constants.DEBUG)
            Log.v(Constants.TAG, getCaller() + msg);
    }

    public static void v(String msg, Throwable e) {
        if (Constants.DEBUG)
            Log.v(Constants.TAG, getCaller() + msg, e);
    }

    public static void d(String msg) {
        if (Constants.DEBUG)
            Log.d(Constants.TAG, getCaller() + msg);
    }

    public static void d(String msg, Throwable e) {
        if (Constants.DEBUG)
            Log.d(Constants.TAG, getCaller() + msg, e);
    }

    public static void i(String msg) {
        if (Constants.DEBUG)
            Log.i(Constants.TAG, getCaller() + msg);
    }

    public static void i(String msg, Throwable e) {
        if (Constants.DEBUG)
            Log.i(Constants.TAG, getCaller() + msg, e);
    }

    public static void w(String msg) {
        if (Constants.DEBUG)
            Log.w(Constants.TAG, getCaller() + msg);
    }

    public static void w(String msg, Throwable e) {
        if (Constants.DEBUG)
            Log.w(Constants.TAG, getCaller() + msg, e);
    }

    public static void e(String msg) {
        if (Constants.DEBUG)
            Log.e(Constants.TAG, getCaller() + msg);
    }

    public static void e(String msg, Throwable e) {
        if (Constants.DEBUG)
            Log.e(Constants.TAG, getCaller() + msg, e);
    }

    private static String getCaller() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (stack.length < 5)
            return null;
        StackTraceElement caller = stack[4];
        String className = caller.getClassName();
        int shortIndex = className.lastIndexOf(".");
        if (shortIndex > 0)
            className = className.substring(shortIndex + 1, className.length());
        return "[" + className + " - " + caller.getLineNumber() + "] ";
    }
}
