package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/24.
 */

public class UploadGroupPicBean {

    /**
     * rid : xxyy
     * m : group
     * k : portrait
     * v : {"groupId":"28c8edde3d61a0411511d3b1866f0636","portraitUri":"http://xxx.com/xxx.jpg"}
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
         * groupId : 28c8edde3d61a0411511d3b1866f0636
         * portraitUri : http://xxx.com/xxx.jpg
         */

        private String groupId;
        private String portraitUri;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
