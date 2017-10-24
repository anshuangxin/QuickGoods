package com.zmsoft.TestTool.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
@SuppressLint("AppCompatCustomView")
public class SquarImageButton extends ImageButton {
    public SquarImageButton(Context context) {
        this(context, null);
    }

    public SquarImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquarImageButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childHeightSize = getMeasuredHeight();
        int childWidthSize = childHeightSize;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
