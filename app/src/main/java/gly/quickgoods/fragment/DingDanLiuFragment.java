package gly.quickgoods.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gly.quickgoods.R;
import gly.quickgoods.basees.BaseFragment;
import gly.quickgoods.utils.baserecycleadapter.CommonAdapter;
import gly.quickgoods.utils.baserecycleadapter.base.ViewHolder;
import gly.quickgoods.views.CircularImageView;

/**
 * Created by gly on 2017/9/19.
 */

public class DingDanLiuFragment extends BaseFragment {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private List<String> datas;
    private CommonAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_dingdanliu, container);
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
            recycleview.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();

    }
}
