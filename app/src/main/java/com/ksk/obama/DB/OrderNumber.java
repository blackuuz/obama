package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * Created by uuz on 2017/1/4.
 */

public class OrderNumber extends DataSupport {
    private int id;
    private String orderNumber; // 订单号
    private String groupId;// groupid
    private String dbName;//dbname
    private String userId;//userid
    //    private String c_Billfrom;//终端型号
//    private String EquipmentNum;//终端序列号
    private String cardNum;//卡号
    private String CardCode;//卡的uid
    private String CardName;//会员名
    private String sex;//性别
    private String mobile;//手机号
    private String birthday;//生日
    private String IDCard;//身份证
    private String car;//车号
    private String address;// 地址
    private String remarks;//备注
    private String recommendEmpoyee;//推荐员工
    private String password;//密码
    private String time;//时间

    private String money;//消费金额//实际金额
    private String shouldMoney;//应付金额
    private String delMoney;//抵现的金额
    private String payDecIntegral;//抵现的积分
    private String shopIntegral;//商品积分【拼接字符串】
    private String getIntegral;//获得积分
    private String memberid;//  会员id
    private String goodsId;//商品id【拼接字符串】
    private String num;//商品数量【拼接字符串】
    private String goodsMoney;//商品的价格【字符串拼接】

    private String payDiscounted;//抹零 商品界面
    private String payTicket;//优惠券金额
    private String couponId;//优惠券id
    private int payType;//支付类型

    private String costtime;//快速扣次/ 次数
    private String paySend;//赠送金额
    private String validTimes;//有效时间
    private String refernumber;//官方订单号
    private String temporary_num;//临时授权工号
    private String result_name;//临时授权姓名
    // private String gforder;//官方订单号
    private boolean isVip;//是否是会员
    private boolean isTemporary;//是否进行了临时授权
    private String formClazz;//来自哪个类
    private boolean isChecked = true;//是否被选中
    private  String getMoney;//购买次数里面的属性
    private boolean isQuery;//是否被查询

    public boolean isQuery() {
        return isQuery;
    }

    public void setQuery(boolean query) {
        isQuery = query;
    }

    public int getIsSucceedUploading() {
        return isSucceedUploading;
    }

    public void setIsSucceedUploading(int isSucceedUploading) {
        this.isSucceedUploading = isSucceedUploading;
    }

    private int isSucceedUploading ;//订单的状态  -1 为失败 1 为成功


    public String getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(String getMoney) {
        this.getMoney = getMoney;
    }

    public String getValidTimes() {
        return validTimes;
    }

    public void setValidTimes(String validTimes) {
        this.validTimes = validTimes;
    }


    public String getPaySend() {
        return paySend;
    }

    public void setPaySend(String paySend) {
        this.paySend = paySend;
    }

    private String goodsName;


    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecommendEmpoyee() {
        return recommendEmpoyee;
    }

    public void setRecommendEmpoyee(String recommendEmpoyee) {
        this.recommendEmpoyee = recommendEmpoyee;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String getCosttime() {
        return costtime;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }

    public String getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(String goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public String getShouldMoney() {
        return shouldMoney;
    }

    public void setShouldMoney(String shouldMoney) {
        this.shouldMoney = shouldMoney;
    }

    public String getshopIntegral() {
        return shopIntegral;
    }

    public void setshopIntegral(String shopIntegral) {
        this.shopIntegral = shopIntegral;
    }

    public String getGetIntegral() {
        return getIntegral;
    }

    public void setGetIntegral(String getIntegral) {
        this.getIntegral = getIntegral;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPayDiscounted() {
        return payDiscounted;
    }

    public void setPayDiscounted(String payDiscounted) {
        this.payDiscounted = payDiscounted;
    }

    public String getPayTicket() {
        return payTicket;
    }

    public void setPayTicket(String payTicket) {
        this.payTicket = payTicket;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getFormClazz() {
        return formClazz;
    }

    public void setFormClazz(String formClazz) {
        this.formClazz = formClazz;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public void setTemporary(boolean temporary) {
        isTemporary = temporary;
    }

//    public String getGforder() {
//        return gforder;
//    }
//
//    public void setGforder(String gforder) {
//        this.gforder = gforder;
//    }
    //    public String getC_Billfrom() {
//        return c_Billfrom;
//    }
//
//    public void setC_Billfrom(String c_Billfrom) {
//        this.c_Billfrom = c_Billfrom;
//    }
//
//    public String getEquipmentNum() {
//        return EquipmentNum;
//    }
//
//    public void setEquipmentNum(String equipmentNum) {
//        EquipmentNum = equipmentNum;
//    }

    public String getCardCode() {
        return CardCode;
    }

    public void setCardCode(String cardCode) {
        CardCode = cardCode;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayDecIntegral() {
        return payDecIntegral;
    }

    public void setPayDecIntegral(String payDecIntegral) {
        this.payDecIntegral = payDecIntegral;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getRefernumber() {
        return refernumber;
    }

    public void setRefernumber(String refernumber) {
        this.refernumber = refernumber;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getTemporary_num() {
        return temporary_num;
    }

    public void setTemporary_num(String temporary_num) {
        this.temporary_num = temporary_num;
    }

    public String getResult_name() {
        return result_name;
    }

    public void setResult_name(String result_name) {
        this.result_name = result_name;
    }

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
                ", haveMoney='" + money + '\'' +
                ", payDecIntegral='" + payDecIntegral + '\'' +
                ", groupId='" + groupId + '\'' +
                ", dbName='" + dbName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
