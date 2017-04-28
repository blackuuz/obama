package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * Created by djy on 2017/1/4.
 */

public class OrderNumber extends DataSupport {
    private int id;
    private String cardNum;
    private String time;
    private String orderNumber;
    private String delMoney;
    private String haveMoney;
    private String haveIntegral;
    private String getIntegral;
    private String groupId;
    private String dbName;
    private String userId;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDelMoney() {
        return delMoney;
    }

    public void setDelMoney(String delMoney) {
        this.delMoney = delMoney;
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

    public String getGetIntegral() {
        return getIntegral;
    }

    public void setGetIntegral(String getIntegral) {
        this.getIntegral = getIntegral;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "OrderNumber{" +
                "id=" + id +
                ", cardNum='" + cardNum + '\'' +
                ", time='" + time + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", delMoney='" + delMoney + '\'' +
                ", haveMoney='" + haveMoney + '\'' +
                ", haveIntegral='" + haveIntegral + '\'' +
                ", getIntegral='" + getIntegral + '\'' +
                ", groupId='" + groupId + '\'' +
                ", dbName='" + dbName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
