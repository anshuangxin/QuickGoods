package com.bjyzqs.kuaihuo_yunshouyin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.activity.LoginActivity;
import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseFragment;
import com.bjyzqs.kuaihuo_yunshouyin.bluetooth.BluetoothConnectUtil;
import com.bjyzqs.kuaihuo_yunshouyin.bluetooth.PrintDataUtil;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.DingDanChannel;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;
import com.bjyzqs.kuaihuo_yunshouyin.modle.OrderFlowInfo;
import com.bjyzqs.kuaihuo_yunshouyin.modle.OrderInfo;
import com.bjyzqs.kuaihuo_yunshouyin.sdk.jpush.LocalBroadcastManager;
import com.bjyzqs.kuaihuo_yunshouyin.utils.Logger;
import com.bjyzqs.kuaihuo_yunshouyin.utils.SharedPreferencesUtil;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baserecycleadapter.CommonAdapter;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baserecycleadapter.base.ViewHolder;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;
import com.bjyzqs.kuaihuo_yunshouyin.views.CircularImageView;
import com.bjyzqs.kuaihuo_yunshouyin.views.SpacesItemDecoration;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gly on 2017/9/19.
 */

public class DingDanLiuFragment extends BaseFragment {
    public static final String IS_SPEECH = "IS_SPEECH";
    private static final String ACTION_DING_DAN_LIU = "dingdanliu";
    public static final String ORDER_FLOW_INFO = "OrderFlowInfo";
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.swb_auto_speech)
    SwitchButton swb_auto_speech;
    @BindView(R.id.swb_auto_print)
    SwitchButton swb_auto_print;
    private CommonAdapter<OrderInfo.InterfacegoodsBean> adapter;
    private BluetoothConnectUtil bluetoothService;
    private LocalBroadcastManager localBroadcastManager;
    private boolean isFirstIn = true;
    private ArrayList<OrderInfo.InterfacegoodsBean> interfacegoods;
    private OrderInfo.HeaderBean header;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_dingdanliu, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == localBroadcastManager) {
            localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
            bluetoothService = new BluetoothConnectUtil(mContext);
            initListView();
            initSwitchButton();
            registReciver();
        }
        getData();
    }

    private void registReciver() {
        DingdanReciver dingdanReciver = new DingdanReciver();
        localBroadcastManager.registerReceiver(dingdanReciver, new IntentFilter(ACTION_DING_DAN_LIU));
    }

    public void getData() {
        ConnectDao.orderlist(MyApplication.userId, new DisposeDataListener<OrderInfo>() {
            @Override
            public void onSuccess(OrderInfo orderInfo) {
                Logger.log("orderInfo:" + orderInfo);
                header = orderInfo.header;
                interfacegoods.clear();
                interfacegoods.addAll(orderInfo.interfacegoods);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @OnClick(R.id.btn_loginout)
    public void onViewClicked() {
        new AlertDialog.Builder(mContext).setTitle("确认退出登录?").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferencesUtil.putString(mContext, "user_id", "");
                startActivity(new Intent(mContext, LoginActivity.class));
                mContext.finish();
            }
        }).setPositiveButton("取消", null).show();

    }

    class DingdanReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DING_DAN_LIU)) {
                OrderFlowInfo serializableExtra = (OrderFlowInfo) intent.getSerializableExtra(ORDER_FLOW_INFO);
                bluetoothService.print(serializableExtra);
            }
        }
    }

    private void initSwitchButton() {
        swb_auto_speech.setChecked(SharedPreferencesUtil.getBoolean(mContext, IS_SPEECH, true));
        swb_auto_speech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpeechDao.isOpenSpeech(b);
            }
        });

        boolean isOpenBlueTooth = SharedPreferencesUtil.getBoolean(mContext, "isOpenBlueTooth");
        swb_auto_print.setChecked(isOpenBlueTooth);
        if (isOpenBlueTooth) {
            if (!bluetoothService.isOpen()) {
                // 蓝牙关闭的情况
                System.out.println("蓝牙关闭的情况");
                bluetoothService.openBluetooth(mContext);
            } else {
                bluetoothService.searchDevices();
            }
        }
        swb_auto_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferencesUtil.putBoolean(mContext, "isOpenBlueTooth", b);
                if (b) {
                    if (!bluetoothService.isOpen()) {
                        // 蓝牙关闭的情况
                        System.out.println("蓝牙关闭的情况");
                        bluetoothService.openBluetooth(mContext);
                    } else {
                        bluetoothService.searchDevices();
                    }
                } else {
                    PrintDataUtil.disconnect();
                }
            }
        });
    }

    private void initListView() {
        if (null == interfacegoods) {
            interfacegoods = new ArrayList<>();
            recycleview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            adapter = new CommonAdapter<OrderInfo.InterfacegoodsBean>(mContext, interfacegoods, R.layout.dingdanliu_view) {
                @Override
                protected void convert(ViewHolder holder, OrderInfo.InterfacegoodsBean o, int position) {
                    CircularImageView view = holder.getView(R.id.circle_view);
                    view.setText("v" + o.tickets);
                    view.setTextColor(Color.BLACK);
                    holder.setText(R.id.textview2, DingDanChannel.getChannel(o.order_source));
                    view.setBackgroundColor(getResources().getColor(R.color.btn_bg_normal));
                    TextView tv_tablecode = holder.getView(R.id.tv_tablecode);
                    if (o.tables == 0) {
                        tv_tablecode.setVisibility(View.GONE);
                    } else {
                        tv_tablecode.setVisibility(View.VISIBLE);
                    }
                    Button btn_pay = holder.getView(R.id.btn_pay);
                    if (o.pay == 8) {
                        btn_pay.setText("付款");
                    } else {
                        btn_pay.setText("已付款");
                    }

                    holder.setText(R.id.tv_tablecode, "桌号:" + o.tables);
                    holder.setText(R.id.tv_pay_man, o.name);
                    holder.setText(R.id.tv_title, header.merchant_name);
                    holder.setText(R.id.tv_control_man, header.name);
                    holder.setText(R.id.tv_odd_number, "单号:" + o.goods_id);
                    holder.setText(R.id.tv_total_price, "¥" + o.actual_sum);
                    holder.setText(R.id.tv_buy_number, "购买数量:" + o.orderinfo.size());
                    holder.setText(R.id.tv_date, o.goods_time);

                    LinearLayout lin_childcontinor = holder.getView(R.id.lin_childcontinor);
                    lin_childcontinor.removeAllViews();

                    for (OrderInfo.InterfacegoodsBean.OrderinfoBean item : o.orderinfo) {
                        View view1 = LinearLayout.inflate(mContext, R.layout.dingdanliuview_listitem, null);
                        TextView tv_name = view1.findViewById(R.id.tv_name);
                        tv_name.setText(item.goods_name);
                        TextView tv_price = view1.findViewById(R.id.tv_price);
                        tv_price.setText("¥" + item.unitpay);
                        TextView tv_count = view1.findViewById(R.id.tv_count);
                        tv_count.setText("X" + item.nums);
                        TextView textView3 = view1.findViewById(R.id.textView3);
                        textView3.setText("货号:" + item.sku_id);
                        lin_childcontinor.addView(view1);
                    }
                }
            };
            SpacesItemDecoration decoration = new SpacesItemDecoration(16);
            recycleview.addItemDecoration(decoration);
            recycleview.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();

    }
}
