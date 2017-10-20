package com.bjyzqs.kuaihuo_yunshouyin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseFragment;
import com.bjyzqs.kuaihuo_yunshouyin.constants.HttpConstants;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.modle.GoodSInfo;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baseListadapter.CommonAdapter;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baseListadapter.ViewHolder;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;
import com.bjyzqs.kuaihuo_yunshouyin.views.DingDanView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
        if (null == datas) {
            initListView();

            dingdanview.setOnChangeLinstener(new DingDanView.onChangeLinstener() {
                @Override
                public void onClear() {
                    if (null != datas) {
                        datas.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            ed_search.setInputType(0);

            ed_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String s = charSequence.toString();
                    if (!TextUtils.isEmpty(s) && s.length() >= 13) {
                        search();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
//            ed_search.setText("6920202888883");
        }

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
                helper.setText(R.id.tv_price, String.valueOf(item.price));
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
        search();
    }

    private void search() {
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
                        dingdanview.addGoods(goodsInfoBean);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (null != ed_search) {
                    ed_search.setText("");
                    ed_search.requestFocus();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                if (null != ed_search) {
                    ed_search.setText("");
                    ed_search.requestFocus();
                }
            }
        });
    }
}
