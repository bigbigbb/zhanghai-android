package com.caishi.zhanghai.im.bean;

import java.util.List;

/**
 * Created by shihui on 2018/1/26.
 */

public class SearchGroupReturnBean {


    /**
     * rid : 1517225383266
     * m : group
     * k : search
     * v : ok
     * desc : 搜索成功
     * data : [{"id":"6cc09500d486a77f6e2e985ead29393c","name":"11111111","join_amount":"0.00","join_direct":1,"portraitUri":"http://zhanghai.looklaw.cn/upavatar/c32eba8ccc56db9a4e69dc921d8a6854_20180124195645.jpg","creatorId":"c32eba8ccc56db9a4e69dc921d8a6854","creatorName":"Cherish","memberCount":3,"code":900008},{"id":"2ab8fbb3ba95ddba3cdeeeaa08f9b401","name":"1111111","join_amount":"10.00","join_direct":1,"portraitUri":null,"creatorId":"6309d2d98922359173b9931e9110ed2c","creatorName":"Yusy","memberCount":2,"code":900009},{"id":"4d99688e610484e922a8c230f8dffd21","name":"1111111111","join_amount":"10.00","join_direct":1,"portraitUri":null,"creatorId":"6309d2d98922359173b9931e9110ed2c","creatorName":"Yusy","memberCount":3,"code":900032}]
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
         * id : 6cc09500d486a77f6e2e985ead29393c
         * name : 11111111
         * join_amount : 0.00
         * join_direct : 1
         * portraitUri : http://zhanghai.looklaw.cn/upavatar/c32eba8ccc56db9a4e69dc921d8a6854_20180124195645.jpg
         * creatorId : c32eba8ccc56db9a4e69dc921d8a6854
         * creatorName : Cherish
         * memberCount : 3
         * code : 900008
         */

        private String id;
        private String name;
        private String join_amount;
        private int join_direct;
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

        public String getJoin_amount() {
            return join_amount;
        }

        public void setJoin_amount(String join_amount) {
            this.join_amount = join_amount;
        }

        public int getJoin_direct() {
            return join_direct;
        }

        public void setJoin_direct(int join_direct) {
            this.join_direct = join_direct;
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
