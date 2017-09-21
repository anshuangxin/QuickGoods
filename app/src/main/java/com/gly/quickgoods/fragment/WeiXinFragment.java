package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gly.quickgoods.basees.BaseFragment;
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

    }

}
