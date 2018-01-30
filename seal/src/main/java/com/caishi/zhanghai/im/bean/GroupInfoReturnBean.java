package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/26.
 */

public class GroupInfoReturnBean {


    /**
     * rid : 1517286817787
     * m : group
     * k : info
     * v : ok
     * desc : 获取成功
     * data : {"id":"419eb9e7a964a8ea2a9ba43584eef937","code":900052,"name":"测试333","myDisplayName":null,"join_direct":0,"is_join_via_pay":"0.00","join_amount":"0.00","is_view_each":0,"is_invite_each":0,"portraitUri":"http://zhanghai.looklaw.cn/avatar/?name=%E6%B5%8B%E8%AF%95333","memberCount":2,"maxMemberCount":500,"creatorId":"7ef80afb69f80b130dade78e82b45ff7","type":1,"bulletin":null,"deletedAt":null}
     */

    private String rid;
    private String m;
    private String k;
    private String v;
    private String desc;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 419eb9e7a964a8ea2a9ba43584eef937
         * code : 900052
         * name : 测试333
         * myDisplayName : null
         * join_direct : 0
         * is_join_via_pay : 0.00
         * join_amount : 0.00
         * is_view_each : 0
         * is_invite_each : 0
         * portraitUri : http://zhanghai.looklaw.cn/avatar/?name=%E6%B5%8B%E8%AF%95333
         * memberCount : 2
         * maxMemberCount : 500
         * creatorId : 7ef80afb69f80b130dade78e82b45ff7
         * type : 1
         * bulletin : null
         * deletedAt : null
         */

        private String id;
        private int code;
        private String name;
        private Object myDisplayName;
        private int join_direct;
        private String is_join_via_pay;
        private String join_amount;
        private int is_view_each;
        private int is_invite_each;
        private String portraitUri;
        private int memberCount;
        private int maxMemberCount;
        private String creatorId;
        private int type;
        private Object bulletin;
        private Object deletedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getMyDisplayName() {
            return myDisplayName;
        }

        public void setMyDisplayName(Object myDisplayName) {
            this.myDisplayName = myDisplayName;
        }

        public int getJoin_direct() {
            return join_direct;
        }

        public void setJoin_direct(int join_direct) {
            this.join_direct = join_direct;
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

        public int getIs_view_each() {
            return is_view_each;
        }

        public void setIs_view_each(int is_view_each) {
            this.is_view_each = is_view_each;
        }

        public int getIs_invite_each() {
            return is_invite_each;
        }

        public void setIs_invite_each(int is_invite_each) {
            this.is_invite_each = is_invite_each;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public int getMaxMemberCount() {
            return maxMemberCount;
        }

        public void setMaxMemberCount(int maxMemberCount) {
            this.maxMemberCount = maxMemberCount;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getBulletin() {
            return bulletin;
        }

        public void setBulletin(Object bulletin) {
            this.bulletin = bulletin;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }
    }
}
