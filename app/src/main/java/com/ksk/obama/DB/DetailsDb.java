package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/9/1 17:33
 * 修改人：Administrator
 * 修改时间：2017/9/1 17:33
 * 修改备注：
 */
public class DetailsDb extends DataSupport {
    private int id;
    private String name;
    private float  price;
    private float  num;
    private float  money;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
