package com.ksk.obama.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/17.
 */

public class OpenCardInfo implements Parcelable{
    private String cardnum;
    private String name;
    private String payshould;
    private String paymoney;
    private String goodsId;
    private String str0;
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String sex;
    private String phone;
    private String birthday;
    private String password;
    private String cardName;
    private String price;
    private String uid;

    private String vipAddId;
    private String vipAddPerson;
    private String addId;
    private String addPerson;

    public String getVipAddPerson() {
        return vipAddPerson;
    }

    public void setVipAddPerson(String vipAddPerson) {
        this.vipAddPerson = vipAddPerson;
    }

    public String getVipAddId() {
        return vipAddId;
    }

    public void setVipAddId(String vipAddId) {
        this.vipAddId = vipAddId;
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

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson;
    }

    public static Creator<OpenCardInfo> getCREATOR() {
        return CREATOR;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public OpenCardInfo() {
    }

    protected OpenCardInfo(Parcel in) {
        cardnum = in.readString();
        name = in.readString();
        payshould = in.readString();
        paymoney = in.readString();
        goodsId = in.readString();
        str0 = in.readString();
        str1 = in.readString();
        str2 = in.readString();
        str3 = in.readString();
        str4 = in.readString();
        sex = in.readString();
        phone = in.readString();
        birthday = in.readString();
        password = in.readString();
        cardName = in.readString();
        price = in.readString();
        uid = in.readString();
        addId = in.readString();
        addPerson = in.readString();
        vipAddId = in.readString();
        vipAddPerson = in.readString();
    }

    public static final Creator<OpenCardInfo> CREATOR = new Creator<OpenCardInfo>() {
        @Override
        public OpenCardInfo createFromParcel(Parcel in) {
            return new OpenCardInfo(in);
        }

        @Override
        public OpenCardInfo[] newArray(int size) {
            return new OpenCardInfo[size];
        }
    };

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayshould() {
        return payshould;
    }

    public void setPayshould(String payshould) {
        this.payshould = payshould;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getStr0() {
        return str0;
    }

    public void setStr0(String str0) {
        this.str0 = str0;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardnum);
        dest.writeString(name);
        dest.writeString(payshould);
        dest.writeString(paymoney);
        dest.writeString(goodsId);
        dest.writeString(str0);
        dest.writeString(str1);
        dest.writeString(str2);
        dest.writeString(str3);
        dest.writeString(str4);
        dest.writeString(sex);
        dest.writeString(phone);
        dest.writeString(birthday);
        dest.writeString(password);
        dest.writeString(cardName);
        dest.writeString(price);
        dest.writeString(uid);
        dest.writeString(addId);
        dest.writeString(addPerson);
        dest.writeString(vipAddId);
        dest.writeString(vipAddPerson);
    }
}
