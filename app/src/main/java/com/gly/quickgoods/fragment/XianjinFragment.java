package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gly.quickgoods.application.MyApplication;
import com.gly.quickgoods.basees.BaseFragment;
import com.gly.quickgoods.dao.ConnectDao;
import com.gly.quickgoods.dao.EdViewSubtractionDao;
import com.gly.quickgoods.utils.Logger;
import com.gly.quickgoods.utils.PayDialog;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.views.PassKeyBoard;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import gly.quickgoods.R;

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
    Unbinder unbinder;
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
        new EdViewSubtractionDao().subtraction(edKefu, edYingshou, edZhaoling);
        edKefu.setInputType(0);
        edKefu.requestFocus();
        edPhonenum.setInputType(0);
        edYingshou.setInputType(0);
        passkeyboard.setOnKeyClickLinstener(new PassKeyBoard.onKeyClickLinstener() {
            @Override
            public void onKeyClock(int i) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus instanceof EditText) {
                    EditText e = (EditText) currentFocus;
                    if (i == -1) {
                        int index = e.getSelectionStart();
                        if (index > 0) {
                            e.getEditableText().delete(index - 1, index);
                        }
                    } else {
                        e.getEditableText().append(i + "");
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        ConnectDao.Ajaxcash(edYingshou.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId + "", new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
                Logger.log(responseObj.toString());
                if (!responseObj.contains("-1")) {
                    PayDialog.ShowDialog(mContext, false);
                } else {
                    PayDialog.ShowDialog(mContext, true);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
