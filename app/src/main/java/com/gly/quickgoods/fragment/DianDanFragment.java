package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gly.quickgoods.basees.BaseFragment;
import com.gly.quickgoods.modle.GoodSInfo;
import com.gly.quickgoods.request.ConnectDao;
import com.gly.quickgoods.utils.Logger;
import com.gly.quickgoods.utils.ToastUtil;
import com.gly.quickgoods.utils.baseListadapter.CommonAdapter;
import com.gly.quickgoods.utils.baseListadapter.ViewHolder;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/13.
 */

public class DianDanFragment extends BaseFragment {
    @BindView(R.id.rb_top)
    RadioButton rbTop;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    private GridView gridView;
    private boolean isFirstIn = true;
    private List<GoodSInfo.GoodsInfoBean> datas = new ArrayList<>();
    private CommonAdapter<GoodSInfo.GoodsInfoBean> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_diandan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();

    }

    private void getData() {
        ConnectDao.getPossess("10827", new DisposeDataListener<GoodSInfo>() {
            @Override
            public void onSuccess(GoodSInfo goodSInfo) {
                datas.clear();
                datas.addAll(goodSInfo.goodsInfo);
                adapter.notifyDataSetChanged();
                Logger.log("responseObj: " + goodSInfo.toString());
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onFailure(Object reasonObj) {
                Logger.log("responseObj: " + reasonObj.toString());
            }
        });
    }

    private void initView() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_top:

                        break;
                    case R.id.rb_recai:
                        break;
                    case R.id.rb_liangcai:
                        break;
                    case R.id.rb_zhushi:
                        break;
                    case R.id.rb_jiushui:
                        break;
                }
            }
        });
        rbTop.performClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 第一次进入自动刷新
        if (isFirstIn) {
            refreshView.autoRefresh();
            getData();
            isFirstIn = false;
        }
    }


    /**
     * ListView初始化方法
     */
    private void initListView() {
        refreshView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData();
            }
        });
        gridView = (GridView) refreshView.getPullableView();
        adapter = new CommonAdapter<GoodSInfo.GoodsInfoBean>(mContext, datas, R.layout.tiaoma_list_item) {
            @Override
            public void convert(ViewHolder helper, GoodSInfo.GoodsInfoBean item) {
                helper.getConvertView().getLayoutParams().width = gridView.getMeasuredWidth() / gridView.getNumColumns();
                helper.getConvertView().getLayoutParams().height = (int) (helper.getConvertView().getLayoutParams().width * 1.533f);
                helper.setText(R.id.tv_name, item.goods_name);
                helper.setText(R.id.tv_code, "商品编号:" + item.sku_id);
                helper.setText(R.id.tv_hava_count, "库存数量:" + item.goods_num);
                helper.setText(R.id.tv_price, item.price);
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(
                        mContext,
                        "LongClick on "
                                + parent.getAdapter().getItemId(position),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ToastUtil.showToast(mContext,
                        " Click on " + parent.getAdapter().getItemId(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 下拉刷新与上拉加载更多监听器
     */
    public class MyPullListener implements PullToRefreshLayout.OnPullListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            getData();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 加载更多操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件加载完毕了哦！
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 1000);
        }
    }
}
