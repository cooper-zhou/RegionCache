package com.aervon.app.regioncacheproject;

import java.io.Serializable;

/**
 * Author: alvin.zhou
 * Desc: com.aervon.app.regioncacheproject
 * Date: 2018/10/9
 */


public class RegionData implements Serializable {

    private int code = 200;

    private String msg = "Hello RegionCache !";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
