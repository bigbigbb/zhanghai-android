package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/26.
 */

public class SearchGroupBean {

    /**
     * rid : xxyy
     * m : group
     * k : search
     * v : {"type":"name","searchkey":"喝"}
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
         * type : name
         * searchkey : 喝
         */

        private String type;
        private String searchkey;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSearchkey() {
            return searchkey;
        }

        public void setSearchkey(String searchkey) {
            this.searchkey = searchkey;
        }
    }
}
