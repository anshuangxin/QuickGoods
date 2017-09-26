package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gly.quickgoods.application.MyApplication;
import com.gly.quickgoods.basees.BaseFragment;
import com.gly.quickgoods.dao.ConnectDao;
import com.gly.quickgoods.utils.Logger;
import com.gly.quickgoods.utils.PayDialog;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.views.PassKeyBoard;

import butterknife.BindView;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/13.
 */

public class WeiXinFragment extends BaseFragment {


    @BindView(R.id.ed_yingshou)
    EditText edYingshou;
    @BindView(R.id.ed_shouquanma)
    EditText edShouquanma;
    @BindView(R.id.ed_phonenum)
    EditText edPhonenum;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.passkeyboard)
    PassKeyBoard passkeyboard;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_weixin, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edYingshou.setInputType(0);
        edYingshou.requestFocus();
        edPhonenum.setInputType(0);
        edShouquanma.setInputType(0);
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
        ConnectDao.Ajaxpay("1","3654",edYingshou.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId + "", new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
                Logger.log(responseObj);
                if(!responseObj.contains("-1")){
                    PayDialog.ShowDialog(mContext, false);
                }else {
                    PayDialog.ShowDialog(mContext, true);
                }
            }
            @Override
            public void onFailure(Object reasonObj) {

            }
        });

    }

}
