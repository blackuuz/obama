package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/10/11.
 */

public class QuickDelMoney extends DataSupport {

    private int id;
    private String cardNum;
    private String cardName;
    private String dbName;
    private String user_Id;
    private String equipmentNum;
    private String money;
    private String refernumber;
    private int n;
    private String url;
    private String url1;
    private String orderTime;
    private String orderNo;
    private String userName;
    private String shopName;
    private boolean isVip;
    private String haveMoney;
    private String haveIntegral;
    private String getIntegral;
    private String uid;

    public String getDmoney() {
        return dmoney;
    }

    public void setDmoney(String dmoney) {
        this.dmoney = dmoney;
    }

    private String dmoney;
    private String delMoney;
    private String delIntegral;
    private String tem;
    private String temName;
    private boolean isTem;


    public String getTemName() {
        return temName;
    }

    public void setTemName(String temName) {
        this.temName = temName;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public boolean getIsTem() {
        return isTem;
    }

    public void setIsTem(boolean isTem) {
        this.isTem = isTem;
    }

    public String getDelIntegral() {
        return delIntegral;
    }

    public void setDelIntegral(String delIntegral) {
        this.delIntegral = delIntegral;
    }

    public String getDelMoney() {
        return delMoney;
    }

    public void setDelMoney(String delMoney) {
        this.delMoney = delMoney;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGetIntegral() {
        return getIntegral;
    }

    public void setGetIntegral(String getIntegral) {
        this.getIntegral = getIntegral;
    }

    public String getHaveMoney() {
        return haveMoney;
    }

    public void setHaveMoney(String haveMoney) {
        this.haveMoney = haveMoney;
    }

    public String getHaveIntegral() {
        return haveIntegral;
    }

    public void setHaveIntegral(String haveIntegral) {
        this.haveIntegral = haveIntegral;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRefernumber() {
        return refernumber;
    }

    public void setRefernumber(String refernumber) {
        this.refernumber = refernumber;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
