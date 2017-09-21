package com.gly.quickgoods.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gly.quickgoods.utils.baserecycleadapter.base.ViewHolder;
import com.gly.quickgoods.views.SpacesItemDecoration;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gly.quickgoods.R;
import com.gly.quickgoods.basees.BaseFragment;
import com.gly.quickgoods.utils.baserecycleadapter.CommonAdapter;
import com.gly.quickgoods.views.CircularImageView;

/**
 * Created by gly on 2017/9/19.
 */

public class DingDanLiuFragment extends BaseFragment {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.swb_auto_print)
    SwitchButton swbAutoPrint;
    Unbinder unbinder;
    private List<String> datas;
    private CommonAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = getView(inflater, R.layout.fragment_dingdanliu, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
    }

    private void initListView() {
        if (null == datas) {
            datas = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                datas.add("" + i);
            }
            recycleview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            adapter = new CommonAdapter<String>(mContext, datas, R.layout.dingdanliu_listitem) {
                @Override
                protected void convert(ViewHolder holder, String o, int position) {
                    CircularImageView view = holder.getView(R.id.circle_view);
                    view.setText("v" + position);
                    view.setTextColor(Color.BLACK);
                    view.setBackgroundColor(getResources().getColor(R.color.btn_bg_normal));
//                holder.getConvertView().getLayoutParams().height =
                }
            };
            SpacesItemDecoration decoration= new SpacesItemDecoration(16);
            recycleview.addItemDecoration(decoration);
            recycleview.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
