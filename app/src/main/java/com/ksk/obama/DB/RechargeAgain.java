package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/10/19.
 */

public class RechargeAgain extends DataSupport {
    private int id;

    public String getResult_name() {
        return result_name;
    }

    public void setResult_name(String result_name) {
        this.result_name = result_name;
    }

    private String result_name;
    private String url;
    private String userID;
    private String dbName;
    private String cardNO;
    private String payActual;
    private String payShould;
    private int n;
    private String userName;
    private String shopName;
    private String vipName;
    private String cardType;
    private String orderNo;
    private String oldMoney;
    private String gread;
    private String orderTime;
    private String equipmentNum;
    private String refernumber;
    private String uid;
    private String paySend;

    public String getPaySend() {
        return paySend;
    }

    public void setPaySend(String paySend) {
        this.paySend = paySend;
    }

    public String getGetIntegral() {
        return GetIntegral;
    }

    public void setGetIntegral(String getIntegral) {
        GetIntegral = getIntegral;
    }

    private String GetIntegral;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOldMoney() {
        return oldMoney;
    }

    public void setOldMoney(String oldMoney) {
        this.oldMoney = oldMoney;
    }

    public String getGread() {
        return gread;
    }

    public void setGread(String gread) {
        this.gread = gread;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getPayActual() {
        return payActual;
    }

    public void setPayActual(String payActual) {
        this.payActual = payActual;
    }

    public String getPayShould() {
        return payShould;
    }

    public void setPayShould(String payShould) {
        this.payShould = payShould;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getRefernumber() {
        return refernumber;
    }

    public void setRefernumber(String refernumber) {
        this.refernumber = refernumber;
    }
}
