package com.ksk.obama.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/19 14:05
 * 修改人：Administrator
 * 修改时间：2017/7/19 14:05
 * 修改备注：
 */
public class GoodsStock extends DataSupport implements Parcelable {

    private int id;
    private String id1;
    private String name;
    private String goodsNO;//商品编号
    private float num = 1;
    private float price;//单价
    private float actualPrice;//修改后的单价
    private String goodsModel;//商品模式
    private float money;//总价

    public GoodsStock() {
    }

    protected GoodsStock(Parcel in) {
        id = in.readInt();
        id1 = in.readString();
        name = in.readString();
        num = in.readFloat();
        price = in.readFloat();
        goodsModel = in.readString();
        money = in.readFloat();
        actualPrice = in.readFloat();
        goodsNO = in.readString();
    }

    public static final Creator<GoodsStock> CREATOR = new Creator<GoodsStock>() {
        @Override
        public GoodsStock createFromParcel(Parcel in) {
            return new GoodsStock(in);
        }

        @Override
        public GoodsStock[] newArray(int size) {
            return new GoodsStock[size];
        }
    };

//    public int getId() {
//        return id;
//    }

    public String getGoodsNO() {
        return goodsNO;
    }

    public void setGoodsNO(String goodsNO) {
        this.goodsNO = goodsNO;
    }

    public float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(float actualPrice) {
        this.actualPrice = actualPrice;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getId() {
        return id1;
    }

    public void setId(String id1) {
        this.id1 = id1;
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

    public String getGoodsModel() {
        return goodsModel;
    }

    public void setGoodsModel(String goodsModel) {
        this.goodsModel = goodsModel;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(id1);
        dest.writeString(name);
        dest.writeFloat(num);
        dest.writeFloat(price);
        dest.writeString(goodsModel);
        dest.writeFloat(money);
        dest.writeFloat(actualPrice);
        dest.writeString(goodsNO);
    }
}
