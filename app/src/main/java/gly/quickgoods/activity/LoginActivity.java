package gly.quickgoods.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gly.quickgoods.R;
import gly.quickgoods.application.MyApplication;
import gly.quickgoods.basees.BaseActivity;
import gly.quickgoods.request.ConnectDao;
import gly.quickgoods.utils.Logger;
import gly.quickgoods.utils.MyTextUtils;
import gly.quickgoods.utils.ToastUtil;
import gly.quickgoods.utils.okhttp.listener.DisposeDataListener;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.imageview1)
    ImageView imageview1;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(R.id.ed_pwd)
    EditText edPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tv_err)
    TextView tvErr;
    @BindView(R.id.tv_regist)
    TextView tvRegist;
    @BindView(R.id.code_layout)
    LinearLayout codeLayout;
    private String code;
    private static final int COUNT_DOWN = 899;
    private static final int START_COUNT = 60;
    private static int count = START_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (MyTextUtils.isMobileNO(charSequence.toString())) {
                    codeLayout.setVisibility(View.VISIBLE);
                } else {
                    codeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        passkeyboard.setOnKeyClickLinstener(new PassKeyBoard.onKeyClickLinstener() {
//            @Override
//            public void onKeyClock(int i) {
//                EditText v = null;
//                if (edPhone.isFocused()) {
//                    v = edPhone;
//
//                } else if (edCode.isFocused()) {
//                    v = edCode;
//
//                } else if (edPwd.isFocused()) {
//                    v = edPwd;
//                }
//
//                int index = v.getSelectionStart();
//                Editable editable = v.getText();
//
//                if (i == -1) {
//                    if (index > 0) {
//                        editable.delete(index - 1, index);
//                    }
//                } else {
//                    editable.insert(index, "" + i);
//                }
//            }
//        });
    }


    @OnClick({R.id.tv_getcode, R.id.btn_login, R.id.tv_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                getCode();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_regist:
                break;
        }
    }

    private void login() {
//        telephone String 是门店名称或手机号
//        captch String 是验证码当telephone 为手机号时必填
//        passwords String 是密码必填
        final String code = edCode.getText().toString();
        String phone = edPhone.getText().toString();
        String pwd = edPwd.getText().toString();
//        if (TextUtils.isEmpty(code)) {
//            ToastUtil.showToast(mContext, "验证码为空!", 2000).show();
//            return;
//        }
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtil.showToast(mContext, "手机号为空!", 2000).show();
//            return;
//        }
//        if (TextUtils.isEmpty(pwd)) {
//            ToastUtil.showToast(mContext, "密码为空!", 2000).show();
//            return;
//        }
//        if (!MyTextUtils.isNum(code)) {
//            ToastUtil.showToast(mContext, "验证码错误!", 2000).show();
//            return;
//        }
        ConnectDao.Login("13823579661", "9566", "987654321", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                Logger.log("responseObj: " + responseObj.toString());
                JSONObject jsonObject = JSON.parseObject(responseObj.toString());
                String err = jsonObject.getString("err");
                String message = jsonObject.getString("message");
                if (err.equals("200")) {
                    MyApplication.getInstance().is_one = jsonObject.getString("is_one");
                    startActivity(new Intent(mContext, MainActivity.class));
                } else if ("false".equals(err)) {
                    ToastUtil.showToast(mContext, message, 2000).show();
                }
                if (!TextUtils.isEmpty(message)) {
                    tvErr.setText(message);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    public void getCode() {
        String phone = edPhone.getText().toString();
        if (!MyTextUtils.isMobileNO(phone)) {
            ToastUtil.showToast(mContext, "不是手机号!", 2000).show();
            return;
        }
        count = START_COUNT;
        handler.sendEmptyMessage(COUNT_DOWN);
        ConnectDao.getVerCode(phone, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Logger.log("responseObj: " + responseObj.toString());
                JSONObject jsonObject = JSON.parseObject(responseObj.toString());
                String code1 = jsonObject.getString("code");
                String err = jsonObject.getString("err");
                if (!TextUtils.isEmpty(err) && "200".equals(err)) {
                    ToastUtil.showToast(mContext, "请求成功!", 2000).show();
                    if (!TextUtils.isEmpty(code1)) {
                        code = code1;
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                ToastUtil.showToast(mContext, "请求失败!", 2000).show();
                Logger.log(reasonObj.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(COUNT_DOWN);
        super.onDestroy();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT_DOWN:
                    if (count > 1) {
                        tvGetcode.setEnabled(false);
                        tvGetcode.setText(--count + "秒后重新获取验证码");
                        handler.sendEmptyMessageDelayed(COUNT_DOWN, 1000);
                    } else {
                        tvGetcode.setEnabled(true);
                        tvGetcode.setText("获取验证码");
                    }

                    break;
                default:
                    break;
            }
        }
    };
}
