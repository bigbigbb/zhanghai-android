package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/24.
 */

public class QuitGroupBean {

    /**
     * rid : xxyy
     * m : group
     * k : quit
     * v : {"groupId":"bdc59baa47627c4b38c43fe2fbc4f4ae"}
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
         */

        private String groupId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }
}
