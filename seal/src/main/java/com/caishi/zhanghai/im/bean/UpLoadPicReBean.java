package com.caishi.zhanghai.im.bean;

/**
 * Created by shihui on 2018/1/24.
 */

public class UpLoadPicReBean {

    /**
     * code : 200
     * msg : 上传成功
     * data : {"portraitUri":"http://zhanghai.looklaw.cn/upavatar/cc30d023795105213b43f973e2270275_20180112232139.jpg"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * portraitUri : http://zhanghai.looklaw.cn/upavatar/cc30d023795105213b43f973e2270275_20180112232139.jpg
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
