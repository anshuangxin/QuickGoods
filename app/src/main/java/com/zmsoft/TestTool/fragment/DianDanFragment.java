package com.zmsoft.TestTool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;
import com.zmsoft.TestTool.constants.HttpConstants;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.modle.GoodSInfo;
import com.zmsoft.TestTool.modle.IfyInfo;
import com.zmsoft.TestTool.modle.TbSelecterInfo;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.baseListadapter.CommonAdapter;
import com.zmsoft.TestTool.utils.baseListadapter.ViewHolder;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.DingDanView;
import com.zmsoft.TestTool.views.TbSelector;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.zmsoft.TestTool.R;

/**
 * Created by gly on 2017/9/13.
 */

public class DianDanFragment extends BaseFragment {
    @BindView(R.id.tbselector)
    TbSelector tbselector;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @BindView(R.id.dingdanview)
    DingDanView dingdanview;
    private GridView gridView;
    private boolean isFirstIn = true;
    private List<GoodSInfo.GoodsInfoBean> datas;

    private CommonAdapter<GoodSInfo.GoodsInfoBean> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_diandan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == datas) {
            initListView();
            initView();
        }
    }

    private void getData() {
        ConnectDao.getPossess(MyApplication.userId, new DisposeDataListener<GoodSInfo>() {
            @Override
            public void onSuccess(GoodSInfo goodSInfo) {
                initTitle(goodSInfo.oneLevel);
                datas.clear();
                datas.addAll(goodSInfo.goodsInfo);
                adapter.notifyDataSetChanged();
                if (null != refreshView) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Logger.log("responseObj: " + reasonObj.toString());
            }
        });
    }

    private void initTitle(List<GoodSInfo.OneLevelBean> oneLevel2) {
        this.oneLevel.clear();
        this.oneLevel.add(new GoodSInfo.OneLevelBean("全部商品排行"));
        this.oneLevel.addAll(oneLevel2);
        List<TbSelecterInfo> infos = new ArrayList<>();
        for (GoodSInfo.OneLevelBean bean : oneLevel) {
            infos.add(new TbSelecterInfo(bean.cat_name, 0, 0, R.drawable.oval_selector));
        }
        tbselector.inItTabS(infos);
        tbselector.setOnTbSelectListener(new TbSelector.onTbSelectListener() {
            @Override
            public void onSelectChange(int position) {
                if (TextUtils.isEmpty(oneLevel.get(position).id)) {
                    getData();
                } else {
                    ConnectDao.ifyClass(oneLevel.get(position).id, MyApplication.userId, new DisposeDataListener<IfyInfo>() {
                        @Override
                        public void onSuccess(IfyInfo responseObj) {
                            datas.clear();
                            datas.addAll(responseObj.data);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
            }
        });

//        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                Logger.log("id" + i);
//                switch (i) {
//                    case R.id.rb_top:
//
//                        break;
//                    case R.id.rb_recai:
//                        break;
//                    case R.id.rb_liangcai:
//                        break;
//                    case R.id.rb_zhushi:
//                        break;
//                    case R.id.rb_jiushui:
//                        break;
//                }
//            }
//        });
    }

    private void initView() {

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
        datas = new ArrayList<>();
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
                helper.setText(R.id.tv_code, "商品编号: " + item.sku_id);
                helper.setText(R.id.tv_hava_count, "库存数量: " + item.goods_num);
                helper.setText(R.id.tv_price, String.valueOf(item.price));
                if (!TextUtils.isEmpty(item.pic)) {
                    ImageView view = helper.getView(R.id.img_title);
                    Glide.with(DianDanFragment.this)
                            .load(HttpConstants.ROOT_URL + item.pic)
                            .into(view);
                }
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dingdanview.addGoods(datas.get(position));
            }
        });
    }

}
