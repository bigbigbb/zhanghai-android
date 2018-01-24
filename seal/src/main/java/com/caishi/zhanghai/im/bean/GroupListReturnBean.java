package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by shihui on 2018/1/24.
 */

public class GroupListReturnBean {

    /**
     * rid : xxyy
     * m : group
     * k : all
     * data : [{"id":"sdf9sd0df98","name":"Team 1","portraitUri":"http://test.com/group/abc123.jpg","creatorId":"fgh89wefd9","memberCount":5},{"id":"fgh809fg098","name":"Team 2","portraitUri":"http://test.com/group/abc234.jpg","creatorId":"kl234klj234","memberCount":8}]
     */

    private String rid;
    private String m;
    private String k;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : sdf9sd0df98
         * name : Team 1
         * portraitUri : http://test.com/group/abc123.jpg
         * creatorId : fgh89wefd9
         * memberCount : 5
         */

        private String id;
        private String name;
        private String portraitUri;
        private String creatorId;
        private int memberCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }
    }
}
