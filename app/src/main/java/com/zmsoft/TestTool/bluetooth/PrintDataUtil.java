package com.zmsoft.TestTool.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrintDataUtil {
    private Context context = null;
    public static List<PrinterHolder> printerHolders = new ArrayList<>();
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private static final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private class PrinterHolder {
        public String deviceAddress;
        public boolean isPrint;
        public BluetoothDevice bluetoothDevice;
        public BluetoothSocket bluetoothSocket;
        public OutputStream outputStream;

        public PrinterHolder(String deviceAddress, BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket, OutputStream outputStream) {
            this.deviceAddress = deviceAddress;
            this.bluetoothDevice = bluetoothDevice;
            this.bluetoothSocket = bluetoothSocket;
            this.outputStream = outputStream;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj instanceof String) {
                return deviceAddress.equals(obj);
            } else return false;
        }

    }

    /**
     * 打印纸一行最大的字节
     */
    private static final int LINE_BYTE_SIZE = 32;

    private static final int LEFT_LENGTH = 20;

    private static final int RIGHT_LENGTH = 12;

    /**
     * 左侧汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 8;

    /**
     * 小票打印菜品的名称，上限调到8个字
     */
    public static final int MEAL_NAME_MAX_LENGTH = 8;


    public PrintDataUtil(Context context) {
        super();
        this.context = context;

    }

    /**
     * 连接蓝牙设备
     */
    public boolean connectAddress(String addres) {
        if (printerHolders.contains(addres)) {
            return false;
        }
        BluetoothDevice bluetoothDevice = this.bluetoothAdapter.getRemoteDevice(addres);
        try {
            BluetoothSocket bluetoothSocket = bluetoothDevice
                    .createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            OutputStream outputStream = bluetoothSocket.getOutputStream();
//            if (this.bluetoothAdapter.isDiscovering()) {
//                System.out.println("关闭适配器！");
//                this.bluetoothAdapter.isDiscovering();
//            }
            printerHolders.add(new PrinterHolder(addres, bluetoothDevice, bluetoothSocket, outputStream));
            Toast.makeText(this.context, bluetoothDevice.getName() + "连接成功！",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this.context, "连接失败！", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    /**
     * 断开蓝牙设备连接
     */
    public static void disconnect() {
        System.out.println("断开蓝牙设备连接");
        try {
            for (PrinterHolder printerHolder : printerHolders) {
                printerHolder.bluetoothSocket.close();
                printerHolder.outputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block    
            e.printStackTrace();
        }

    }


    /**
     * 打印文字
     *
     * @param text 要打印的文字
     */
    public void printText(String text) {
        try {
            byte[] data = text.getBytes("gbk");
            for (PrinterHolder printerHolder : printerHolders) {
                if (printerHolder.isPrint) {
                    printerHolder.outputStream.write(data, 0, data.length);
                    printerHolder.outputStream.flush();
                }
            }
        } catch (IOException e) {
            //Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 设置打印格式
     *
     * @param command 格式指令
     */
    public void selectCommand(byte[] command) {
        try {
            for (PrinterHolder printerHolder : printerHolders) {
                printerHolder.outputStream.write(command);
                printerHolder.outputStream.flush();
            }
        } catch (IOException e) {
            //Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 复位打印机
     */
    public static final byte[] RESET = {0x1b, 0x40};

    /**
     * 左对齐
     */
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};

    /**
     * 中间对齐
     */
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};

    /**
     * 右对齐
     */
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};

    /**
     * 选择加粗模式
     */
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    /**
     * 取消加粗模式
     */
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    /**
     * 宽高加倍
     */
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    /**
     * 宽加倍
     */
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    /**
     * 高加倍
     */
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    /**
     * 字体不放大
     */
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};

    /**
     * 设置默认行间距
     */
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 格式化菜品名称，最多显示MEAL_NAME_MAX_LENGTH个数
     *
     * @param name
     * @return
     */
    public String formatMealName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() > MEAL_NAME_MAX_LENGTH) {
            return name.substring(0, 8) + "..";
        }
        return name;
    }


    public void setPrintAddress(List<String> printAddress) {
        for (PrinterHolder printerHolder : printerHolders) {
            printerHolder.isPrint = printAddress.contains(printerHolder.deviceAddress);
        }
    }
}