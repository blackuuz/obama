package com.ksk.obama.model;

import java.util.List;

/**
 * Created by djy on 2017/2/22.
 */

public class Technician {

    /**
     * result_data : {"Employee":[{"id":"1","i_GroupID":"1","i_ShopID":"1","c_JobNumber":"0101001","c_Name":"G","c_Job":"按摩","c_Tel":"1599999999","c_Remark":"","c_Status":"空闲","ROW_NUMBER":"1"}]}
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
        private List<EmployeeBean> Employee;

        public List<EmployeeBean> getEmployee() {
            return Employee;
        }

        public void setEmployee(List<EmployeeBean> Employee) {
            this.Employee = Employee;
        }

        public static class EmployeeBean {
            /**
             * id : 1
             * i_GroupID : 1
             * i_ShopID : 1
             * c_JobNumber : 0101001
             * c_Name : G
             * c_Job : 按摩
             * c_Tel : 1599999999
             * c_Remark :
             * c_Status : 空闲
             * ROW_NUMBER : 1
             */

            private String id;
            private String i_GroupID;
            private String i_ShopID;
            private String c_JobNumber;
            private String c_Name;
            private String c_Job;
            private String c_Tel;
            private String c_Remark;
            private String c_Status;
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

            public String getC_JobNumber() {
                return c_JobNumber;
            }

            public void setC_JobNumber(String c_JobNumber) {
                this.c_JobNumber = c_JobNumber;
            }

            public String getC_Name() {
                return c_Name;
            }

            public void setC_Name(String c_Name) {
                this.c_Name = c_Name;
            }

            public String getC_Job() {
                return c_Job;
            }

            public void setC_Job(String c_Job) {
                this.c_Job = c_Job;
            }

            public String getC_Tel() {
                return c_Tel;
            }

            public void setC_Tel(String c_Tel) {
                this.c_Tel = c_Tel;
            }

            public String getC_Remark() {
                return c_Remark;
            }

            public void setC_Remark(String c_Remark) {
                this.c_Remark = c_Remark;
            }

            public String getC_Status() {
                return c_Status;
            }

            public void setC_Status(String c_Status) {
                this.c_Status = c_Status;
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
