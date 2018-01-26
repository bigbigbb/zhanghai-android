package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by shihui on 2018/1/26.
 */

public class SearchGroupReturnBean {

    /**
     * rid : 1516951644776
     * m : group
     * k : search
     * v : ok
     * desc : 搜索成功
     * data : [{"id":"3cb3233335c714e079c516ee94999c5e","name":"吃喝玩乐群","portraitUri":"http://zhanghai.looklaw.cn/upavatar/3cb3233335c714e079c516ee94999c5e_20180123202629.jpg","creatorId":"cc30d023795105213b43f973e2270275","creatorName":"付小涛5805","memberCount":8,"code":900002}]
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
         * id : 3cb3233335c714e079c516ee94999c5e
         * name : 吃喝玩乐群
         * portraitUri : http://zhanghai.looklaw.cn/upavatar/3cb3233335c714e079c516ee94999c5e_20180123202629.jpg
         * creatorId : cc30d023795105213b43f973e2270275
         * creatorName : 付小涛5805
         * memberCount : 8
         * code : 900002
         */

        private String id;
        private String name;
        private String portraitUri;
        private String creatorId;
        private String creatorName;
        private int memberCount;
        private int code;

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

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
