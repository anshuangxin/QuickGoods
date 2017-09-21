package com.gly.quickgoods.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by gly on 2017/9/21.
 */

@SuppressLint("AppCompatCustomView")
public class NoKeyBoardEditText extends EditText {
    public NoKeyBoardEditText(Context context) {
        this(context, null);
    }

    public NoKeyBoardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoKeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setFocusable(true);
            return true;
        }
        return super.onTouchEvent(event);
    }
}