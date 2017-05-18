package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/10/25.
 */

public class OpenCardDb extends DataSupport {

    private int id;
    private int n;
    private String url;
    private String cardNO;
    private String memberName;
    private String sex;
    private String mobile;
    private String orderNo;
    private String birthdayYear;
    private String birthdayMonth;
    private String birthdayDay;
    private String classID;
    private String userID;
    private String payActual;
    private String EquipmentNum;
    private String Remark;
    private String dbName;
    private String userName;
    private String shopName;
    private String orderTime;
    private String cardType;
    private String chushimoney;
    private String chushijf;
    private String cardMoney;
    private String refernumber;
    private String uid;
    private String addId;
    private String addName;

    private String payShould;
    private boolean isTem;
    private String temporaryNum;
    private String temName;




    public boolean isTem() {
        return isTem;
    }

    public void setTem(boolean tem) {
        isTem = tem;
    }
    public String getPayShould() {
        return payShould;
    }

    public void setPayShould(String payShould) {
        this.payShould = payShould;
    }

    public String getTemporaryNum() {
        return temporaryNum;
    }

    public void setTemporaryNum(String temporaryNum) {
        this.temporaryNum = temporaryNum;
    }

    public String getTemName() {
        return temName;
    }

    public void setTemName(String temName) {
        this.temName = temName;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId;
    }

    public String getAddName() {
        return addName;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(String birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(String birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPayActual() {
        return payActual;
    }

    public void setPayActual(String payActual) {
        this.payActual = payActual;
    }

    public String getEquipmentNum() {
        return EquipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        EquipmentNum = equipmentNum;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getChushimoney() {
        return chushimoney;
    }

    public void setChushimoney(String chushimoney) {
        this.chushimoney = chushimoney;
    }

    public String getChushijf() {
        return chushijf;
    }

    public void setChushijf(String chushijf) {
        this.chushijf = chushijf;
    }

    public String getCardMoney() {
        return cardMoney;
    }

    public void setCardMoney(String cardMoney) {
        this.cardMoney = cardMoney;
    }

    public String getRefernumber() {
        return refernumber;
    }

    public void setRefernumber(String refernumber) {
        this.refernumber = refernumber;
    }
}
