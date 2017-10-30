package com.zmsoft.TestTool.fragment;

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

import com.zbar.lib.CaptureActivity;
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;
import com.zmsoft.TestTool.dao.ActivityStarter;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.dao.SpeechDao;
import com.zmsoft.TestTool.modle.CheckInfo;
import com.zmsoft.TestTool.utils.MyTextUtils;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.MessageDialog;
import com.zmsoft.TestTool.views.PassKeyBoard;
import com.zmsoft.TestTool.views.SquarImageButton;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zbar.lib.CaptureActivity.ACTION_DECODE_INTENT;
import static com.zbar.lib.CaptureActivity.KEY_DECODE_RESULT;
import static com.zmsoft.TestTool.R.id.ed_shouquanma;

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
        if (null != localBroadcastManager && null != shouQuanReciver) {
            localBroadcastManager.unregisterReceiver(shouQuanReciver);
        }
    }

    private class ShouQuanReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DECODE_INTENT)) {
                String result = intent.getStringExtra(KEY_DECODE_RESULT);
                edShouquanma.setText(result);
                if (null != CaptureActivity.activity) {
                    CaptureActivity.activity.finish();
                }
            }
        }
    }

    private int num;

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

        if (s.startsWith("13")) {
            num = 1;
        } else if (s.startsWith("28")) {
            num = 5;
        }
        ConnectDao.Ajaxpay(num + "", s, edYingshou.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId + "", new DisposeDataListener<CheckInfo>() {
            @Override
            public void onSuccess(CheckInfo responseObj) {
                edPhonenum.setText("");
                edYingshou.setText("");
                edShouquanma.setText("");
                SpeechDao.speechByPosition(responseObj.type);
                if (responseObj.err == -1) {
                    new MessageDialog(mContext).isSuccess(false).title("").delayTime(2000).message("授权码错误或者失效，请重新扫描").show();
                } else {
                    new MessageDialog(getContext()).isSuccess(true).message("付款成功").show();
                }
                MyTextUtils.reSetEdit(edYingshou);
            }

            @Override
            public void onFailure(Object reasonObj) {
                edPhonenum.setText("");
                edShouquanma.setText("");
                MyTextUtils.reSetEdit(edYingshou);
            }
        });
        edShouquanma.setText("");

    }

    @OnClick({R.id.btn_saoma, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_saoma:
                ActivityStarter.startCapActivity(mContext);
                break;
            case R.id.btn_sure:
                checkShouquanMa();
                break;
        }
    }
}
