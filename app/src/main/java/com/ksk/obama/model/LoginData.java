package com.ksk.obama.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class LoginData {


    /**
     * result_stadus : SUCCESS
     * UserInfoId : 1
     * UserInfoName : 管理员1
     * ShopName : 总店
     * ShopId : 1
     * YdManagerId : 1
     * dbname : HuiYuanTest
     * Isswiping : 1
     * Author : [{"c_PopedomName":"POS:会员办卡","ROW_NUMBER":"1"},{"c_PopedomName":"POS:快速收银","ROW_NUMBER":"2"},{"c_PopedomName":"POS:会员充值","ROW_NUMBER":"3"},{"c_PopedomName":"POS:商品消费","ROW_NUMBER":"4"},{"c_PopedomName":"POS:积分兑换","ROW_NUMBER":"5"},{"c_PopedomName":"POS:购买次数","ROW_NUMBER":"6"},{"c_PopedomName":"POS:挂失补卡","ROW_NUMBER":"7"},{"c_PopedomName":"POS:会员到店","ROW_NUMBER":"8"},{"c_PopedomName":"POS:退卡销户","ROW_NUMBER":"9"},{"c_PopedomName":"POS:赠送/扣除积分","ROW_NUMBER":"10"},{"c_PopedomName":"POS:交班","ROW_NUMBER":"11"},{"c_PopedomName":"POS:卡片延期","ROW_NUMBER":"12"},{"c_PopedomName":"POS:开卡统计","ROW_NUMBER":"13"},{"c_PopedomName":"POS:消费统计","ROW_NUMBER":"14"},{"c_PopedomName":"POS:充值统计","ROW_NUMBER":"15"},{"c_PopedomName":"POS:兑换统计","ROW_NUMBER":"16"},{"c_PopedomName":"POS:购次统计","ROW_NUMBER":"17"},{"c_PopedomName":"POS:计次统计","ROW_NUMBER":"18"},{"c_PopedomName":"POS:退卡统计","ROW_NUMBER":"19"},{"c_PopedomName":"POS:今日收益","ROW_NUMBER":"20"},{"c_PopedomName":"POS:会员到店:修改密码","ROW_NUMBER":"21"},{"c_PopedomName":"POS:会员到店:修改资料","ROW_NUMBER":"22"},{"c_PopedomName":"POS:会员到店:通知设置","ROW_NUMBER":"23"},{"c_PopedomName":"POS:会员到店:微信绑定","ROW_NUMBER":"24"},{"c_PopedomName":"POS:会员到店:微信更改","ROW_NUMBER":"25"},{"c_PopedomName":"POS:会员到店:微信解绑","ROW_NUMBER":"26"}]
     * Set : {"CardSet":["非接会员卡","条码会员卡"],"PaySet":["现金","银联卡","微信","支付宝"],"IntegralSet":["快速消费积分"]}
     * MaxFastCostMoney : 100
     * SetHandCard : 1
     * PayMent: 1
     * QuitTime : 0
     * HandoverTime : 12:20:00
     * ShopIntegraltimes : 10
     * "n_IntegralTimes":"1.00","n_IntegralToMoney":"2.00","n_IntegralToMoneyMax":"10.00","n_IntegralToMoneyDefault":"50.00"
     * Module : [{"id":"1","i_GroupID":"1","c_ModuleName":"台房","t_StopTime":"2028-02-22","ROW_NUMBER":"1"}]
     */

    private String result_stadus;
    private String UserInfoId;
    private String UserInfoName;
    private String ShopName;
    private String ShopId;
    private String YdManagerId;
    private String dbname;
    private String Isswiping;
    private SetBean Set;
    private String MaxFastCostMoney;
    private String SetHandCard;
    private String QuitTime;
    private String Drawmenu;
    private String HandoverTime;
    private String ShopIntegraltimes;
    private String n_IntegralToMoney;
    private String n_IntegralToMoneyMax;
    private String n_IntegralToMoneyDefault;
    private String Rechargebutton;//是否开启快速充值
    private String RechargePoints;

    public String getPayMent() {
        return PayMent;
    }

    public void setPayMent(String payMent) {
        PayMent = payMent;
    }

    private String PayMent; // 是否使用wangpos官方支付接口  1|0

    private List<SetPaysBean> SetPays;
    private List<RechargefastBean> Rechargefast;

    public List<RechargefastBean> getRechargefast() {
        return Rechargefast;
    }

    public void setRechargefast(List<RechargefastBean> rechargefast) {
        Rechargefast = rechargefast;
    }

    private List<AuthorBean> Author;
    private List<ModuleBean> Module;


    public String getRechargePoints() {
        return RechargePoints;
    }

    public void setRechargePoints(String rechargePoints) {
        RechargePoints = rechargePoints;
    }

    public List<SetPaysBean> getSetPays() {
        return SetPays;
    }

    public void setSetPays(List<SetPaysBean> setPays) {
        this.SetPays = setPays;
    }

    public String getN_IntegralToMoney() {
        return n_IntegralToMoney;
    }

    public void setN_IntegralToMoney(String n_IntegralToMoney) {
        this.n_IntegralToMoney = n_IntegralToMoney;
    }

    public String getN_IntegralToMoneyMax() {
        return n_IntegralToMoneyMax;
    }

    public void setN_IntegralToMoneyMax(String n_IntegralToMoneyMax) {
        this.n_IntegralToMoneyMax = n_IntegralToMoneyMax;
    }

    public String getN_IntegralToMoneyDefault() {
        return n_IntegralToMoneyDefault;
    }

    public void setN_IntegralToMoneyDefault(String n_IntegralToMoneyDefault) {
        this.n_IntegralToMoneyDefault = n_IntegralToMoneyDefault;
    }

    public String getDrawmenu() {
        return Drawmenu;
    }

    public void setDrawmenu(String drawmenu) {
        Drawmenu = drawmenu;
    }

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public String getUserInfoId() {
        return UserInfoId;
    }

    public void setUserInfoId(String UserInfoId) {
        this.UserInfoId = UserInfoId;
    }

    public String getUserInfoName() {
        return UserInfoName;
    }

    public void setUserInfoName(String UserInfoName) {
        this.UserInfoName = UserInfoName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String ShopId) {
        this.ShopId = ShopId;
    }

    public String getYdManagerId() {
        return YdManagerId;
    }

    public void setYdManagerId(String YdManagerId) {
        this.YdManagerId = YdManagerId;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getIsswiping() {
        return Isswiping;
    }

    public void setIsswiping(String Isswiping) {
        this.Isswiping = Isswiping;
    }

    public SetBean getSet() {
        return Set;
    }

    public void setSet(SetBean Set) {
        this.Set = Set;
    }

    public String getMaxFastCostMoney() {
        return MaxFastCostMoney;
    }

    public void setMaxFastCostMoney(String MaxFastCostMoney) {
        this.MaxFastCostMoney = MaxFastCostMoney;
    }

    public String getSetHandCard() {
        return SetHandCard;
    }

    public void setSetHandCard(String SetHandCard) {
        this.SetHandCard = SetHandCard;
    }

    public String getQuitTime() {
        return QuitTime;
    }

    public void setQuitTime(String QuitTime) {
        this.QuitTime = QuitTime;
    }

    public String getHandoverTime() {
        return HandoverTime;
    }

    public void setHandoverTime(String HandoverTime) {
        this.HandoverTime = HandoverTime;
    }

    public String getShopIntegraltimes() {
        return ShopIntegraltimes;
    }

    public void setShopIntegraltimes(String ShopIntegraltimes) {
        this.ShopIntegraltimes = ShopIntegraltimes;
    }

    public List<AuthorBean> getAuthor() {
        return Author;
    }

    public void setAuthor(List<AuthorBean> Author) {
        this.Author = Author;
    }

    public List<ModuleBean> getModule() {
        return Module;
    }

    public void setModule(List<ModuleBean> Module) {
        this.Module = Module;
    }


    public static class RechargefastBean implements Parcelable {
        /**
         * "c_Value": "45"
         * "c_Remark": "5"
         * "ROW_NUMBER": "1"
         */
        private String c_Value;
        private String c_Remark;
        private String ROW_NUMBER;

        protected RechargefastBean(Parcel in) {
            c_Value = in.readString();
            c_Remark = in.readString();
            ROW_NUMBER = in.readString();
        }

        public static final Creator<RechargefastBean> CREATOR = new Creator<RechargefastBean>() {
            @Override
            public RechargefastBean createFromParcel(Parcel in) {
                return new RechargefastBean(in);
            }

            @Override
            public RechargefastBean[] newArray(int size) {
                return new RechargefastBean[size];
            }
        };

        public String getC_Value() {
            return c_Value;
        }

        public void setC_Value(String c_Value) {
            this.c_Value = c_Value;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

        public String getC_Remark() {
            return c_Remark;
        }

        public void setC_Remark(String c_Remark) {
            this.c_Remark = c_Remark;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(c_Value);
            dest.writeString(c_Remark);
            dest.writeString(ROW_NUMBER);
        }
    }


    public static class AuthorBean implements Parcelable {
        /**
         * c_PopedomName : POS:会员办卡
         * ROW_NUMBER : 1
         */

        private String c_PopedomName;
        private String ROW_NUMBER;

        protected AuthorBean(Parcel in) {
            c_PopedomName = in.readString();
            ROW_NUMBER = in.readString();
        }

        public static final Creator<AuthorBean> CREATOR = new Creator<AuthorBean>() {
            @Override
            public AuthorBean createFromParcel(Parcel in) {
                return new AuthorBean(in);
            }

            @Override
            public AuthorBean[] newArray(int size) {
                return new AuthorBean[size];
            }
        };

        public String getC_PopedomName() {
            return c_PopedomName;
        }

        public void setC_PopedomName(String c_PopedomName) {
            this.c_PopedomName = c_PopedomName;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(c_PopedomName);
            dest.writeString(ROW_NUMBER);
        }
    }

    public static class SetBean {
        private List<String> CardSet;
        private List<String> PaySet;
        private List<String> IntegralSet;

        public List<String> getCardSet() {
            return CardSet;
        }

        public void setCardSet(List<String> CardSet) {
            this.CardSet = CardSet;
        }

        public List<String> getPaySet() {
            return PaySet;
        }

        public void setPaySet(List<String> PaySet) {
            this.PaySet = PaySet;
        }

        public List<String> getIntegralSet() {
            return IntegralSet;
        }

        public void setIntegralSet(List<String> IntegralSet) {
            this.IntegralSet = IntegralSet;
        }
    }

    public static class ModuleBean implements Parcelable {
        /**
         * id : 1
         * i_GroupID : 1
         * c_ModuleName : 台房
         * t_StopTime : 2028-02-22
         * ROW_NUMBER : 1
         */

        private String id;
        private String i_GroupID;
        private String c_ModuleName;
        private String t_StopTime;
        private String ROW_NUMBER;

        protected ModuleBean(Parcel in) {
            id = in.readString();
            i_GroupID = in.readString();
            c_ModuleName = in.readString();
            t_StopTime = in.readString();
            ROW_NUMBER = in.readString();
        }

        public static final Creator<ModuleBean> CREATOR = new Creator<ModuleBean>() {
            @Override
            public ModuleBean createFromParcel(Parcel in) {
                return new ModuleBean(in);
            }

            @Override
            public ModuleBean[] newArray(int size) {
                return new ModuleBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getI_GroupID() {
            return i_GroupID;
        }

        public void setI_GroupID(String i_GroupID) {
            this.i_GroupID = i_GroupID;
        }

        public String getC_ModuleName() {
            return c_ModuleName;
        }

        public void setC_ModuleName(String c_ModuleName) {
            this.c_ModuleName = c_ModuleName;
        }

        public String getT_StopTime() {
            return t_StopTime;
        }

        public void setT_StopTime(String t_StopTime) {
            this.t_StopTime = t_StopTime;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(i_GroupID);
            dest.writeString(c_ModuleName);
            dest.writeString(t_StopTime);
            dest.writeString(ROW_NUMBER);
        }
    }

    public static class SetPaysBean implements Parcelable {
        /**
         * "c_Value": "FastWeChat"
         * "ROW_NUMBER": "1"
         */

        private String c_Value;
        private String ROW_NUMBER;

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

        public String getC_Value() {
            return c_Value;
        }

        public void setC_Value(String c_Value) {
            this.c_Value = c_Value;
        }

        protected SetPaysBean(Parcel in) {
            c_Value = in.readString();
            ROW_NUMBER = in.readString();
        }

        public static final Creator<SetPaysBean> CREATOR = new Creator<SetPaysBean>() {
            @Override
            public SetPaysBean createFromParcel(Parcel in) {
                return new SetPaysBean(in);
            }

            @Override
            public SetPaysBean[] newArray(int size) {
                return new SetPaysBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(c_Value);
            dest.writeString(ROW_NUMBER);
        }
    }
    public String getRechargebutton() {
        return Rechargebutton;
    }

    public void setRechargebutton(String rechargebutton) {
        Rechargebutton = rechargebutton;
    }
}
