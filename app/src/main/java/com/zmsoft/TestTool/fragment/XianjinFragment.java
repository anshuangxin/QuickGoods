package com.zmsoft.TestTool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.dao.EdViewSubtractionDao;
import com.zmsoft.TestTool.dao.SpeechDao;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.MessageDialog;
import com.zmsoft.TestTool.views.PassKeyBoard;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gly on 2017/9/13.
 */

public class XianjinFragment extends BaseFragment {

    @BindView(R.id.ed_kefu)
    EditText edKefu;
    @BindView(R.id.ed_yingshou)
    EditText edYingshou;
    @BindView(R.id.ed_phonenum)
    EditText edPhonenum;
    @BindView(R.id.ed_zhaoling)
    TextView edZhaoling;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.passkeyboard)
    PassKeyBoard passkeyboard;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_xianjin, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        new EdViewSubtractionDao().subtraction(edKefu, edYingshou, edZhaoling,btnSure);
        edKefu.setInputType(0);
        edYingshou.requestFocus();
        edPhonenum.setInputType(0);
        edYingshou.setInputType(0);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        ConnectDao.Ajaxcash(edYingshou.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId, new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
                if (responseObj.equals("1")) {
                    SpeechDao.receive(edYingshou.getText().toString());
                    new MessageDialog(getContext()).isSuccess(true).message("付款成功").show();
                } else {
                    new MessageDialog(getContext()).isSuccess(false).message("付款失败").show();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
