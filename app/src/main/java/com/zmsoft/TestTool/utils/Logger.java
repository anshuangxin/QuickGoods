package com.zmsoft.TestTool.utils;

import android.util.Log;

import com.zmsoft.TestTool.application.MyApplication;

public class Logger {

    public static void log(String responseObj) {
        if (MyApplication.debug) {
            try {
                String[] split = responseObj.split(":", 2);
                Log.d(split[0], split[1]);
            } catch (Exception e) {
                Log.d("MYLOG", responseObj);
            }
        }
    }
}