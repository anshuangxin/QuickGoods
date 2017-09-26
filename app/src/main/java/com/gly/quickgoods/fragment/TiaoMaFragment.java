package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.gly.quickgoods.application.MyApplication;
import com.gly.quickgoods.basees.BaseFragment;
import com.gly.quickgoods.constants.HttpConstants;
import com.gly.quickgoods.dao.ConnectDao;
import com.gly.quickgoods.modle.GoodSInfo;
import com.gly.quickgoods.utils.baseListadapter.CommonAdapter;
import com.gly.quickgoods.utils.baseListadapter.ViewHolder;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.views.DingDanView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/13.
 */

public class TiaoMaFragment extends BaseFragment {
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.dingdanview)
    DingDanView dingdanview;
    @BindView(R.id.ed_search)
    EditText ed_search;
    private boolean isFirstIn = true;
    private List<GoodSInfo.GoodsInfoBean> datas;
    private CommonAdapter<GoodSInfo.GoodsInfoBean> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_tiaoma, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
    }


    /**
     * ListView初始化方法
     */
    private void initListView() {
        datas = new ArrayList<>();
        adapter = new CommonAdapter<GoodSInfo.GoodsInfoBean>(mContext, datas, R.layout.tiaoma_list_item) {
            @Override
            public void convert(ViewHolder helper, GoodSInfo.GoodsInfoBean item) {
                helper.getConvertView().getLayoutParams().width = gridView.getMeasuredWidth() / gridView.getNumColumns();
                helper.getConvertView().getLayoutParams().height = (int) (helper.getConvertView().getLayoutParams().width * 1.533f);
                if (!TextUtils.isEmpty(item.pic)) {
                    ImageView imageView = helper.getView(R.id.img_title);
                    Glide.with(TiaoMaFragment.this)
                            .load(HttpConstants.ROOT_URL + item.pic)
                            .into(imageView);
                }
                helper.setText(R.id.tv_name, item.goods_name);
                helper.setText(R.id.tv_code, "商品编号: " + item.sku_id);
                helper.setText(R.id.tv_name, "库存数量: " + item.goods_num);
                helper.setText(R.id.tv_price, item.price + "");
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dingdanview.addGoods(datas.get(position));
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        datas.clear();
        ConnectDao.Calculate(ed_search.getText().toString(), MyApplication.userId, new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
                datas.clear();
                try {
                    JSONArray jsonArray = JSON.parseArray(responseObj);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject o = (JSONObject) jsonArray.get(i);
                        GoodSInfo.GoodsInfoBean goodsInfoBean = JSON.parseObject(o.toString(), GoodSInfo.GoodsInfoBean.class);
                        datas.add(goodsInfoBean);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
