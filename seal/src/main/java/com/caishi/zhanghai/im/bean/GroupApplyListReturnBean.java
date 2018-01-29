package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by yusy on 2018/1/29.
 */

public class GroupApplyListReturnBean {

    /**
     * rid : 1517237187789
     * m : group
     * k : applies
     * v : ok
     * desc : 获取成功
     * data : [{"account":"6309d2d98922359173b9931e9110ed2c","nickname":"Yusy","portraitUri":"http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124203042.jpg","passed":0,"apply_time":"2018-01-29T22:43:16.000Z"}]
     */

    private String rid;
    private String m;
    private String k;
    private String v;
    private String desc;
    private List<DataBean> data;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * account : 6309d2d98922359173b9931e9110ed2c
         * nickname : Yusy
         * portraitUri : http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124203042.jpg
         * passed : 0
         * apply_time : 2018-01-29T22:43:16.000Z
         */

        private String account;
        private String nickname;
        private String portraitUri;
        private int passed;
        private String apply_time;
        private boolean isCheck = false;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }

        public int getPassed() {
            return passed;
        }

        public void setPassed(int passed) {
            this.passed = passed;
        }

        public String getApply_time() {
            return apply_time;
        }

        public void setApply_time(String apply_time) {
            this.apply_time = apply_time;
        }
    }
}
