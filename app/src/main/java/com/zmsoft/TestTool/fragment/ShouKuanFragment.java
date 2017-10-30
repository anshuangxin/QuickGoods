package com.zmsoft.TestTool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;

import butterknife.BindView;

/**
 * Created by gly on 2017/9/13.
 */

public class ShouKuanFragment extends BaseFragment {
    @BindView(R.id.imageview1)
    ImageView imageview1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_shoukuan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(ShouKuanFragment.this)
                .load("http://www.kh95.com/m.php/juhepay/index?uid=" + MyApplication.userId)
                .into(imageview1);
    }
}
