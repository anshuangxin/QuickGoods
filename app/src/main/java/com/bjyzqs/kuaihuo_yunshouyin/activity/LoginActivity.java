package com.bjyzqs.kuaihuo_yunshouyin.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseActivity;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Logger;
import com.bjyzqs.kuaihuo_yunshouyin.utils.MyTextUtils;
import com.bjyzqs.kuaihuo_yunshouyin.utils.SharedPreferencesUtil;
import com.bjyzqs.kuaihuo_yunshouyin.utils.ToastUtil;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Util;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;
import com.bjyzqs.kuaihuo_yunshouyin.views.MessageDialog;
import com.zbar.lib.CaptureActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bjyzqs.kuaihuo_yunshouyin.R.id.ed_pwd;

public class LoginActivity extends BaseActivity {

    public static final int TRY_REQUEST_CODE = 200;
    public static final String KEY_MSG_DATA = "try_message";
    @BindView(R.id.imageview1)
    ImageView imageview1;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(ed_pwd)
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
        checkIsLogin();
    }

    private void checkIsLogin() {
        String user_id = SharedPreferencesUtil.getString(mContext, "user_id");
        if (!TextUtils.isEmpty(user_id)) {
            MyApplication.userId = "10827";//user_id;
            SpeechDao.open();
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
            boolean key_msg_data = SharedPreferencesUtil.getBoolean(mContext, KEY_MSG_DATA);
            if (key_msg_data) {
                tvRegist.setText("已申请");
            }
            initView();
        }
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
                    edPwd.setVisibility(View.GONE);
                } else {
                    edPwd.setVisibility(View.VISIBLE);
                    codeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 4 && codeLayout.getVisibility() == View.VISIBLE) {
                    login();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                login();
                return false;
            }
        });
//        passkeyboard.setOnKeyClickLinstener(new PassKeyBoard.onKeyClickLinstener() {
//            @Override
//            public void getFocuseView(int i) {
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


    @OnClick({R.id.tv_getcode, R.id.btn_login, R.id.tv_regist, R.id.imageview1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                getCode();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_regist:
                startActivityForResult(new Intent(mContext, TryActivity.class), TRY_REQUEST_CODE);
                break;
            case R.id.imageview1:
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
//                startActivity(new Intent(mContext, TestSpeechActivity.class));
                break;
        }
    }

    private final int REQUEST_CAMERA = 102;//请求照相机权限

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                } else {
                    ToastUtil.showToast(mContext, "无照相机权限，无法使用", Toast.LENGTH_SHORT);
                }
                break;

            default:
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
//            ToastUtil.showToast(mContext, "验证码为空!", 2000);
//            return;
//        }
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtil.showToast(mContext, "手机号为空!", 2000);
//            return;
//        }
//        if (TextUtils.isEmpty(pwd)) {
//            ToastUtil.showToast(mContext, "密码为空!", 2000);
//            return;
//        }
//        if (!MyTextUtils.isNum(code)) {
//            ToastUtil.showToast(mContext, "验证码错误!", 2000);
//            return;
//        }
        ConnectDao.Login(phone, code, pwd, new DisposeDataListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                String err = jsonObject.getString("err");
                String message = jsonObject.getString("message");
                if (err.equals("200")) {
                    SharedPreferencesUtil.putString(mContext, "user_id", message);
                    MyApplication.userId = message;
                    SpeechDao.open();
                    startActivity(new Intent(mContext, MainActivity.class));
                    LoginActivity.this.finish();
                } else if ("false".equals(err)) {
                    ToastUtil.showToast(mContext, message, 2000);
                    if (!TextUtils.isEmpty(message)) {
                        tvErr.setText(String.valueOf(message));
                    }
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
            ToastUtil.showToast(mContext, "不是手机号!", 2000);
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
                    ToastUtil.showToast(mContext, "请求成功!", 2000);
                    if (!TextUtils.isEmpty(code1)) {
                        code = code1;
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                ToastUtil.showToast(mContext, "请求失败!", 2000);
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
                        tvGetcode.setText(String.valueOf(--count) + "秒后重新获取验证码");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TRY_REQUEST_CODE:
                if (null != data) {
                    String returnedData = data.getStringExtra(KEY_MSG_DATA);
                    SharedPreferencesUtil.putBoolean(mContext, KEY_MSG_DATA, true);
                    tvRegist.setText(returnedData);
                }
                break;
            case 0:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    final String scanResult = bundle.getString("result");
                    ToastUtil.showToast(mContext, scanResult, 0);
                    //只能由数字和字母组成匹配
                    Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
                    //邮箱匹配
                    Pattern email = Pattern.compile("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$");
                    Matcher phone_p = p.matcher(scanResult);
                    Matcher email_p = email.matcher(scanResult);
                    //网址匹配
                    Pattern pm = Util.setMatcher();
                    Matcher Web_p = pm.matcher(scanResult);
                    if (phone_p.find() || email_p.find()) {
                    } else if (Web_p.find()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                (Uri.parse(scanResult))
                        ).addCategory(Intent.CATEGORY_BROWSABLE)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        //扫描不到的
                    } else {
                        new MessageDialog(mContext).isSuccess(true).delayTime(100000).title("扫描结果").message(scanResult).show();
                    }
                }
                break;
        }
    }
}
