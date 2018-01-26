package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/26.
 */

public class GroupInfoReturnBean {

    /**
     * rid : 1516935337771
     * m : group
     * k : info
     * v : ok
     * desc : 获取成功
     * data : {"id":"9132e3d4fb60295686ccdbcbe1ac3af8","code":900017,"name":"221111","portraitUri":"http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124202601.jpg","memberCount":2,"maxMemberCount":500,"creatorId":"6309d2d98922359173b9931e9110ed2c","type":1,"bulletin":null,"deletedAt":null}
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
         * id : 9132e3d4fb60295686ccdbcbe1ac3af8
         * code : 900017
         * name : 221111
         * portraitUri : http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124202601.jpg
         * memberCount : 2
         * maxMemberCount : 500
         * creatorId : 6309d2d98922359173b9931e9110ed2c
         * type : 1
         * bulletin : null
         * deletedAt : null
         */

        private String id;
        private int code;
        private String name;
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
