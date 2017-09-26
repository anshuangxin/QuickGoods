package com.gly.quickgoods.modle;

/**
 * Created by gly on 2017/9/26.
 */

public class LoginInfo {
    public LoginInfo() {
    }

    /**
     * err : 200
     * is_one : -1
     * code : 登录成功
     * message : {"user_id":"13946","logo":"","merchant_name":"","letter":"","user_admin_id":"","industry_id":"","mobile":"13823579661","address":"","aboard":"","password":"25f9e794323b453885f5181f1b624d0b","is_root":"-1","right":"{\"retail\":{\"purchase\":[\"purchased\",\"possess\",\"cash\",\"wechat\",\"alipay\",\"check\"]}}"}
     */

    public String err;
    public String is_one;
    public String code;
    public MessageBean message;

    public static class MessageBean {
        public MessageBean() {
        }

        /**
         * user_id : 13946
         * logo :
         * merchant_name :
         * letter :
         * user_admin_id :
         * industry_id :
         * mobile : 13823579661
         * address :
         * aboard :
         * password : 25f9e794323b453885f5181f1b624d0b
         * is_root : -1
         * right : {"retail":{"purchase":["purchased","possess","cash","wechat","alipay","check"]}}
         */

        public String user_id;
        public String logo;
        public String merchant_name;
        public String letter;
        public String user_admin_id;
        public String industry_id;
        public String mobile;
        public String address;
        public String aboard;
        public String password;
        public String is_root;
        public String right;
    }
}
