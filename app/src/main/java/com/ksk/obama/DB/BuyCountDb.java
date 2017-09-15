package com.ksk.obama.DB;

import com.ksk.obama.model.BuyCount;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BuyCountDb extends DataSupport {
    private int id;
    private int n;
    private String url;
    private String cardNo;
    private String dbname;
    private String cardName;
    private String userid;
    private String userName;
    private String shopName;
    private String memid;
    private String goods_id;
    private String times;
    private String orderTime;
    private String payshould;
    private String payactual;
    private String orderNo;
    private String get_money;
    private String youhui;
    private String haveMoney;
    private String equipmentNum;
    private String havejf;
    private String getIntegral;
    private String refernumber;
    private String PayDiscounted;
    private String validTimes;
    private String uid;
    private String dx_Integral;
    private String dx_Money;
    private String temporaryNum;
    private String temName;

    private List<BuyCount> dataList=new ArrayList<>();

    public List<BuyCount> getDataList() {
        return dataList;
    }

    public void setDataList(List<BuyCount> dataList) {
        this.dataList = dataList;
    }





    public String getDx_Integral() {
        return dx_Integral;
    }

    public void setDx_Integral(String dx_Integral) {
        this.dx_Integral = dx_Integral;
    }

    public String getDx_Money() {
        return dx_Money;
    }

    public void setDx_Money(String dx_Money) {
        this.dx_Money = dx_Money;
    }

    public String getTemName() {
        return temName;
    }

    public void setTemName(String temName) {
        this.temName = temName;
    }

    public String getTemporaryNum() {
        return temporaryNum;
    }

    public void setTemporaryNum(String temporaryNum) {
        this.temporaryNum = temporaryNum;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPayDiscounted() {
        return PayDiscounted;
    }

    public void setPayDiscounted(String payDiscounted) {
        PayDiscounted = payDiscounted;
    }

    public String getGetIntegral() {
        return getIntegral;
    }

    public void setGetIntegral(String getIntegral) {
        this.getIntegral = getIntegral;
    }

    public String getValidTimes() {
        return validTimes;
    }

    public void setValidTimes(String validTimes) {
        this.validTimes = validTimes;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getMemid() {
        return memid;
    }

    public void setMemid(String memid) {
        this.memid = memid;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayshould() {
        return payshould;
    }

    public void setPayshould(String payshould) {
        this.payshould = payshould;
    }

    public String getPayactual() {
        return payactual;
    }

    public void setPayactual(String payactual) {
        this.payactual = payactual;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGet_money() {
        return get_money;
    }

    public void setGet_money(String get_money) {
        this.get_money = get_money;
    }

    public String getYouhui() {
        return youhui;
    }

    public void setYouhui(String youhui) {
        this.youhui = youhui;
    }

    public String getHaveMoney() {
        return haveMoney;
    }

    public void setHaveMoney(String haveMoney) {
        this.haveMoney = haveMoney;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public String getHavejf() {
        return havejf;
    }

    public void setHavejf(String havejf) {
        this.havejf = havejf;
    }



    public String getRefernumber() {
        return refernumber;
    }

    public void setRefernumber(String refernumber) {
        this.refernumber = refernumber;
    }


}
