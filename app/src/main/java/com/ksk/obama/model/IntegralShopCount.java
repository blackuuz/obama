package com.ksk.obama.model;

/**
 * Created by Administrator on 2016/9/5.
 */
public class IntegralShopCount{
    private String id;
    private String name;
    private float num = 0f;
    private float inregral;
    private String goodsId;
    private float count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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

    public float getInregral() {
        return inregral;
    }

    public void setInregral(float inregral) {
        this.inregral = inregral;
    }

}
