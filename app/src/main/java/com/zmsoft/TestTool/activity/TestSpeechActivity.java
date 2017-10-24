package com.zmsoft.TestTool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.basees.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    }
}
