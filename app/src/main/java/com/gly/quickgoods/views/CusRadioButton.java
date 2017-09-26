package com.gly.quickgoods.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import com.gly.quickgoods.modle.TbSelecterInfo;

@SuppressLint({"AppCompatCustomView"})
public class CusRadioButton extends RadioButton {

    public CusRadioButton(Context context) {
        this(context, null);
    }

    public CusRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setTextSize(14);
        setTextColor(Color.BLACK);
    }


    public void inItDrawables(TbSelecterInfo tbSelecterInfo) {
        setBackgroundResource(tbSelecterInfo.getresource());
        setText(tbSelecterInfo.getText());
        invalidate();
    }

}
