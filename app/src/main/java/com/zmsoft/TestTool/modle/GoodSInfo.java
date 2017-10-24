package com.zmsoft.TestTool.modle;

import java.util.List;

/**
 * Created by gly on 2017/9/21.
 */

public class GoodSInfo {


    public List<OneLevelBean> oneLevel;
    public List<GoodsInfoBean> goodsInfo;

    public GoodSInfo() {
    }

    public static class OneLevelBean {
        public OneLevelBean() {
        }

        /**
         * id : 141
         * cat_name : 粤菜轩
         * uid : 10827
         * pic :
         * parent : 0
         * is_del : 1
         * creation_time : 1489114795
         * update_time : 1505304386
         */

        public String id;
        public String cat_name;
        public String uid;
        public String pic;
        public String parent;
        public String is_del;
        public String creation_time;
        public String update_time;

        public OneLevelBean(String cat_name) {
            this.cat_name = cat_name;
        }
    }


    public static class GoodsInfoBean {
        public GoodsInfoBean() {


        }

        public GoodsInfoBean(double price, int jianShu, String goods_name) {
            this.price = price;
            this.jianShu = jianShu;
            this.goods_name = goods_name;
        }

        /**
         * id : 5427
         * sku_id : 6906907101014
         * cat_id : 1508
         * pic :
         * pic_api :
         * price : 0.02
         * goods_name : 百事可乐
         * specification : 330ml / 罐
         * unit : 瓶
         * goods_num : 998.00
         * goods_sum : 1000.00
         * goods_sales : 2.00
         * goods_back : 0.00
         * purchasing_total : 10.00
         * selling_total : 0.04
         * is_del : 1
         * user_id : 0
         * uid : 10827
         * remind : 0
         * path : 57
         * create_time : 1504598342
         * updata_time : 1504598584
         * two_catid : 1509
         * three_catid : null
         * content : null
         * storage_location :
         * out_price : 0.00
         * is_out : 0
         * printer : null
         */

        public int id;
        public String sku_id  = "";
        public String cat_id  = "";
        public String pic  = "";
        public String pic_api  = "";
        public double price;
        public int jianShu;
        public String goods_name  = "";
        public String specification;
        public String unit;
        public String goods_num  = "";
        public String goods_sum;
        public String goods_sales;
        public String goods_back;
        public String purchasing_total;
        public String selling_total;
        public String is_del;
        public String user_id;
        public String uid;
        public String remind;
        public String path;
        public String create_time;
        public String updata_time;
        public String two_catid;
        public Object three_catid;
        public Object content  = "";
        public String storage_location;
        public String out_price;
        public String is_out;
        public Object printer;

        @Override
        public boolean equals(Object obj) {
            GoodsInfoBean GoodsInfoBean = (GoodSInfo.GoodsInfoBean) obj;
            return GoodsInfoBean.id == this.id;
        }
    }

}
