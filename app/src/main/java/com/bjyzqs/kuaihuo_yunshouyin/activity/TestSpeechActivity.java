package com.bjyzqs.kuaihuo_yunshouyin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseActivity;
import com.bjyzqs.kuaihuo_yunshouyin.constants.SpeechConstants;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bjyzqs.kuaihuo_yunshouyin.R;

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
        SpeechDao.newOutOrder(SpeechConstants.BAIDU_DINGDAN, editText.getText().toString());
    }
}
