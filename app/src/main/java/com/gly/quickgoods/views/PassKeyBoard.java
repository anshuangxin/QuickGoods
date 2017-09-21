package com.gly.quickgoods.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/12.
 */

public class PassKeyBoard extends FrameLayout {
    public onKeyClickLinstener onKeyClickLinstener;

    public PassKeyBoard(Context context) {
        this(context, null);
    }

    public PassKeyBoard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassKeyBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LinearLayout.inflate(context, R.layout.view_passkeyboard, null);
        addView(view);
        ButterKnife.bind(this,view);
    }

    public void setOnKeyClickLinstener(PassKeyBoard.onKeyClickLinstener onKeyClickLinstener) {
        this.onKeyClickLinstener = onKeyClickLinstener;
    }


    @OnClick({R.id.button4, R.id.button1, R.id.button6, R.id.button5, R.id.back, R.id.button0, R.id.button7, R.id.button8, R.id.button9, R.id.button2, R.id.button3})
    public void onViewClicked(View view) {
        if (null != onKeyClickLinstener) {
            int i = -1;
            switch (view.getId()) {
                case R.id.button4:
                    i = 4;
                    break;
                case R.id.button1:
                    i = 1;
                    break;
                case R.id.button6:
                    i = 6;
                    break;
                case R.id.button5:
                    i = 5;
                    break;
                case R.id.back:
                    i = -1;
                    break;
                case R.id.button0:
                    i = 0;
                    break;
                case R.id.button7:
                    i = 7;
                    break;
                case R.id.button8:
                    i = 8;
                    break;
                case R.id.button9:
                    i = 9;
                    break;
                case R.id.button2:
                    i = 2;
                    break;
                case R.id.button3:
                    i = 3;
                    break;
            }
            onKeyClickLinstener.onKeyClock(i);
        }
    }

    public interface onKeyClickLinstener {
        void onKeyClock(int i);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childHeightSize = getMeasuredHeight();
        int childWidthSize = (int) (childHeightSize / 1.237f);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
