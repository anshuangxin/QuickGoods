package com.gly.quickgoods.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.gly.quickgoods.basees.BaseActivity;
import com.gly.quickgoods.dao.SpeechDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/22.
 */

public class TestSpeechActivity extends BaseActivity {


    @BindView(R.id.editText)
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button10)
    public void onViewClicked() {
        SpeechDao.CustomRead1(mContext, editText.getText().toString());
    }
}
