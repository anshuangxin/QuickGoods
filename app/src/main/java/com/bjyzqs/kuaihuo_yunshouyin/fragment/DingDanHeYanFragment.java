package com.bjyzqs.kuaihuo_yunshouyin.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseFragment;
import com.bjyzqs.kuaihuo_yunshouyin.constants.HttpConstants;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;
import com.bjyzqs.kuaihuo_yunshouyin.modle.DingDanHeYanInfo;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Logger;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baseListadapter.CommonAdapter;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baseListadapter.ViewHolder;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;
import com.bjyzqs.kuaihuo_yunshouyin.views.DrawableLeftText;
import com.bjyzqs.kuaihuo_yunshouyin.views.MessageDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.frm_ischeck)
    FrameLayout frm_ischeck;
    @BindView(R.id.tv_heyan_date)
    TextView tv_heyan_date;
    private CommonAdapter<DingDanHeYanInfo.OrderinfoBean> adapter;
    private ArrayList<DingDanHeYanInfo.OrderinfoBean> datas;
    private MessageDialog messageDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_dngdanheyan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
        edCode.setFocusable(true);
        edCode.setFocusableInTouchMode(true);
        edCode.requestFocus();
        edCode.setInputType(0);
        edCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                if (!TextUtils.isEmpty(s) && s.endsWith("-B")) {
                    search();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    /**
     * ListView初始化方法
     */
    private void initListView() {
        datas = new ArrayList<>();
        adapter = new CommonAdapter<DingDanHeYanInfo.OrderinfoBean>(mContext, datas, R.layout.dingdan_list_item) {
            @Override
            public void convert(ViewHolder helper, DingDanHeYanInfo.OrderinfoBean item) {
                if (!TextUtils.isEmpty(item.pic)) {
                    ImageView view = helper.getView(R.id.img_title);
                    Glide.with(DingDanHeYanFragment.this)
                            .load(HttpConstants.ROOT_URL + item.pic)
                            .into(view);
                }
                helper.setText(R.id.tv_name, item.goods_name);
                helper.setText(R.id.tv_count, String.valueOf(item.actual_num));
            }
        };
        gridview.setAdapter(adapter);
    }


    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        search();
    }

    private void search() {
        edCode.getText().toString();
        datas.clear();
        frm_ischeck.setVisibility(View.GONE);
        isshowlayout1.setVisibility(View.GONE);
        isshowlayout2.setVisibility(View.GONE);
        isshowlayout3.setVisibility(View.GONE);
        ConnectDao.check(edCode.getText().toString(), MyApplication.userId, new DisposeDataListener<DingDanHeYanInfo>() {
            @Override
            public void onSuccess(DingDanHeYanInfo responseObj) {
                datas.clear();
                Logger.log(responseObj.toString());
                if (responseObj.flog == 1) {
                    if (null == messageDialog) {
                        messageDialog = new MessageDialog(mContext).isSuccess(false).title("").delayTime(2000).message("此订单已过期失效");
                    }
                    messageDialog.show();
                    SpeechDao.check(false);
                } else {
                    SpeechDao.check(true);
                }
                frm_ischeck.setVisibility(View.VISIBLE);
                tv_heyan_date.setText(responseObj.validate_time);
                datas.addAll(responseObj.orderinfo);
                adapter.notifyDataSetChanged();
                tvPayMan.setMyText("付款人: " + responseObj.name);
                if (!TextUtils.isEmpty(responseObj.validate_time)) {
                    tvDate.setMyText("日期: " + responseObj.time);
                } else {
                    tvDate.setMyText("");
                }
                tvFormCode.setMyText("订单号: " + responseObj.goods_id);
                tvNum.setText(String.valueOf(responseObj.num));
                tvPrice.setText(responseObj.money);
                String bringType = "";
                switch (responseObj.order_sltmode)

                {
                    case 0:
                        bringType = "堂食";
                        break;
                    case 1:
                        bringType = "送餐";
                        break;
                    case 2:
                        bringType = "打包";
                        break;
                    default:
                        bringType = "";
                        break;

                }
                tvTakeType.setMyText(bringType);
                isshowlayout1.setVisibility(View.VISIBLE);
                isshowlayout2.setVisibility(View.VISIBLE);
                isshowlayout3.setVisibility(View.VISIBLE);
                if (null != edCode) {
                    edCode.setText("");
                    edCode.setFocusable(true);
                    edCode.setFocusableInTouchMode(true);
                    edCode.requestFocus();
                    edCode.findFocus();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                SpeechDao.check(false);
                if (null == messageDialog) {
                    messageDialog = new MessageDialog(mContext).isSuccess(false).title("").delayTime(3000).message("暂无此订单,请确认订单号正确");
                    messageDialog.show();
                }
                if (null != edCode) {
                    edCode.setText("");
                    edCode.setFocusable(true);
                    edCode.setFocusableInTouchMode(true);
                    edCode.requestFocus();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (null != edCode) {
                        edCode.setText("20171017152753204-B");
                    }
                    break;
            }
        }
    };

}
