package com.zmsoft.TestTool.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;
import com.zmsoft.TestTool.constants.HttpConstants;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.modle.GoodSInfo;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.baseListadapter.CommonAdapter;
import com.zmsoft.TestTool.utils.baseListadapter.ViewHolder;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.DingDanView;
import com.bumptech.glide.Glide;
import com.zbar.lib.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zbar.lib.CaptureActivity.ACTION_DECODE_INTENT;
import static com.zbar.lib.CaptureActivity.KEY_DECODE_RESULT;

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
    private ShouQuanReciver shouQuanReciver;
    private LocalBroadcastManager localBroadcastManager;

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
            ed_search.setFocusable(true);
            ed_search.setFocusableInTouchMode(true);
            ed_search.requestFocus();
            registReciver();
        }
    }

    private void registReciver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        shouQuanReciver = new ShouQuanReciver();
        localBroadcastManager.registerReceiver(shouQuanReciver, new IntentFilter(ACTION_DECODE_INTENT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != localBroadcastManager && null != shouQuanReciver) {
            localBroadcastManager.unregisterReceiver(shouQuanReciver);
        }
    }

    private class ShouQuanReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(KEY_DECODE_RESULT);
            Logger.log("tiaomarecive" + result);
            ed_search.setText(result);
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

    @OnClick(R.id.btn_decode)
    public void onViewClicked() {
        getContext().startActivity(new Intent(getContext(), CaptureActivity.class));
    }

    private void search() {
        datas.clear();
        ConnectDao.Calculate(ed_search.getText().toString(), MyApplication.userId, new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
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
                reSetEditText();
            }

            @Override
            public void onFailure(Object reasonObj) {
                reSetEditText();
            }
        });
    }

    private void reSetEditText() {
        if (null != ed_search) {
            ed_search.setText("");
            ed_search.setFocusable(true);
            ed_search.setFocusableInTouchMode(true);
            ed_search.requestFocus();
        }
    }
}
