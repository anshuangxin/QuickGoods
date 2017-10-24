package com.zmsoft.TestTool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.basees.BaseFragment;

/**
 * Created by gly on 2017/9/13.
 */

public class ShouKuanFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_shoukuan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
