package com.zmsoft.TestTool.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单流实体类
 */

public class OrderFlowInfo implements Serializable {
    private String name = "山岳小号";
    private String payNAme = "春暖花开";
    private String date = "2017-08-26 08:10";
    private String orderCode = "321321321321";
    private String byCount = "1";
    private String sendCost = "1";
    private String packCost = "0.01";
    private String discount = "0.00";
    private String shouldPay = "1.01";
    private String dress = "张三\n13823544775 \n天通苑北街05栋1楼2单元904室";
    private List<GoodSInfo.GoodsInfoBean> goodSInfos;
    private List<String> printerAddress;

    public String getPayNAme() {
        return payNAme;
    }

    public void setPayNAme(String payNAme) {
        this.payNAme = payNAme;
    }

    public OrderFlowInfo() {
        goodSInfos = new ArrayList<>();
        goodSInfos.add(new GoodSInfo.GoodsInfoBean(0.01, 1, "农夫山泉 饮用山泉水500ml"));
        printerAddress = new ArrayList<>();
        printerAddress.add("0D:30:24:CC:F6");
        printerAddress.add("0D:30:26:EF:23");
    }

    public List<String> getPrinterAddress() {
        return printerAddress;
    }

    public void setPrinterAddress(List<String> printerAddress) {
        this.printerAddress = printerAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<GoodSInfo.GoodsInfoBean> getGoodSInfos() {
        return goodSInfos;
    }

    public void setGoodSInfos(List<GoodSInfo.GoodsInfoBean> goodSInfos) {
        this.goodSInfos = goodSInfos;
    }

    public String getByCount() {
        return byCount;
    }

    public void setByCount(String byCount) {
        this.byCount = byCount;
    }

    public String getSendCost() {
        return sendCost;
    }

    public void setSendCost(String sendCost) {
        this.sendCost = sendCost;
    }

    public String getPackCost() {
        return packCost;
    }

    public void setPackCost(String packCost) {
        this.packCost = packCost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(String shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getDress() {
        return dress;
    }

    public void setDress(String dress) {
        this.dress = dress;
    }
}
