package com.gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gly.quickgoods.application.MyApplication;
import com.gly.quickgoods.basees.BaseFragment;
import com.gly.quickgoods.constants.HttpConstants;
import com.gly.quickgoods.dao.ConnectDao;
import com.gly.quickgoods.modle.DingDanHeYanInfo;
import com.gly.quickgoods.utils.Logger;
import com.gly.quickgoods.utils.ToastUtil;
import com.gly.quickgoods.utils.baseListadapter.CommonAdapter;
import com.gly.quickgoods.utils.baseListadapter.ViewHolder;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.views.DrawableLeftText;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/14.
 */

public class DingDanHeYanFragment extends BaseFragment {
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.isshowlayout1)
    LinearLayout isshowlayout1;
    @BindView(R.id.isshowlayout2)
    View isshowlayout2;
    @BindView(R.id.isshowlayout3)
    RelativeLayout isshowlayout3;
    @BindView(R.id.tv_pay_man)
    DrawableLeftText tvPayMan;
    @BindView(R.id.tv_date)
    DrawableLeftText tvDate;
    @BindView(R.id.tv_form_code)
    DrawableLeftText tvFormCode;
    @BindView(R.id.tv_take_type)
    DrawableLeftText tvTakeType;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    @BindView(R.id.img_ischeck)
    ImageView imgIscheck;
    private GridView gridView;
    private boolean isFirstIn = true;
    private CommonAdapter<DingDanHeYanInfo.OrderinfoBean> adapter;
    private ArrayList<DingDanHeYanInfo.OrderinfoBean> datas;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_dngdanheyan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置带gif动画的上拉头与下拉头
//        try {
//            refreshView.setGifRefreshView(new GifDrawable(getResources(), R.drawable.anim));
//            refreshView.setGifLoadmoreView(new GifDrawable(getResources(), R.drawable.anim));
//
//        } catch (Resources.NotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        gridView = (GridView) refreshView.getPullableView();
        initListView();
    }


    /**
     * ListView初始化方法
     */
    private void initListView() {
        datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
//            datas.add("这里是item " + i);
        }
        adapter = new CommonAdapter<DingDanHeYanInfo.OrderinfoBean>(mContext, datas, R.layout.dingdan_list_item) {
            @Override
            public void convert(ViewHolder helper, DingDanHeYanInfo.OrderinfoBean item) {
                ImageView view = helper.getView(R.id.img_title);
                Glide.with(DingDanHeYanFragment.this)
                        .load(HttpConstants.ROOT_URL + item.pic)
                        .into(view);
                helper.setText(R.id.tv_name, item.goods_name);
                helper.setText(R.id.tv_count, item.actual_num);
            }
        };
        gridView.setAdapter(adapter);
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


    @OnClick(R.id.btn_search)
    public void onViewClicked() {
//        edCode.getText().toString()
        refreshView.autoRefresh();
        datas.clear();
        imgIscheck.setVisibility(View.GONE);
        isshowlayout1.setVisibility(View.GONE);
        isshowlayout2.setVisibility(View.GONE);
        isshowlayout3.setVisibility(View.GONE);
        ConnectDao.check(edCode.getText().toString(), MyApplication.userId, new DisposeDataListener<DingDanHeYanInfo>() {
            @Override
            public void onSuccess(DingDanHeYanInfo responseObj) {
                Logger.log(responseObj.toString());
                datas.addAll(responseObj.orderinfo);
                if (responseObj.flog == 1) {
                    imgIscheck.setImageResource(R.drawable.check_sh);
                    imgIscheck.setVisibility(View.VISIBLE);
                } else {
                    imgIscheck.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                tvPayMan.setMyText("付款人: " + responseObj.name);
                tvDate.setMyText("日期: " + responseObj.validate_time.substring(0, 10));
                tvFormCode.setMyText("订单号: " + responseObj.validate_id);
                tvNum.setText(responseObj.num + "");
                tvPrice.setText(responseObj.money + "");
                String bringType = "";
                switch (responseObj.order_sltmode) {
                    case 0:
                        bringType = "堂食";
                        break;
                    case 1:
                        bringType = "送餐";
                        break;
                    case 2:
                        bringType = "打包";
                        break;
                }
                tvTakeType.setMyText(bringType);
                isshowlayout1.setVisibility(View.VISIBLE);
                isshowlayout2.setVisibility(View.VISIBLE);
                isshowlayout3.setVisibility(View.VISIBLE);
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

}
