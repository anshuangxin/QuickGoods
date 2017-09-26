package com.gly.quickgoods.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.gly.quickgoods.basees.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/22.
 */

public class TryActivity extends BaseActivity {

    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_company_name)
    EditText edCompanyName;
    @BindView(R.id.ed_mail)
    EditText edMail;
    @BindView(R.id.ed_other)
    EditText edOther;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        commit();
    }

    private void commit() {

    }
}
