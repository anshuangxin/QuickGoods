package com.bjyzqs.kuaihuo_yunshouyin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseFragment;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.EdViewSubtractionDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;
import com.bjyzqs.kuaihuo_yunshouyin.views.MessageDialog;
import com.bjyzqs.kuaihuo_yunshouyin.views.PassKeyBoard;

import butterknife.BindView;
import butterknife.OnClick;
import com.bjyzqs.kuaihuo_yunshouyin.R;

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
