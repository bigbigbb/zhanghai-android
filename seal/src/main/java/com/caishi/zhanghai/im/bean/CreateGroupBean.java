package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by shihui on 2018/1/16.
 */

public class CreateGroupBean {


    /**
     * rid : xxyy
     * m : group
     * k : create
     * v : {"groupName":"吃喝玩乐群","groupMemberList":["e10adc3949ba59abbe56e057f20f883e","e10adc3949ba59abbe56e057f20f883e"],"is_join_via_pay":"1","join_amount":"10.00","is_recheck_paid":"0","is_view_each":"0","is_invite_each":"0"}
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
         * groupName : 吃喝玩乐群
         * groupMemberList : ["e10adc3949ba59abbe56e057f20f883e","e10adc3949ba59abbe56e057f20f883e"]
         * is_join_via_pay : 1
         * join_amount : 10.00
         * is_recheck_paid : 0
         * is_view_each : 0
         * is_invite_each : 0
         */

        private String groupName;
        private String is_join_via_pay;
        private String join_amount;
        private String is_recheck_paid;
        private String is_view_each;
        private String is_invite_each;
        private List<String> groupMemberList;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getIs_join_via_pay() {
            return is_join_via_pay;
        }

        public void setIs_join_via_pay(String is_join_via_pay) {
            this.is_join_via_pay = is_join_via_pay;
        }

        public String getJoin_amount() {
            return join_amount;
        }

        public void setJoin_amount(String join_amount) {
            this.join_amount = join_amount;
        }

        public String getIs_recheck_paid() {
            return is_recheck_paid;
        }

        public void setIs_recheck_paid(String is_recheck_paid) {
            this.is_recheck_paid = is_recheck_paid;
        }

        public String getIs_view_each() {
            return is_view_each;
        }

        public void setIs_view_each(String is_view_each) {
            this.is_view_each = is_view_each;
        }

        public String getIs_invite_each() {
            return is_invite_each;
        }

        public void setIs_invite_each(String is_invite_each) {
            this.is_invite_each = is_invite_each;
        }

        public List<String> getGroupMemberList() {
            return groupMemberList;
        }

        public void setGroupMemberList(List<String> groupMemberList) {
            this.groupMemberList = groupMemberList;
        }
    }
}
