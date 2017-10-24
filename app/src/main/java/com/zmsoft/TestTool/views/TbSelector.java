package com.zmsoft.TestTool.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zmsoft.TestTool.modle.TbSelecterInfo;

import java.util.ArrayList;
import java.util.List;


public class TbSelector extends HorizontalScrollView {

    private Context mContext;
    private onTbSelectListener titleSelectListener;
    private RadioGroup myRadioGroup;
    private float density;

    public TbSelector(Context context) {
        this(context, null);
    }

    public TbSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TbSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt(context);
    }


    private void inIt(Context context) {
        this.mContext = context;
        density = getResources().getDisplayMetrics().density;
    }

    private List<TbSelecterInfo> titleList = new ArrayList<TbSelecterInfo>();

    public void inItTabS(List<TbSelecterInfo> titleList) {
        this.titleList.clear();
        this.titleList.addAll(titleList);
        removeAllViews();
        initGroup();
    }


    private void initGroup() {
        LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        imgParams.weight = 2;
        textparams.weight = 1;
        myRadioGroup = new RadioGroup(mContext);
        myRadioGroup.setLayoutParams(imgParams);

        // mButton = new Button(mContext);
        // mButton.setClickable(false);
        // mButton.setBackgroundResource(R.drawable.idea2_list_bg);

        // titleLayout.addContentView(mButton);

        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

        int len = titleList.size();
        for (int i = 0; i < len; i++) {
            final CusRadioButton radio = new CusRadioButton(mContext);
            TbSelecterInfo info = titleList.get(i);
            radio.inItDrawables(info);
            // radio.setButtonDrawable(android.R.color.transparent);
            RadioGroup.LayoutParams l = new RadioGroup.LayoutParams(-2, (int) (density * 35),
                    Gravity.CENTER);
            l.weight = 1;
            int margin = (int) (density * 5);
            l.setMargins(margin, 0, margin, 0);
            radio.setLayoutParams(l);
            radio.setPadding((int)(density*20), 0, (int)(density*20), 0);
            radio.setId(i);
            radio.setTag(radio);
            radio.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRadioGroup.clearCheck();
                    radio.setChecked(true);
                    titleSelectListener.onSelectChange(radio.getId());
                }
            });
            if (i == 0) {
                radio.setChecked(true);
            }
            myRadioGroup.addView(radio);
        }
        addView(myRadioGroup);

    }


    public void setCurrItem(int currItem) {
        for (int i = 0; i < myRadioGroup.getChildCount(); i++) {
            CusRadioButton cusRadioButton = (CusRadioButton) myRadioGroup.getChildAt(i);
            cusRadioButton.setChecked(false);
        }
        CusRadioButton childAt = (CusRadioButton) myRadioGroup.getChildAt(currItem);
        childAt.setChecked(true);
    }

    public int getCurrItem() {
        int index = -1;
        for (int i = 0; i < myRadioGroup.getChildCount(); i++) {
            CusRadioButton cusRadioButton = (CusRadioButton) myRadioGroup.getChildAt(i);
            if (cusRadioButton.isChecked()) {
                index = i;
            }
        }
        return index;
    }

    public interface onTbSelectListener {
        void onSelectChange(int position);
    }

    public onTbSelectListener getTitleSelectListener() {
        return titleSelectListener;
    }

    public void setOnTbSelectListener(onTbSelectListener titleSelectListener) {
        this.titleSelectListener = titleSelectListener;
    }

    public LinearLayout getMyRadioGroup() {
        return myRadioGroup;
    }

    public void setMyRadioGroup(RadioGroup myRadioGroup) {
        this.myRadioGroup = myRadioGroup;
    }

    public RadioButton getChildAtPosition(int i) {
        return (RadioButton) myRadioGroup.getChildAt(i);
    }

//    public void setCurrItem(int position) {
//        int len = myRadioGroup.getChildCount();
//        for (int i = 0; i < len; i++) {
//            CusRadioButton childAt = (CusRadioButton) myRadioGroup.getChildAt(i);
//            if (childAt.isChecked()) {
//                childAt.setChecked(false);
//                break;
//            }
//        }
//        CusRadioButton csRadioButton = (CusRadioButton) myRadioGroup.getChildAt(position);
//        csRadioButton.setAlpha(0.1f);
//        csRadioButton.animate().alpha(1f).setDuration(300).start();
//        csRadioButton.setChecked(true);
//    }

}
