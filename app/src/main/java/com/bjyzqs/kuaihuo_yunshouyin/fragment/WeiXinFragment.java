package com.bjyzqs.kuaihuo_yunshouyin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseFragment;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Logger;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;
import com.bjyzqs.kuaihuo_yunshouyin.views.MessageDialog;
import com.bjyzqs.kuaihuo_yunshouyin.views.PassKeyBoard;
import com.bjyzqs.kuaihuo_yunshouyin.views.SquarImageButton;
import com.zbar.lib.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bjyzqs.kuaihuo_yunshouyin.R.id.ed_shouquanma;
import static com.zbar.lib.CaptureActivity.ACTION_DECODE_INTENT;
import static com.zbar.lib.CaptureActivity.KEY_DECODE_RESULT;

/**
 * Created by gly on 2017/9/13.
 */

public class WeiXinFragment extends BaseFragment {


    @BindView(R.id.ed_yingshou)
    EditText edYingshou;
    @BindView(R.id.btn_saoma)
    SquarImageButton btnSaoma;
    @BindView(ed_shouquanma)
    EditText edShouquanma;
    @BindView(R.id.ed_phonenum)
    EditText edPhonenum;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.passkeyboard)
    PassKeyBoard passkeyboard;
    private ShouQuanReciver shouQuanReciver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_weixin, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        edYingshou.setInputType(0);
        edYingshou.requestFocus();
        edPhonenum.setInputType(0);
        edShouquanma.setInputType(0);
        edShouquanma.setEnabled(false);
        btnSaoma.setEnabled(false);
        edYingshou.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    edShouquanma.setEnabled(false);
                    btnSaoma.setEnabled(false);
                } else {
                    edShouquanma.setEnabled(true);
                    btnSaoma.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edShouquanma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkShouquanMa();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        shouQuanReciver = new ShouQuanReciver();
        localBroadcastManager.registerReceiver(shouQuanReciver, new IntentFilter(ACTION_DECODE_INTENT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(shouQuanReciver);
    }

    private class ShouQuanReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(KEY_DECODE_RESULT);
            edShouquanma.setText(result);
            if (null != CaptureActivity.activity) {
                CaptureActivity.activity.finish();
            }
        }
    }

    private void checkShouquanMa() {
        String s = edShouquanma.getText().toString();
        if (TextUtils.isEmpty(s) || s.length() != 18 || !(s.startsWith("28") || s.startsWith("13"))) {
            btnSure.setEnabled(false);
            return;
        }
        if (edYingshou.getText().length() == 0) {
            btnSure.setEnabled(false);
            return;
        }
        btnSure.setEnabled(true);
        String num = "";
        if (s.startsWith("13")) {
            num = "1";
        } else if (s.startsWith("28")) {
            num = "5";
        }
        ConnectDao.Ajaxpay(num, s, edYingshou.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId + "", new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
                Logger.log(responseObj);
                if (responseObj.contains("-1")) {
                    edPhonenum.setText("");
                    edYingshou.setText("");
                    new MessageDialog(mContext).isSuccess(false).title("").delayTime(2000).message("授权码错误或者失效，请重新扫描").show();
                } else {
                    SpeechDao.receive(edYingshou.getText().toString());
                    new MessageDialog(getContext()).isSuccess(true).message("付款成功").show();
                }
                edShouquanma.setFocusable(true);
                edShouquanma.setFocusableInTouchMode(true);
                edShouquanma.requestFocus();
            }

            @Override
            public void onFailure(Object reasonObj) {
                edShouquanma.setFocusable(true);
                edShouquanma.setFocusableInTouchMode(true);
                edShouquanma.requestFocus();
            }
        });
        edShouquanma.setText("");

    }

    @OnClick({R.id.btn_saoma, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_saoma:
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.btn_sure:
                checkShouquanMa();
                break;
        }
    }
}
