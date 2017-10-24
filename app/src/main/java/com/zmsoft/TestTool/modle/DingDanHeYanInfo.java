package com.zmsoft.TestTool.modle;

import java.util.List;

/**
 * Created by gly on 2017/9/25.
 */

public class DingDanHeYanInfo {
    @Override
    public String toString() {
        return "DingDanHeYanInfo{" +
                "flog=" + flog +
                ", validate_id='" + validate_id + '\'' +
                ", validate_time='" + validate_time + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", num=" + num +
                ", money='" + money + '\'' +
                ", order_sltmode=" + order_sltmode +
                ", goods_id='" + goods_id + '\'' +
                ", orderinfo=" + orderinfo +
                '}';
    }

    public DingDanHeYanInfo() {
    }

    /**
     * validate_id : 10827
     * validate_time : 2017.09.25 09:54
     * orderinfo : [{"id":"23961","goods_id":"20170915094532195","sku_id":"8993175537469","goods_name":"richeese纳宝帝奶酪威化饼","spec":"145g","typename":"第五层","nums":"1.00","actual_num":"1.00","unit":"包","unitpay":"0.01","total":"0.01","actual_total":"0.01","remark":null,"storage_location":"","printer":null,"pic":"/uploads/mobile/shing/803210c1cfa5d776717150165648290e1.png"}]
     * name : 且将青衫换酒℡
     * time : 2017.09.15 09:45
     * num : 1
     * money : 0.01
     * order_sltmode : 0
     * goods_id : 20170915094532195
     */

    public int flog;
    public String validate_id  = "";
    public String validate_time  = "";
    public String name  = "";
    public String time  = "";
    public int num;
    public String money ="";
    public int order_sltmode;
    public String goods_id  = "";
    public List<OrderinfoBean> orderinfo;

    public static class OrderinfoBean {

        public OrderinfoBean() {
        }

        /**
         * id : 23961
         * goods_id : 20170915094532195
         * sku_id : 8993175537469
         * goods_name : richeese纳宝帝奶酪威化饼
         * spec : 145g
         * typename : 第五层
         * nums : 1.00
         * actual_num : 1.00
         * unit : 包
         * unitpay : 0.01
         * total : 0.01
         * actual_total : 0.01
         * remark : null
         * storage_location :
         * printer : null
         * pic : /uploads/mobile/shing/803210c1cfa5d776717150165648290e1.png
         */

        public String id;
        public String goods_id;
        public String sku_id;
        public String goods_name  = "";
        public String spec;
        public String typename;
        public String nums;
        public String actual_num  = "";
        public String unit;
        public String unitpay;
        public String total;
        public String actual_total;
        public Object remark;
        public String storage_location;
        public Object printer;
        public String pic;
    }
}
