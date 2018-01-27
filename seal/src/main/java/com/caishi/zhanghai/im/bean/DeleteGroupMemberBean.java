package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by yusy on 2018/1/27.
 */

public class DeleteGroupMemberBean {

    /**
     * rid : xxyy
     * m : group
     * k : kick
     * v : {"groupId":"bdc59baa47627c4b38c43fe2fbc4f4ae","memberIds":["9f2a66f2793f56024210a460a53b6fd0","43425cb13460923d8713e6e9ffcfda8a"]}
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
         * memberIds : ["9f2a66f2793f56024210a460a53b6fd0","43425cb13460923d8713e6e9ffcfda8a"]
         */

        private String groupId;
        private List<String> memberIds;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<String> getMemberIds() {
            return memberIds;
        }

        public void setMemberIds(List<String> memberIds) {
            this.memberIds = memberIds;
        }
    }
}
