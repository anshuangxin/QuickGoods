package com.bjyzqs.kuaihuo_yunshouyin.modle;

/**
 * Created by gly on 2017/9/22.
 */

public class SpeechInfo {
    public String message;
    public long delayTime;

    public SpeechInfo(String message, long delayTime) {
        this.message = message;
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return "SpeechInfo{" +
                "message='" + message + '\'' +
                ", delayTime=" + delayTime +
                '}';
    }

    public SpeechInfo(String message) {
        this.message = message;
    }
}
