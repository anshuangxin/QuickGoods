package com.zmsoft.TestTool.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.modle.GoodSInfo;
import com.zmsoft.TestTool.modle.OrderFlowInfo;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.ToastUtil;
import com.zmsoft.TestTool.utils.baseListadapter.CommonAdapter;
import com.zmsoft.TestTool.utils.baseListadapter.ViewHolder;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BluetoothConnectUtil {
    private static final String TAG = "BluetoothConnectUtil";
    private static Context mContext;
    private Context context = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private ArrayList<BluetoothDevice> devices = null;
    private PrintDataUtil printDataService;

    private BluetoothConnectUtil(Context context) {
        Logger.log(TAG + "BluetoothConnectUtil");
        this.context = context;
        this.devices = new ArrayList<>();
        printDataService = new PrintDataUtil(context);
        this.initIntentFilter();
    }

    public static class BluetoothConnectUtilHolder {
        private static final BluetoothConnectUtil instance = new BluetoothConnectUtil(mContext);
    }

    public static BluetoothConnectUtil getInstance(Context context) {
        mContext = context;
        return BluetoothConnectUtilHolder.instance;
    }

    /**
     * 绑定蓝牙设备
     */
    private void bondDevice(int position) {
        Logger.log(TAG + "bondDevice");
        try {
            Method createBondMethod = BluetoothDevice.class
                    .getMethod("createBond");
            createBondMethod
                    .invoke(devices.get(position));
            ToastUtil.showToast("配对成功！");
        } catch (Exception e) {
            ToastUtil.showToast("配对失败！");
        }
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

    public void unregistReceiver() {
        Logger.log(TAG + "unregistReceiver");
        if (null != receiver) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth(Activity activity) {
        Logger.log(TAG + "openBluetooth");
        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 1);
    }

    public void openBluetooth(Context activity) {
        Logger.log(TAG + "openBluetooth");
        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(enableBtIntent);
    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        Logger.log(TAG + "closeBluetooth");
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
        this.bluetoothAdapter.startDiscovery();
    }

    public void searchDevicesWithDialog(Context context) {
        dialog = new BlueToothDialog(context).title("蓝牙设备");
        if (null == commonAdapter) {
            commonAdapter = new CommonAdapter<BluetoothDevice>(context, devices, R.layout.device_item) {
                @Override
                public void convert(ViewHolder helper, BluetoothDevice item) {
                    helper.setText(R.id.device_name, item.getName());

                    helper.setText(R.id.tv_connect_state, PrintDataUtil.printerHolders.contains(item.getAddress()) ? "已连接" : "未连接");
                    helper.setText(R.id.device_state, item.getBondState() == BluetoothDevice.BOND_BONDED ? "已绑定" : "未绑定");
                }
            };
            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Logger.log(TAG + "click！" + position);
                    BluetoothDevice bluetoothDevice = devices.get(position);
                    if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                        bondDevice(position);
                    } else {
                        connect(bluetoothDevice);
                    }
                }
            };
        }
        dialog.adapter(commonAdapter, onItemClickListener);
        dialog.Show();
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            if (device.getName().toLowerCase().contains("printer")) {
                addDevices(device);
            }
        }
        handler.sendEmptyMessageDelayed(100, 300);
        this.bluetoothAdapter.startDiscovery();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    commonAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    /**
     * 添加蓝牙设备到list集合
     *
     * @param device
     */
    public void addDevices(BluetoothDevice device) {
        Logger.log(TAG + "设备名称：" + device.getName() + "设备地址：" + device.getAddress());
        for (int i = 0; i < devices.size(); i++) {
            BluetoothDevice devicea = devices.get(i);
            if (devicea.getAddress().equals(device.getAddress())) {
                devices.remove(devicea);
                i--;
            }
        }
        this.devices.add(device);
    }


    /**
     * 蓝牙广播接收器
     */
    private BlueToothDialog dialog;
    private CommonAdapter<BluetoothDevice> commonAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            Logger.log(TAG + "onReceive" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (!TextUtils.isEmpty(name) && device.getName().toLowerCase().contains("printer")) {
                    addDevices(device);
                    connect(device);
                    if (null != dialog) {
                        dialog.title("搜索蓝牙设备中...");
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                if (null != dialog) {
                    dialog.title("搜索蓝牙设备中...");
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                if (null != dialog) {
                    dialog.title("设备搜索完毕");
                    if (devices.size() == 0) {
                        dialog.title("没有找到打印机");
                    } else {
                        dialog.title("搜索完成");
                    }
                }
                bluetoothAdapter.cancelDiscovery();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    Logger.log(TAG + "--------打开蓝牙-----------");
                    searchDevices();
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    Logger.log(TAG + "--------关闭蓝牙-----------");
                }
            }
        }
    };

    private void connect(BluetoothDevice device) {
        boolean connect = printDataService.connectAddress(device.getAddress());
        if (connect == false) {
            // 连接失败
            ToastUtil.showToast(mContext, "连接失败！", 2000);
            Logger.log(TAG + "连接失败！");
        } else {
            // 连接成功
            ToastUtil.showToast(mContext, "连接成功！", 2000);
            Logger.log(TAG + "连接成功！" + device.getAddress());
            print(new OrderFlowInfo());

        }
        if (null != commonAdapter) {
            commonAdapter.notifyDataSetChanged();
        }
    }

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
        printDataService.setPrintAddress(serializableExtra.getPrinterAddress());
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