package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by shihui on 2018/1/26.
 */

public class GroupMembersReturnBean {

    /**
     * rid : 1516937890799
     * m : group
     * k : members
     * v : ok
     * desc : 获取成功
     * data : [{"displayName":"Yusy","role":1,"createdAt":"2017-12-30T17:47:18.000Z","updatedAt":"2018-01-26T11:37:58.000Z","user":{"id":"6309d2d98922359173b9931e9110ed2c","nickname":"Yusy","portraitUri":"http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124203042.jpg"}},{"displayName":"咯1发天津","role":0,"createdAt":"2017-12-30T17:46:00.000Z","updatedAt":"2018-01-16T19:49:18.000Z","user":{"id":"d59630e27081796fc1ab652e82706304","nickname":"咯1发天津","portraitUri":"http://zhanghai.looklaw.cn/upavatar/d59630e27081796fc1ab652e82706304_20180111140448.jpg"}}]
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
         * displayName : Yusy
         * role : 1
         * createdAt : 2017-12-30T17:47:18.000Z
         * updatedAt : 2018-01-26T11:37:58.000Z
         * user : {"id":"6309d2d98922359173b9931e9110ed2c","nickname":"Yusy","portraitUri":"http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124203042.jpg"}
         */

        private String displayName;
        private int role;
        private String createdAt;
        private String updatedAt;
        private UserBean user;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 6309d2d98922359173b9931e9110ed2c
             * nickname : Yusy
             * portraitUri : http://zhanghai.looklaw.cn/upavatar/6309d2d98922359173b9931e9110ed2c_20180124203042.jpg
             */

            private String id;
            private String nickname;
            private String portraitUri;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
        }
    }
}
