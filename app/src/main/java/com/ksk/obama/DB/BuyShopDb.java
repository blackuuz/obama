package com.ksk.obama.DB;

import com.ksk.obama.model.BuyCount;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/25.
 */

public class BuyShopDb extends DataSupport {

    private int id;
    private int n;
    private String url;
    private boolean isVip;
    private String goods_id;
    private String num;
    private String money;
    private String integral;
    private String Member_Id;
    private String User_Id;
    private String get_integral;
    private String EquipmentNum;
    private String PayShould;
    private String PayActual;
    private String PayDiscounted;
    private String orderNo;
    private String dbName;
    private String orderTime;
    private String shopName;
    private String cardNum;
    private String name;
    private String userName;
    private String havejf;
    private String haveMoney;
    private String youhui;
    private String delkq;
    private String deljf;
    private String oldMoney;
    private String oldIntegral;
    private String order_again;
    private String uid;
    private List<BuyCount> dataList=new ArrayList<>();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrder_again() {
        return order_again;
    }

    public void setOrder_again(String order_again) {
        this.order_again = order_again;
    }

    public String getOldMoney() {
        return oldMoney;
    }

    public void setOldMoney(String oldMoney) {
        this.oldMoney = oldMoney;
    }

    public String getOldIntegral() {
        return oldIntegral;
    }

    public void setOldIntegral(String oldIntegral) {
        this.oldIntegral = oldIntegral;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getPayDiscounted() {
        return PayDiscounted;
    }

    public void setPayDiscounted(String payDiscounted) {
        PayDiscounted = payDiscounted;
    }

    public String getDelkq() {
        return delkq;
    }

    public void setDelkq(String delkq) {
        this.delkq = delkq;
    }

    public String getDeljf() {
        return deljf;
    }

    public void setDeljf(String deljf) {
        this.deljf = deljf;
    }

    public List<BuyCount> getDataList() {
        return dataList;
    }

    public void setDataList(List<BuyCount> dataList) {
        this.dataList = dataList;
    }

    public String getYouhui() {
        return youhui;
    }

    public void setYouhui(String youhui) {
        this.youhui = youhui;
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

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getGet_integral() {
        return get_integral;
    }

    public void setGet_integral(String get_integral) {
        this.get_integral = get_integral;
    }

    public String getEquipmentNum() {
        return EquipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        EquipmentNum = equipmentNum;
    }

    public String getPayShould() {
        return PayShould;
    }

    public void setPayShould(String payShould) {
        PayShould = payShould;
    }

    public String getPayActual() {
        return PayActual;
    }

    public void setPayActual(String payActual) {
        PayActual = payActual;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHavejf() {
        return havejf;
    }

    public void setHavejf(String havejf) {
        this.havejf = havejf;
    }

    public String getHaveMoney() {
        return haveMoney;
    }

    public void setHaveMoney(String haveMoney) {
        this.haveMoney = haveMoney;
    }
}
