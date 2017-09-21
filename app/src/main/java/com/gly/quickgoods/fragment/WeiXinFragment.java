package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gly.quickgoods.basees.BaseFragment;

import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/13.
 */

public class WeiXinFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_weixin, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {

    }
}
