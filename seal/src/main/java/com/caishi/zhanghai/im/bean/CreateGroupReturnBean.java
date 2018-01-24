package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/16.
 */

public class CreateGroupReturnBean {

    /**
     * rid : xxyy
     * m : group
     * k : create
     * v : ok
     * desc : 创建成功
     * data : {"groupId":"e10adc3949ba59abbe56e057f20f883e","groupName":"吃喝玩乐群"}
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
         * groupId : e10adc3949ba59abbe56e057f20f883e
         * groupName : 吃喝玩乐群
         */

        private String groupId;
        private String groupName;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }
}
