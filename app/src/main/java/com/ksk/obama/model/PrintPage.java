package com.ksk.obama.model;

/**
 * Created by Administrator on 2016/9/19.
 */
public class PrintPage {
    private String name;
    private String price;
    private String num;
    private String money;

    public PrintPage() {
    }

    public PrintPage(String name, String price, String num, String money) {
        this.name = name;
        this.price = price;
        this.num = num;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
