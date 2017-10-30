package com.zmsoft.TestTool.fragment;

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

import com.kyleduo.switchbutton.SwitchButton;
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.activity.LoginActivity;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseFragment;
import com.zmsoft.TestTool.bluetooth.BluetoothConnectUtil;
import com.zmsoft.TestTool.bluetooth.PrintDataUtil;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.dao.DingDanChannel;
import com.zmsoft.TestTool.dao.SpeechDao;
import com.zmsoft.TestTool.modle.OrderFlowInfo;
import com.zmsoft.TestTool.modle.OrderInfo;
import com.zmsoft.TestTool.sdk.jpush.LocalBroadcastManager;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.SharedPreferencesUtil;
import com.zmsoft.TestTool.utils.baserecycleadapter.CommonAdapter;
import com.zmsoft.TestTool.utils.baserecycleadapter.base.ViewHolder;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;
import com.zmsoft.TestTool.views.CircularImageView;
import com.zmsoft.TestTool.views.ReceiveDialog;
import com.zmsoft.TestTool.views.SpacesItemDecoration;

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

    private LocalBroadcastManager localBroadcastManager;
    private boolean isFirstIn = true;
    private ArrayList<OrderInfo.InterfacegoodsBean> interfacegoods;
    private OrderInfo.HeaderBean header;
    private DingdanReciver dingdanReciver;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_dingdanliu, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == localBroadcastManager) {
            MyApplication.getInstance().bluetoothService = BluetoothConnectUtil.getInstance(mContext);
            localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
            initListView();
            initSwitchButton();
            registReciver();
        }
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().bluetoothService.unregistReceiver();
        if (null != localBroadcastManager) {
            localBroadcastManager.unregisterReceiver(dingdanReciver);
        }
    }

    private void registReciver() {
        dingdanReciver = new DingdanReciver();
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

    private class DingdanReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DING_DAN_LIU)) {
                OrderFlowInfo serializableExtra = (OrderFlowInfo) intent.getSerializableExtra(ORDER_FLOW_INFO);
                MyApplication.getInstance().bluetoothService.print(serializableExtra);
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
        swb_auto_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferencesUtil.putBoolean(mContext, "isOpenBlueTooth", b);
                if (b) {
                    if (!MyApplication.getInstance().bluetoothService.isOpen()) {
                        MyApplication.getInstance().bluetoothService.openBluetooth(mContext);
                    } else {
                        MyApplication.getInstance().bluetoothService.searchDevicesWithDialog(mContext);
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
                protected void convert(ViewHolder holder, final OrderInfo.InterfacegoodsBean o, int position) {
                    CircularImageView view = holder.getView(R.id.circle_view);
                    if (o.order_source == 4) {
                        view.setText("S" + o.tickets);
                    } else {
                        view.setText("V" + o.tickets);
                    }
                    view.setTextColor(Color.BLACK);
                    holder.setText(R.id.textview2, DingDanChannel.getChannelText(o.order_source, o.pay, o.order_sltmode));
                    view.setBackgroundColor(getResources().getColor(R.color.btn_bg_normal));
                    TextView tv_tablecode = holder.getView(R.id.tv_tablecode);
                    if (o.tables == 0) {
                        tv_tablecode.setVisibility(View.GONE);
                    } else {
                        tv_tablecode.setVisibility(View.VISIBLE);
                    }
                    final Button btn_pay = holder.getView(R.id.btn_pay);
                    if (o.pay == 8) {
                        btn_pay.setText("付款");
                        btn_pay.setBackgroundResource(R.drawable.login_btn_selector);
                        btn_pay.setTextColor(Color.BLACK);
                        btn_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new ReceiveDialog(mContext, o.actual_sum, o.orderinfo.size(), o.goods_id, new ReceiveDialog.OnCanclelinstener() {
                                    @Override
                                    public void onSuccess() {
                                        o.pay = 9;
                                        btn_pay.setText("已付款");
                                        btn_pay.setTextColor(getResources().getColor(R.color.btn_bg_normal));
                                        btn_pay.setBackgroundResource(R.drawable.rb_dayin_selector);
                                    }

                                    @Override
                                    public void onFail() {

                                    }
                                }).show();
                            }
                        });
                    } else {
                        btn_pay.setText("已付款");
                        btn_pay.setTextColor(getResources().getColor(R.color.btn_bg_normal));
                        btn_pay.setBackgroundResource(R.drawable.rb_dayin_selector);
                        btn_pay.setOnClickListener(null);
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
