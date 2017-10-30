package com.zmsoft.TestTool.dao;

import android.content.Context;
import android.content.Intent;

import com.zbar.lib.CameraProvider;
import com.zbar.lib.CaptureActivity;

/**
 * Created by gly on 2017/10/26.
 */

public class ActivityStarter {
    public static void startCapActivity(Context context) {
        if (!CameraProvider.hasBackFacingCamera()) return;
        Intent intent = new Intent(context, CaptureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
