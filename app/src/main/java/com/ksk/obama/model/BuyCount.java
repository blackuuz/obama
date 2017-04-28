package com.ksk.obama.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.DB.BuyShopDb;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/8/31.
 */
public class BuyCount extends DataSupport implements Parcelable {
    private int id;
    private String id1;
    private String name;
    private float num = 1;
    private float price;
    private float money;
    private float dazhe;
    private float jifen;
    private float moneyin;
    private String validTime;
    private float dis;
    private float integ;
    private BuyCountDb buyCountDb;
    private BuyShopDb buyShopDb;

    public BuyCountDb getBuyCountDb() {
        return buyCountDb;
    }

    public void setBuyCountDb(BuyCountDb buyCountDb) {
        this.buyCountDb = buyCountDb;
    }

    public BuyShopDb getBuyShopDb() {
        return buyShopDb;
    }

    public void setBuyShopDb(BuyShopDb buyShopDb) {
        this.buyShopDb = buyShopDb;
    }

    protected BuyCount(Parcel in) {
        id1 = in.readString();
        name = in.readString();
        num = in.readFloat();
        price = in.readFloat();
        money = in.readFloat();
        dazhe = in.readFloat();
        jifen = in.readFloat();
        moneyin = in.readFloat();
        validTime = in.readString();
        dis = in.readFloat();
        integ = in.readFloat();
    }

    public static final Creator<BuyCount> CREATOR = new Creator<BuyCount>() {
        @Override
        public BuyCount createFromParcel(Parcel in) {
            return new BuyCount(in);
        }

        @Override
        public BuyCount[] newArray(int size) {
            return new BuyCount[size];
        }
    };


    public void setId(int id) {
        this.id = id;
    }

    public float getDis() {
        return dis;
    }

    public void setDis(float dis) {
        this.dis = dis;
    }

    public float getInteg() {
        return integ;
    }

    public void setInteg(float integ) {
        this.integ = integ;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public BuyCount() {
    }

    public float getMoneyin() {
        return moneyin;
    }

    public void setMoneyin(float moneyin) {
        this.moneyin = moneyin;
    }

    public String getId() {
        return id1;
    }

    public void setId(String id) {
        this.id1 = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getDazhe() {
        return dazhe;
    }

    public void setDazhe(float dazhe) {
        this.dazhe = dazhe;
    }

    public float getJifen() {
        return jifen;
    }

    public void setJifen(float jifen) {
        this.jifen = jifen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id1);
        dest.writeString(name);
        dest.writeFloat(num);
        dest.writeFloat(price);
        dest.writeFloat(money);
        dest.writeFloat(dazhe);
        dest.writeFloat(jifen);
        dest.writeFloat(moneyin);
        dest.writeString(validTime);
        dest.writeFloat(dis);
        dest.writeFloat(integ);
    }

}
