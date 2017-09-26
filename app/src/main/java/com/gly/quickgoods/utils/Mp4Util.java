package com.gly.quickgoods.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.gly.quickgoods.modle.SpeechInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gly on 2017/9/22.
 */

public class Mp4Util {
    private static final int PLAY = 213;
    private final AssetManager assets;
    private MediaPlayer mediaPlayer;
    private List<SpeechInfo> speechInfos = new ArrayList<>();
    public static Context mContext;
    private int currIndex = -1;

    private Mp4Util(Context context) {
        mediaPlayer = new MediaPlayer();
        assets = context.getAssets();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    public void setData(List<SpeechInfo> data) {
        this.speechInfos.clear();
        this.speechInfos.addAll(data);
        mediaPlayer.pause();
        currIndex = -1;
    }

    public static class Mp4UtilHolder {
        private static final Mp4Util instance = new Mp4Util(mContext);
    }

    public static Mp4Util getInstance(Context context) {
        mContext = context.getApplicationContext();
        return Mp4UtilHolder.instance;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY:
                    startPlay();
                    break;
                default:
                    break;
            }
        }
    };

    public void startPlay(List<SpeechInfo> infos) {
        this.speechInfos.addAll(infos);
        startPlay();
    }

    public void startPlay() {
        if (currIndex == -1) {
            currIndex++;
        }
        AssetFileDescriptor fileDescriptor = null;
        try {
            Logger.log(speechInfos.get(currIndex).message);
            mediaPlayer.reset();
            fileDescriptor = assets.openFd(speechInfos.get(currIndex).message);
            mediaPlayer
                    .setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Logger.log(speechInfos.get(currIndex).message);
            Logger.log("--------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void next() {
        currIndex++;
        if (currIndex < speechInfos.size()) {
            handler.sendEmptyMessageDelayed(PLAY, speechInfos.get(currIndex).delayTime);
        }
    }
}
