package com.ksk.obama.model;

/**
 * Created by uuz on 2017/6/16.
 */

/**
 * "mname": "可视卡测试",
 * "en": " ca 47 22 c7",
 * "snCode":"P327701601014594"
 * "mcode": "193394",
 * "loginType": 0,
 * "deviceType": "2",
 * "name": "匿名用户"
 */
public class WangPos {
    private String mname;
    private String mcode;
    private String en;
    private String loginType;
    private String deviceType;
    private String name;
    private String snCode;

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
