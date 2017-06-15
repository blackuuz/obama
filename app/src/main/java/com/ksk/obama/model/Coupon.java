package com.ksk.obama.model;

/**
 * Created by UUZ on 2017/6/14.
 */

import java.util.List;

/**
 * "result_status": "SUCCESS",
 * "result_data": [
 * {
 * "id": "8",
 * "i_GroupID": "1",
 * "i_CouponID": "18",
 * "c_Channel": "文",
 * "i_MemberID": "1516",
 * "i_BillID": "0",
 * "c_BillNO": "",
 * "t_UseTime": "2017-06-13 14:27:40.533",
 * "c_Code": "1516",
 * "t_SendTime": "2017-06-13 14:27:40.533",
 * "c_Status": "可用",
 * "c_MemberName": "",
 * "i_SendUserID": "0",
 * "c_SendUserName": "",
 * "i_SendShopID": "0",
 * "c_SendShopName": "",
 * "i_UseUserID": "0",
 * "c_UseUserName": "",
 * "i_UseShopID": "0",
 * "c_UseShopName": "",
 * "c_id": "18",
 * "c_Name": "韩_券",
 * "i_Type": "代金券",
 * "n_Money": "10.00",
 * "ROW_NUMBER": "1"
 * }
 * ]
 * }
 */

/**
 * result_errmsg
 */
public class Coupon {
    private String result_status;
    private String result_errmsg;//无优惠券返回此字段
    private List<ResultData> result_data;

    public String getResult_status() {
        return result_status;
    }

    public void setResult_status(String result_status) {
        this.result_status = result_status;
    }

    public String getResult_errmsg() {
        return result_errmsg;
    }

    public void setResult_errmsg(String result_errmsg) {
        this.result_errmsg = result_errmsg;
    }

    public List<ResultData> getResult_data() {
        return result_data;
    }

    public void setResult_data(List<ResultData> result_data) {
        this.result_data = result_data;
    }

    /**
     * "id": "8",
     * "i_GroupID": "1",
     * "i_CouponID": "18",
     * "c_Channel": "文",
     * "i_MemberID": "1516",
     * "i_BillID": "0",
     * "c_BillNO": "",
     * "t_UseTime": "2017-06-13 14:27:40.533",
     * "c_Code": "1516",
     * "t_SendTime": "2017-06-13 14:27:40.533",
     * "c_Status": "可用",
     * "c_MemberName": "",
     * "i_SendUserID": "0",
     * "c_SendUserName": "",
     * "i_SendShopID": "0",
     * "c_SendShopName": "",
     * "i_UseUserID": "0",
     * "c_UseUserName": "",
     * "i_UseShopID": "0",
     * "c_UseShopName": "",
     * "c_id": "18",
     * "c_Name": "韩_券",
     * "i_Type": "代金券",
     * "n_Money": "10.00",
     * "ROW_NUMBER": "1"
     */
   public class ResultData {
        private String id;
        private String i_GroupID;
        private String i_CouponID;
        private String c_Channel;
        private String i_MemberID;
        private String c_Status;
        private String c_BillNO;
        private String t_UseTime;
        private String c_Code;
        private String i_BillID;
        private String t_SendTime;
        private String c_MemberName;
        private String i_SendUserID;
        private String c_SendUserName;
        private String i_SendShopID;
        private String c_SendShopName;
        private String i_UseUserID;
        private String c_UseUserName;
        private String i_UseShopID;
        private String c_UseShopName;
        private String ROW_NUMBER;
        private String c_id;
        private String c_Name;
        private String i_Type;
        private String n_Money;

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

        public String getI_CouponID() {
            return i_CouponID;
        }

        public void setI_CouponID(String i_CouponID) {
            this.i_CouponID = i_CouponID;
        }

        public String getC_Channel() {
            return c_Channel;
        }

        public void setC_Channel(String c_Channel) {
            this.c_Channel = c_Channel;
        }

        public String getI_MemberID() {
            return i_MemberID;
        }

        public void setI_MemberID(String i_MemberID) {
            this.i_MemberID = i_MemberID;
        }

        public String getC_Status() {
            return c_Status;
        }

        public void setC_Status(String c_Status) {
            this.c_Status = c_Status;
        }

        public String getC_BillNO() {
            return c_BillNO;
        }

        public void setC_BillNO(String c_BillNO) {
            this.c_BillNO = c_BillNO;
        }

        public String getT_UseTime() {
            return t_UseTime;
        }

        public void setT_UseTime(String t_UseTime) {
            this.t_UseTime = t_UseTime;
        }

        public String getC_Code() {
            return c_Code;
        }

        public void setC_Code(String c_Code) {
            this.c_Code = c_Code;
        }

        public String getI_BillID() {
            return i_BillID;
        }

        public void setI_BillID(String i_BillID) {
            this.i_BillID = i_BillID;
        }

        public String getT_SendTime() {
            return t_SendTime;
        }

        public void setT_SendTime(String t_SendTime) {
            this.t_SendTime = t_SendTime;
        }

        public String getC_MemberName() {
            return c_MemberName;
        }

        public void setC_MemberName(String c_MemberName) {
            this.c_MemberName = c_MemberName;
        }

        public String getI_SendUserID() {
            return i_SendUserID;
        }

        public void setI_SendUserID(String i_SendUserID) {
            this.i_SendUserID = i_SendUserID;
        }

        public String getC_SendUserName() {
            return c_SendUserName;
        }

        public void setC_SendUserName(String c_SendUserName) {
            this.c_SendUserName = c_SendUserName;
        }

        public String getI_SendShopID() {
            return i_SendShopID;
        }

        public void setI_SendShopID(String i_SendShopID) {
            this.i_SendShopID = i_SendShopID;
        }

        public String getC_SendShopName() {
            return c_SendShopName;
        }

        public void setC_SendShopName(String c_SendShopName) {
            this.c_SendShopName = c_SendShopName;
        }

        public String getI_UseUserID() {
            return i_UseUserID;
        }

        public void setI_UseUserID(String i_UseUserID) {
            this.i_UseUserID = i_UseUserID;
        }

        public String getC_UseUserName() {
            return c_UseUserName;
        }

        public void setC_UseUserName(String c_UseUserName) {
            this.c_UseUserName = c_UseUserName;
        }

        public String getI_UseShopID() {
            return i_UseShopID;
        }

        public void setI_UseShopID(String i_UseShopID) {
            this.i_UseShopID = i_UseShopID;
        }

        public String getC_UseShopName() {
            return c_UseShopName;
        }

        public void setC_UseShopName(String c_UseShopName) {
            this.c_UseShopName = c_UseShopName;
        }

        public String getROW_NUMBER() {
            return ROW_NUMBER;
        }

        public void setROW_NUMBER(String ROW_NUMBER) {
            this.ROW_NUMBER = ROW_NUMBER;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getC_Name() {
            return c_Name;
        }

        public void setC_Name(String c_Name) {
            this.c_Name = c_Name;
        }

        public String getI_Type() {
            return i_Type;
        }

        public void setI_Type(String i_Type) {
            this.i_Type = i_Type;
        }

        public String getN_Money() {
            return n_Money;
        }

        public void setN_Money(String n_Money) {
            this.n_Money = n_Money;
        }
    }


}
