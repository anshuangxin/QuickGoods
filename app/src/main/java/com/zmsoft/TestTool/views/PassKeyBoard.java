package com.zmsoft.TestTool.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.dao.LongClickDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gly on 2017/9/12.
 */

public class PassKeyBoard extends FrameLayout {
    public onKeyClickLinstener onKeyClickLinstener;
    @BindView(R.id.back)
    ImageButton back;

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
        ButterKnife.bind(this, view);
        LongClickDao.getInstance().checkLongClick(back, new LongClickDao.MyOnTouchLinstener() {
            @Override
            public void onClick(View v) {
                keyClock("-1", getFocusView());
            }
        });
    }

    public void setOnKeyClickLinstener(PassKeyBoard.onKeyClickLinstener onKeyClickLinstener) {
        this.onKeyClickLinstener = onKeyClickLinstener;
    }

    @OnClick({R.id.button4, R.id.button1, R.id.button6, R.id.button5, R.id.button0, R.id.button7, R.id.button8, R.id.button9, R.id.button2, R.id.button3, R.id.button_point})
    public void onViewClicked(View view) {
        String i = "-1";
        switch (view.getId()) {
            case R.id.button4:
                i = "4";
                break;
            case R.id.button_point:
                i = ".";
                break;
            case R.id.button1:
                i = "1";
                break;
            case R.id.button6:
                i = "6";
                break;
            case R.id.button5:
                i = "5";
                break;
            case R.id.button0:
                i = "0";
                break;
            case R.id.button7:
                i = "7";
                break;
            case R.id.button8:
                i = "8";
                break;
            case R.id.button9:
                i = "9";
                break;
            case R.id.button2:
                i = "2";
                break;
            case R.id.button3:
                i = "3";
                break;
        }
        keyClock(i, getFocusView());
    }

    public View getFocusView() {
        if (null == onKeyClickLinstener) {
            Activity context = (Activity) getContext();
            return context.getCurrentFocus();

        } else {
            return onKeyClickLinstener.getFocuseView();
        }
    }


    private void keyClock(String i, View v) {
        if (v instanceof EditText) {
            EditText e = (EditText) v;
            if ("-1".equals(i)) {
                int index = e.getSelectionStart();
                if (index > 0) {
                    e.getEditableText().delete(index - 1, index);
                }
            } else {
                if (i.equals(".") && (e.getText().toString().contains(".") || e.getText().toString().length() == 0)) {
                    return;
                } else {
                    e.getEditableText().append(i + "");
                }
            }
        }
    }

    public interface onKeyClickLinstener {
        View getFocuseView();
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
