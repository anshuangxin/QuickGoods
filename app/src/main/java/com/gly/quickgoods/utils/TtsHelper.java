package com.gly.quickgoods.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import java.util.Locale;

import com.gly.quickgoods.application.MyApplication;


public class TtsHelper {
    private static final TtsHelper instance = new TtsHelper();
    private static String TAG = TtsHelper.class.getSimpleName();
    // 语音合成对象
    private TextToSpeech mTts;
    private SharedPreferences mSharedPreferences;
    private SpeechLinstener speechLinstener;
    private boolean isSpeak = false;
    private boolean isInit = false;

    public void init(Context context) {
        mTts = new TextToSpeech(context, mTtsInitListener);
//        mTts.setOnUtteranceProgressListener(utteranceProgressListener);
        mSharedPreferences = context.getSharedPreferences("mts", Context.MODE_PRIVATE);
    }

    public boolean isInit() {
        return isInit;
    }

    public static TtsHelper getInstance() {
        return instance;
    }

    UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String s) {
            isSpeak = true;
            if (null != speechLinstener) {
                speechLinstener.onStart(s);
            }
        }

        @Override
        public void onDone(String s) {
            isSpeak = false;
            if (null != speechLinstener) {
                speechLinstener.onDone(s);
            }
        }

        @Override
        public void onError(String s) {
            isSpeak = false;
            if (null != speechLinstener) {
                speechLinstener.onError(s);
            }
        }
    };

    private TextToSpeech.OnInitListener mTtsInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            isInit = true;
            if (null != speechLinstener) {
                speechLinstener.onInit(status);
            }
            Logger.log("onInit: " + status);
            if (status == TextToSpeech.SUCCESS) {/**如果装载TTS成功*/
                int result = mTts.setLanguage(Locale.ENGLISH);/**有Locale.CHINESE,但是不支持中文*/
                if (result == TextToSpeech.LANG_MISSING_DATA/**表示语言的数据丢失。*/
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {/**语言不支持*/
                    Toast.makeText(MyApplication.getInstance(), "我说不出口", Toast.LENGTH_SHORT).show();
                } else {
                    mTts.speak("I miss you", TextToSpeech.QUEUE_FLUSH,
                            null);
                }
            }
        }
    };

    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
    }

    public void play(String text) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        int code = mTts.speak(text,
                TextToSpeech.QUEUE_FLUSH, null);
        Logger.log("code: " + code);
    }

    public boolean isSpeaking() {
        return isSpeak;
    }

    public void setSpeechLinstener(SpeechLinstener speechLinstener) {
        this.speechLinstener = speechLinstener;
    }

    public interface SpeechLinstener {
        void onInit(int status);

        void onStart(String message);

        void onDone(String message);

        void onError(String message);
    }

}
