package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by yusy on 2018/1/29.
 */

public class AgreeGroupBean {

    /**
     * rid : xxyy
     * m : group
     * k : applies
     * v : {"groupId":"bdc59baa47627c4b38c43fe2fbc4f4ae","applyer_accounts":["e79023c707c97128a24bb16d18e363af"]}
     */

    private String rid;
    private String m;
    private String k;
    private VBean v;

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

    public VBean getV() {
        return v;
    }

    public void setV(VBean v) {
        this.v = v;
    }

    public static class VBean {
        /**
         * groupId : bdc59baa47627c4b38c43fe2fbc4f4ae
         * applyer_accounts : ["e79023c707c97128a24bb16d18e363af"]
         */

        private String groupId;
        private List<String> applyer_accounts;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<String> getApplyer_accounts() {
            return applyer_accounts;
        }

        public void setApplyer_accounts(List<String> applyer_accounts) {
            this.applyer_accounts = applyer_accounts;
        }
    }
}
