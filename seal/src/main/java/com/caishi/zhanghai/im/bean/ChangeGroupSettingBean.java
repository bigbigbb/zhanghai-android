package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/30.
 */

public class ChangeGroupSettingBean {

    /**
     * rid : xxyy
     * m : group
     * k : config
     * v : {"groupId":"3cb3233335c714e079c516ee94999c5e","key":"join_direct"}
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
         * groupId : 3cb3233335c714e079c516ee94999c5e
         * key : join_direct
         * , //要修改的项目         "value":"1", //修改项目的值
         */

        private String groupId;
        private String key;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
