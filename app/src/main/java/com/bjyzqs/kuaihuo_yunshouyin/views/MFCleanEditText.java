package com.bjyzqs.kuaihuo_yunshouyin.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class MFCleanEditText extends EditText implements TextWatcher, View.OnFocusChangeListener {

    private Drawable mRightDrawable;//保存 EditText右侧的删除按钮
    private boolean hasFoucs;  //保存控件是否获取到焦点
    private OnClickListener mListener;//自定义点击监听器
    private boolean isFirst = true;//判断是否是第一次加载

    public MFCleanEditText(Context context) {
        this(context, null);
    }

    public MFCleanEditText(Context context, AttributeSet attrs) {
        //必须使用android.R.attr.editTextStyle，否则失去本有的功能
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public MFCleanEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();//用于设置
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mRightDrawable = getCompoundDrawables()[2]; //保存 右侧按钮
        // 默认设置隐藏图标
        setRightIconVisible(true);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
        int density = (int) getResources().getDisplayMetrics().density;
        int padding = (int) (density*3);
        setPadding(density*10,padding,padding,padding);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClickLeft(Object obj) {

            }

            @Override
            public void onClickRight(Object obj) {

            }
        });
    }

    /**
     * @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     * getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     * distance 删除图标顶部边缘到控件顶部边缘的距离
     * distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (getCompoundDrawables()[2] != null) { //清除按钮存在时
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height(); //按钮高
                int distance = (getHeight() - height) / 2; //按钮距离上边缘（下边缘）的距离
                boolean isInnerWidth =
                        x > (getWidth() - getTotalPaddingRight()) &&
                                x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    if (null != mListener) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            setPressed(false);
                        } else {
                            setPressed(true);
                        }
                        mListener.onClickRight(getCompoundDrawables()[2]);
                        return true;
                    }
                }
            }
            if (getCompoundDrawables()[0] != null) {//左侧按钮存在
                Rect rect = getCompoundDrawables()[0].getBounds();
                int height = rect.height(); //按钮高
                int distance = (getHeight() - height) / 2;  //按钮距离上边缘（下边缘）的距离
                boolean isInnerWidth =
                        x < getTotalPaddingLeft() &&
                                x > getPaddingLeft();
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    //TODO 左侧按钮被点击
                    if (mListener != null) {//自定义点击监听器
                        mListener.onClickLeft(getCompoundDrawables()[0]);
                        return true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start,
                              int count, int after) {
        if (hasFoucs) {
            setRightIconVisible(s.length() > 0);
        }
    }

    /**
     * 当MFCleanEditText焦点发生变化的时候，
     * 输入长度为零或没有焦点时，隐藏删除图标；否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setRightIconVisible(getText().length() > 0);
        } else {
            setRightIconVisible(false);
        }
    }

    protected void setRightIconVisible(boolean visible) {
        Drawable right = visible ? mRightDrawable : mRightDrawable;
        setCompoundDrawables(
                getCompoundDrawables()[0],
                getCompoundDrawables()[1],
                right,
                getCompoundDrawables()[3]);
    }

    public interface OnClickListener {
        void onClickLeft(Object obj);

        void onClickRight(Object obj);
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFirst) {//只执行一次
            setDrawableBound();
            isFirst = false;
        }
        super.onDraw(canvas);
    }

    /**
     * 重新设置DrawableLeft、DrawableTop、DrawableRight、DrawableBottom的布局
     */
    private void setDrawableBound() {
        int padding = getPaddingRight();
        int rectBound = getHeight() - 2 * padding;
        Rect rect = new Rect();
        if (mRightDrawable != null) {
            rectBound = Math.min(getHeight() - 2 * padding, mRightDrawable.getIntrinsicWidth());
            rect.set(0, 0, rectBound, rectBound);
            mRightDrawable.setBounds(rect); //右
        }
        Drawable left = getCompoundDrawables()[0];
        Drawable top = getCompoundDrawables()[1];
        Drawable bottom = getCompoundDrawables()[3];
        if (left != null) {
            rectBound = Math.min(getHeight() - 2 * padding, left.getIntrinsicWidth());
            rect.set(0, 0, rectBound, rectBound);
            left.setBounds(rect);
        }
        if (top != null) {
            rectBound = Math.min(getHeight() - 2 * padding, top.getIntrinsicWidth());
            rect.set(0, 0, rectBound, rectBound);
            left.setBounds(rect);
        }
        if (bottom != null) {
            rectBound = Math.min(getHeight() - 2 * padding, bottom.getIntrinsicWidth());
            rect.set(0, 0, rectBound, rectBound);
            left.setBounds(rect);
        }
        setRightIconVisible(getText().length() > 0);
    }

}