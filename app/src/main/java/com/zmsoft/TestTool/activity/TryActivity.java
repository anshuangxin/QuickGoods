package com.zmsoft.TestTool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.zmsoft.TestTool.basees.BaseActivity;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.utils.MyTextUtils;
import com.zmsoft.TestTool.utils.ToastUtil;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.MessageDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zmsoft.TestTool.R;

import static com.zmsoft.TestTool.activity.LoginActivity.KEY_MSG_DATA;
import static com.zmsoft.TestTool.activity.LoginActivity.TRY_REQUEST_CODE;

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
        if (checkEdit()) {
            return;
        }
        ConnectDao.SendMail(edOther.getText().toString(), edCompanyName.getText().toString(), edPhone.getText().toString(), edMail.getText().toString(), edName.getText().toString(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                if (!responseObj.equals("-1")) {
                    new MessageDialog(mContext).isSuccess(true).message("提交成功").listener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Intent intent = new Intent();
                            intent.putExtra(KEY_MSG_DATA, "已申请");
                            TryActivity.this.setResult(TRY_REQUEST_CODE, intent);
                            TryActivity.this.finish();
                        }
                    }).show();
                } else {
                    new MessageDialog(mContext).isSuccess(false).message("提交失败").show();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private boolean checkEdit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isCheck = false;
        if (!MyTextUtils.isName(edName.getText().toString())) {
            stringBuilder.append(" 姓名不正确 ");
            isCheck = true;
        }
        if (!MyTextUtils.isMobileNO(edPhone.getText().toString())) {
            stringBuilder.append(" 电话不正确 ");
            isCheck = true;
        }
        if (!MyTextUtils.isName(edCompanyName.getText().toString())) {
            stringBuilder.append(" 企业名称不正确 ");
            isCheck = true;
        }
        if (isCheck) {
            ToastUtil.showToast(stringBuilder.toString());
        }
        return isCheck;
    }
}
