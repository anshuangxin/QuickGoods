package com.zmsoft.TestTool.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by gly on 2017/9/22.
 */

@SuppressLint("AppCompatCustomView")
public class ReceiveRadioButton extends RadioButton {
//    private final float density;

    public ReceiveRadioButton(Context context) {
        this(context, null);
    }

    public ReceiveRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReceiveRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        density = getResources().getDisplayMetrics().density;
//        Drawable drawableFirst = getResources().getDrawable(R.drawable.radio_button);
//        int vert = (int) (density * 16);
//        drawableFirst.setBounds(0, 0, vert, vert);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
//        setCompoundDrawables(drawableFirst, null, null, null);
//        setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Logger.log(""+b + "---");
//                Drawable drawableFirst;
//                if (b) {
//                    drawableFirst = getResources().getDrawable(R.drawable.choice_icon01);
//                } else {
//                    drawableFirst = getResources().getDrawable(R.drawable.choice_icon02);
//                }
//                int vert = (int) (density * 16);
//                drawableFirst.setBounds(0, 0, vert, vert);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
//                setCompoundDrawables(drawableFirst, null, null, null);
//            }
//        });
    }
}
