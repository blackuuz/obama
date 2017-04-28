package com.ksk.obama.model;

import java.util.List;

/**
 * Created by djy on 2017/2/22.
 */

public class HouseNo {

    /**
     * result_data : {"Room":[{"id":"1","i_GroupID":"1","i_ShopID":"1","c_RoomNo":"001","c_RoomName":"房间01","c_RoomType":"包房","c_RoomState":"空闲","c_RoomRemark":"","n_minimum_consumption":"0","n_DefaultMoney":"0","c_PayMode":"","n_Deposit":"0","n_customerS":"0","n_CustomersNow":"0","c_Team":"","t_BeginTime":"2017-02-20 14:15:15.967","ROW_NUMBER":"1"},{"id":"3","i_GroupID":"1","i_ShopID":"1","c_RoomNo":"003","c_RoomName":"房间03","c_RoomType":"包房","c_RoomState":"空闲","c_RoomRemark":"","n_minimum_consumption":"0","n_DefaultMoney":"0","c_PayMode":"","n_Deposit":"0","n_customerS":"0","n_CustomersNow":"0","c_Team":"","t_BeginTime":"2017-02-22 09:55:48.773","ROW_NUMBER":"2"}]}
     * result_stadus : SUCCESS
     */

    private ResultDataBean result_data;
    private String result_stadus;

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public static class ResultDataBean {
        private List<RoomBean> Room;

        public List<RoomBean> getRoom() {
            return Room;
        }

        public void setRoom(List<RoomBean> Room) {
            this.Room = Room;
        }

        public static class RoomBean {
            /**
             * id : 1
             * i_GroupID : 1
             * i_ShopID : 1
             * c_RoomNo : 001
             * c_RoomName : 房间01
             * c_RoomType : 包房
             * c_RoomState : 空闲
             * c_RoomRemark :
             * n_minimum_consumption : 0
             * n_DefaultMoney : 0
             * c_PayMode :
             * n_Deposit : 0
             * n_customerS : 0
             * n_CustomersNow : 0
             * c_Team :
             * t_BeginTime : 2017-02-20 14:15:15.967
             * ROW_NUMBER : 1
             */

            private String id;
            private String i_GroupID;
            private String i_ShopID;
            private String c_RoomNo;
            private String c_RoomName;
            private String c_RoomType;
            private String c_RoomState;
            private String c_RoomRemark;
            private String n_minimum_consumption;
            private String n_DefaultMoney;
            private String c_PayMode;
            private String n_Deposit;
            private String n_customerS;
            private String n_CustomersNow;
            private String c_Team;
            private String t_BeginTime;
            private boolean isSelect = false;
            private String ROW_NUMBER;

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

            public String getI_ShopID() {
                return i_ShopID;
            }

            public void setI_ShopID(String i_ShopID) {
                this.i_ShopID = i_ShopID;
            }

            public String getC_RoomNo() {
                return c_RoomNo;
            }

            public void setC_RoomNo(String c_RoomNo) {
                this.c_RoomNo = c_RoomNo;
            }

            public String getC_RoomName() {
                return c_RoomName;
            }

            public void setC_RoomName(String c_RoomName) {
                this.c_RoomName = c_RoomName;
            }

            public String getC_RoomType() {
                return c_RoomType;
            }

            public void setC_RoomType(String c_RoomType) {
                this.c_RoomType = c_RoomType;
            }

            public String getC_RoomState() {
                return c_RoomState;
            }

            public void setC_RoomState(String c_RoomState) {
                this.c_RoomState = c_RoomState;
            }

            public String getC_RoomRemark() {
                return c_RoomRemark;
            }

            public void setC_RoomRemark(String c_RoomRemark) {
                this.c_RoomRemark = c_RoomRemark;
            }

            public String getN_minimum_consumption() {
                return n_minimum_consumption;
            }

            public void setN_minimum_consumption(String n_minimum_consumption) {
                this.n_minimum_consumption = n_minimum_consumption;
            }

            public String getN_DefaultMoney() {
                return n_DefaultMoney;
            }

            public void setN_DefaultMoney(String n_DefaultMoney) {
                this.n_DefaultMoney = n_DefaultMoney;
            }

            public String getC_PayMode() {
                return c_PayMode;
            }

            public void setC_PayMode(String c_PayMode) {
                this.c_PayMode = c_PayMode;
            }

            public String getN_Deposit() {
                return n_Deposit;
            }

            public void setN_Deposit(String n_Deposit) {
                this.n_Deposit = n_Deposit;
            }

            public String getN_customerS() {
                return n_customerS;
            }

            public void setN_customerS(String n_customerS) {
                this.n_customerS = n_customerS;
            }

            public String getN_CustomersNow() {
                return n_CustomersNow;
            }

            public void setN_CustomersNow(String n_CustomersNow) {
                this.n_CustomersNow = n_CustomersNow;
            }

            public String getC_Team() {
                return c_Team;
            }

            public void setC_Team(String c_Team) {
                this.c_Team = c_Team;
            }

            public String getT_BeginTime() {
                return t_BeginTime;
            }

            public void setT_BeginTime(String t_BeginTime) {
                this.t_BeginTime = t_BeginTime;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
            }
        }
    }
}
