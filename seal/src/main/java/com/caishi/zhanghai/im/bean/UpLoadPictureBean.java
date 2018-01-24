package com.caishi.zhanghai.im.bean;

/**
 * Created by yusy on 2018/1/3.
 */

public class UpLoadPictureBean {

    /**
     * rid : xxyy
     * m : member
     * k : portrait
     * v : {"imgbase64":"base64(图片内容)"}
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
         * imgbase64 : base64(图片内容)
         */

        private String portraitUri;


        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
