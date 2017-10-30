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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;
import com.zmsoft.TestTool.constants.HttpConstants;
import com.zmsoft.TestTool.dao.ActivityStarter;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.dao.SpeechDao;
import com.zmsoft.TestTool.modle.DingDanHeYanInfo;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.MyTextUtils;
import com.zmsoft.TestTool.utils.baseListadapter.CommonAdapter;
import com.zmsoft.TestTool.utils.baseListadapter.ViewHolder;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.DrawableLeftText;
import com.zmsoft.TestTool.views.MessageDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zbar.lib.CaptureActivity.ACTION_DECODE_INTENT;
import static com.zbar.lib.CaptureActivity.KEY_DECODE_RESULT;

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
    private ShouQuanReciver shouQuanReciver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_dngdanheyan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
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
        registReciver();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyTextUtils.reSetEdit(edCode);
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
            if (intent.getAction().equals(ACTION_DECODE_INTENT)) {
                String result = intent.getStringExtra(KEY_DECODE_RESULT);
                Logger.log("dingdanrecive" + result);
                MyTextUtils.reSetEdit(edCode, result);
            }
        }
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


    @OnClick(R.id.btn_decode)
    public void onViewClicked() {
        ActivityStarter.startCapActivity(getContext());
    }

    private void search() {
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
                    messageDialog = new MessageDialog(mContext).isSuccess(false).title("").delayTime(2000).message("此订单已核验");
                    messageDialog.show();
                    SpeechDao.check(false);
                } else {
                    messageDialog = new MessageDialog(mContext).isSuccess(true).title("").delayTime(2000).message("核验成功");
                    messageDialog.show();
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
                MyTextUtils.reSetEdit(edCode);
            }

            @Override
            public void onFailure(Object reasonObj) {
                SpeechDao.check(false);
                messageDialog = new MessageDialog(mContext).isSuccess(false).title("").delayTime(3000).message("暂无此订单,请确认订单号正确");
                messageDialog.show();
                MyTextUtils.reSetEdit(edCode);
            }
        });
        MyTextUtils.reSetEdit(edCode);
    }

}
