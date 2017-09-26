package com.gly.quickgoods.dao;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by gly on 2017/9/23.
 */

public class LongClickDao {
    private static final int LONG_CLICK = 10056;
    private MyOnTouchLinstener onTouchLinstener;
    private View view;
    private boolean isDown;
    private int count;

    private LongClickDao() {
    }

    public static class LongClickDaoHolder {
        private static final LongClickDao instance = new LongClickDao();
    }

    public static LongClickDao getInstance() {
        return LongClickDaoHolder.instance;
    }

    public void checkLongClick(View view, final MyOnTouchLinstener onTouchLinstener) {
        this.view = view;
        this.onTouchLinstener = onTouchLinstener;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDown = true;
                        count = 0;
                        handler.sendEmptyMessageDelayed(LONG_CLICK, 300);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (isDown) {
                            onTouchLinstener.onClick(view);
                            isDown = false;
                        }
                        handler.removeMessages(LONG_CLICK);
                        break;
                }
                return false;
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LONG_CLICK:
                    if (count++ < 30) {
                        if (null != onTouchLinstener) {
                            onTouchLinstener.onClick(view);
                        }
                        handler.sendEmptyMessageDelayed(LONG_CLICK, 50);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public interface MyOnTouchLinstener {
        void onClick(View v);
    }
}
