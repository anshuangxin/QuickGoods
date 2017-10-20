package com.bjyzqs.kuaihuo_yunshouyin.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.bjyzqs.kuaihuo_yunshouyin.modle.GoodSInfo;
import com.bjyzqs.kuaihuo_yunshouyin.modle.OrderFlowInfo;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baseListadapter.CommonAdapter;
import com.bjyzqs.kuaihuo_yunshouyin.utils.baseListadapter.ViewHolder;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.bjyzqs.kuaihuo_yunshouyin.R;

public class BluetoothConnectUtil {
    private Context context = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private ArrayList<BluetoothDevice> devices = null;
    private PrintDataUtil printDataService;


    /**
     * 绑定蓝牙设备
     */
    private void bondDevice(int position) {
        try {
            Method createBondMethod = BluetoothDevice.class
                    .getMethod("createBond");
            createBondMethod
                    .invoke(devices.get(position));
        } catch (Exception e) {
            Toast.makeText(context, "配对失败！", Toast.LENGTH_SHORT)
                    ;
        }
    }

    public BluetoothConnectUtil(Context context) {
        this.context = context;
        this.devices = new ArrayList<>();
        this.initIntentFilter();

    }

    private void initIntentFilter() {
        // 设置广播信息过滤    
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果    
        context.registerReceiver(receiver, intentFilter);

    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 1);

    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        if (null != this.bluetoothAdapter) {
            this.bluetoothAdapter.disable();
        }
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        return this.bluetoothAdapter.isEnabled();

    }

    /**
     * 搜索蓝牙设备
     */
    public void searchDevices() {
        this.devices.clear();
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        this.bluetoothAdapter.startDiscovery();
    }

    /**
     * 添加蓝牙设备到list集合
     *
     * @param device
     */
    public void addDevices(BluetoothDevice device) {
        System.out.println("未绑定设备名称：" + device.getName());
        if (!this.devices.contains(device)) {
            this.devices.add(device);
        }
    }


    /**
     * 蓝牙广播接收器
     */

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        AlertDialog dialog;
        AlertDialog.Builder builder;

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("onReceive" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (!TextUtils.isEmpty(name)&&device.getName().toLowerCase().contains("printer")) {
                    addDevices(device);
                    bluetoothAdapter.cancelDiscovery();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                builder = new AlertDialog.Builder(context).setTitle("搜索蓝牙设备中...");
                dialog = builder.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                System.out.println("设备搜索完毕");
                dialog.dismiss();
                if (devices.size() == 0) {
                    builder.setTitle("没有找到打印机").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    CommonAdapter<BluetoothDevice> commonAdapter = new CommonAdapter<BluetoothDevice>(context, devices, R.layout.device_item) {
                        @Override
                        public void convert(ViewHolder helper, BluetoothDevice item) {
                            helper.setText(R.id.device_name, item.getName());
                            helper.setText(R.id.device_state, item.getBondState() == BluetoothDevice.BOND_BONDED ? "已绑定" : "未绑定");
                        }
                    };
                    builder.setTitle("搜索完成").setAdapter(commonAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.out.println("click！" + i);
                            BluetoothDevice bluetoothDevice = devices.get(i);
                            if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                                bondDevice(i);
                            } else {
                                printDataService = new PrintDataUtil(context, devices.get(i).getAddress());
                                boolean connect = printDataService.connect();
                                if (connect == false) {
                                    // 连接失败
                                    System.out.println("连接失败！");
                                } else {
                                    // 连接成功
                                    System.out.println("连接成功！");
                                    print(new OrderFlowInfo());

                                }
                            }
                        }
                    });
                }
                dialog = builder.show();
                // bluetoothAdapter.cancelDiscovery();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------打开蓝牙-----------");
                    searchDevices();
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    System.out.println("--------关闭蓝牙-----------");
                }
            }
        }
    };

    public static String callBack(String input) {
        int[] array = {0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 4, 5, 2, 3, 4, 1, 0, 1,
                0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i % 7 == 0)
                sb.append("\n");
            if (array[i] == 0)
                sb.append("    ");
            else if (array[i] == 4)
                sb.append("    ");
            else if (array[i] == 5)
                sb.append("国庆");
            else if (array[i] == 2)
                sb.append("");
            else if (array[i] == 3)
                sb.append("快乐!");
            else
                sb.append("" + input);
        }
        return sb.toString();
    }


    public void print(OrderFlowInfo serializableExtra) {
        if (null == printDataService) {
            return;
        }
        printDataService.selectCommand(PrintDataUtil.RESET);
        printDataService.selectCommand(PrintDataUtil.LINE_SPACING_DEFAULT);
        printDataService.selectCommand(PrintDataUtil.ALIGN_CENTER);
        printDataService.selectCommand(PrintDataUtil.DOUBLE_HEIGHT_WIDTH);
        printDataService.printText("V18\n\n");
        printDataService.selectCommand(PrintDataUtil.NORMAL);
        printDataService.printText("(如需自取,请注意叫号)\n\n");
        printDataService.selectCommand(PrintDataUtil.DOUBLE_HEIGHT_WIDTH);
        printDataService.printText(serializableExtra.getName() + "\n\n");
        printDataService.selectCommand(PrintDataUtil.NORMAL);
        printDataService.selectCommand(PrintDataUtil.ALIGN_LEFT);
        printDataService.printText("付款人: " + serializableExtra.getPayNAme() + "\n");
        printDataService.printText("日期: " + serializableExtra.getDate() + "\n");
        printDataService.printText("单号: " + serializableExtra.getOrderCode() + "\n");
        printDataService.selectCommand(PrintDataUtil.ALIGN_LEFT);
        printDataService.printText(printDataService.printTwoData("货号", "数量*单价\n"));
        printDataService.printText("********************************\n");

        printDataService.selectCommand(PrintDataUtil.BOLD);
        for (GoodSInfo.GoodsInfoBean info : serializableExtra.getGoodSInfos()) {
            printDataService.printText(printDataService.printTwoData(info.goods_name, info.jianShu + "*" + info.price + "\n"));
        }
        printDataService.selectCommand(PrintDataUtil.BOLD_CANCEL);
        printDataService.selectCommand(PrintDataUtil.NORMAL);
        printDataService.printText("********************************\n");
        printDataService.printText("购买数量: " + serializableExtra.getByCount() + "\n");
        printDataService.printText("配 送 费: " + serializableExtra.getSendCost() + "\n");
        printDataService.printText("打 包 费: " + serializableExtra.getPackCost() + "\n");
        printDataService.printText("折    扣: " + serializableExtra.getDiscount() + "\n");
        printDataService.printText("应付金额: " + serializableExtra.getShouldPay() + "\n\n");
        printDataService.selectCommand(PrintDataUtil.DOUBLE_HEIGHT_WIDTH);
        printDataService.printText(serializableExtra.getDress());
//        printDataService.printText(callBack("慧"));
//        printDataService.printText("美食餐厅\n\n");
//
//        printDataService.printText("桌号：1号桌\n\n");
//        printDataService.selectCommand(PrintDataUtil.NORMAL);
//        printDataService.selectCommand(PrintDataUtil.ALIGN_LEFT);
//        printDataService.printText(printDataService.printTwoData("订单编号", "201507161515\n"));
//        printDataService.printText(printDataService.printTwoData("点菜时间", "2016-02-16 10:46\n"));
//        printDataService.printText(printDataService.printTwoData("上菜时间", "2016-02-16 11:46\n"));
//        printDataService.printText(printDataService.printTwoData("人数：2人", "收银员：张三\n"));
//
//        printDataService.printText("--------------------------------\n");
//        printDataService.selectCommand(PrintDataUtil.BOLD);
//        printDataService.printText(printDataService.printThreeData("项目", "数量", "金额\n"));
//        printDataService.printText("--------------------------------\n");
//        printDataService.selectCommand(PrintDataUtil.BOLD_CANCEL);
//        printDataService.printText(printDataService.printThreeData("面", "1", "0.00\n"));
//        printDataService.printText(printDataService.printThreeData("米饭", "1", "6.00\n"));
//        printDataService.printText(printDataService.printThreeData("铁板烧", "1", "26.00\n"));
//        printDataService.printText(printDataService.printThreeData("一个测试", "1", "226.00\n"));
//        printDataService.printText(printDataService.printThreeData("牛肉面啊啊", "1", "2226.00\n"));
//        printDataService.printText(printDataService.printThreeData("牛肉面啊啊", "888", "98886.00\n"));
//
//        printDataService.printText("--------------------------------\n");
//        printDataService.printText(printDataService.printTwoData("合计", "53.50\n"));
//        printDataService.printText(printDataService.printTwoData("抹零", "3.50\n"));
//        printDataService.printText("--------------------------------\n");
//        printDataService.printText(printDataService.printTwoData("应收", "50.00\n"));
//        printDataService.printText("--------------------------------\n");
//
//        printDataService.selectCommand(printDataService.ALIGN_LEFT);
//        printDataService.printText("备注：不要辣、不要香菜");
        printDataService.printText("\n\n\n\n\n");
    }
}