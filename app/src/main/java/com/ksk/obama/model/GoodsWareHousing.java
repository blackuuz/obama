package com.ksk.obama.model;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/19 11:11
 * 修改人：Administrator
 * 修改时间：2017/7/19 11:11
 * 修改备注：
 */
public class GoodsWareHousing {

    /**
     * result_stadus : SUCCESS
     * result_data : {"stock_s":[{"id":"0","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"18","n_Amount":"15.00","n_DePrice":"777.66","goodsid":"18","c_GoodsNO":"P00001","c_GoodsName":"草莓","c_GoodsClassify":"G","c_Pinyin":"CM","ROW_NUMBER":"1"},{"id":"21","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"26","n_Amount":"221.00","n_DePrice":".01","goodsid":"26","c_GoodsNO":"P00026","c_GoodsName":"测试商品1","c_GoodsClassify":"G","c_Pinyin":"CSSP1","ROW_NUMBER":"2"},{"id":"45","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"145","n_Amount":"5.00","n_DePrice":"50.00","goodsid":"145","c_GoodsNO":"P000144","c_GoodsName":"面部护理","c_GoodsClassify":"美容","c_Pinyin":"MBHL","ROW_NUMBER":"3"},{"id":"46","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"143","n_Amount":"6.00","n_DePrice":"65535.00","goodsid":"143","c_GoodsNO":"javalangNullPointerE","c_GoodsName":"javalangNullPointerExcept","c_GoodsClassify":"G","c_Pinyin":"javalangNullPointerExcept","ROW_NUMBER":"4"},{"id":"47","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"139","n_Amount":"1.00","n_DePrice":"25.00","goodsid":"139","c_GoodsNO":"P000139","c_GoodsName":"水蜡洗车","c_GoodsClassify":"默认分类","c_Pinyin":"SZXJ","ROW_NUMBER":"5"},{"id":"48","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"146","n_Amount":"1.00","n_DePrice":"3800000.00","goodsid":"146","c_GoodsNO":"P000146","c_GoodsName":"陆虎揽胜行政版","c_GoodsClassify":"默认分类","c_Pinyin":"LHLSXZB","ROW_NUMBER":"6"},{"id":"49","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"138","n_Amount":"1.00","n_DePrice":"100.00","goodsid":"138","c_GoodsNO":"P000138","c_GoodsName":"芳勀","c_GoodsClassify":"G","c_Pinyin":"FK","ROW_NUMBER":"7"},{"id":"50","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"137","n_Amount":"1.00","n_DePrice":"10.00","goodsid":"137","c_GoodsNO":"P000134","c_GoodsName":"桃子","c_GoodsClassify":"水果","c_Pinyin":"TZ","ROW_NUMBER":"8"},{"id":"58","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"27","n_Amount":"1.00","n_DePrice":"10.00","goodsid":"27","c_GoodsNO":"P00027","c_GoodsName":"打折积分","c_GoodsClassify":"G","c_Pinyin":"DZJF","ROW_NUMBER":"9"}],"type_s":[{"c_Value":"全部"},{"c_Value":"G","ROW_NUMBER":"1"},{"c_Value":"R","ROW_NUMBER":"2"},{"c_Value":"水果","ROW_NUMBER":"3"},{"c_Value":"剪发","ROW_NUMBER":"4"},{"c_Value":"测试品","ROW_NUMBER":"5"},{"c_Value":"干果","ROW_NUMBER":"6"},{"c_Value":"海鲜","ROW_NUMBER":"7"},{"c_Value":"水吧","ROW_NUMBER":"8"},{"c_Value":"饮料","ROW_NUMBER":"9"},{"c_Value":"热饮","ROW_NUMBER":"10"},{"c_Value":"零食","ROW_NUMBER":"11"},{"c_Value":"鲜花","ROW_NUMBER":"12"},{"c_Value":"美容","ROW_NUMBER":"13"}]}
     * result_errmsg :{当前无分类  当前无库存}
     *
     */

    private String result_stadus;
    private ResultDataBean result_data;
    private String result_errmsg;

    public String getResult_errmsg() {
        return result_errmsg;
    }

    public void setResult_errmsg(String result_errmsg) {
        this.result_errmsg = result_errmsg;
    }

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public static class ResultDataBean {
        private List<StockSBean> stock_s;
        private List<TypeSBean> type_s;

        public List<StockSBean> getStock_s() {
            return stock_s;
        }

        public void setStock_s(List<StockSBean> stock_s) {
            this.stock_s = stock_s;
        }

        public List<TypeSBean> getType_s() {
            return type_s;
        }

        public void setType_s(List<TypeSBean> type_s) {
            this.type_s = type_s;
        }

        public static class StockSBean {
            /**
             * id : 0
             * i_GroupID : 1
             * i_ShopID : 1
             * i_GoodsID : 18
             * n_Amount : 15.00
             * n_DePrice : 777.66
             * goodsid : 18
             * c_GoodsNO : P00001
             * c_GoodsName : 草莓
             * c_GoodsClassify : G
             * c_Pinyin : CM
             * ROW_NUMBER : 1
             */

            private String id;
            private String i_GroupID;
            private String i_ShopID;
            private String i_GoodsID;
            private String n_Amount;
            private float n_DePrice;
            private String goodsid;
            private String c_GoodsNO;
            private String c_GoodsName;
            private String c_GoodsClassify;
            private String c_Pinyin;
            private float num = 0;
            private float acPrice = -1;//修改后的金额
            private float totalMoney =-1;//总价格

            public float getAcPrice() {
                return acPrice;
            }

            public void setAcPrice(float acPrice) {
                this.acPrice = acPrice;
            }

            public float getTotalMoney() {
                return totalMoney;
            }

            public void setTotalMoney(float totalMoney) {
                this.totalMoney = totalMoney;
            }

            public float getNum() {
                return num;
            }

            public void setNum(float num) {
                this.num = num;
            }

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

            public String getI_GoodsID() {
                return i_GoodsID;
            }

            public void setI_GoodsID(String i_GoodsID) {
                this.i_GoodsID = i_GoodsID;
            }

            public String getN_Amount() {
                return n_Amount;
            }

            public void setN_Amount(String n_Amount) {
                this.n_Amount = n_Amount;
            }

            public float getN_DePrice() {
                return n_DePrice;
            }

            public void setN_DePrice(float n_DePrice) {
                this.n_DePrice = n_DePrice;
            }

            public String getGoodsid() {
                return goodsid;
            }

            public void setGoodsid(String goodsid) {
                this.goodsid = goodsid;
            }

            public String getC_GoodsNO() {
                return c_GoodsNO;
            }

            public void setC_GoodsNO(String c_GoodsNO) {
                this.c_GoodsNO = c_GoodsNO;
            }

            public String getC_GoodsName() {
                return c_GoodsName;
            }

            public void setC_GoodsName(String c_GoodsName) {
                this.c_GoodsName = c_GoodsName;
            }

            public String getC_GoodsClassify() {
                return c_GoodsClassify;
            }

            public void setC_GoodsClassify(String c_GoodsClassify) {
                this.c_GoodsClassify = c_GoodsClassify;
            }

            public String getC_Pinyin() {
                return c_Pinyin;
            }

            public void setC_Pinyin(String c_Pinyin) {
                this.c_Pinyin = c_Pinyin;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
            }
        }

        public static class TypeSBean {
            /**
             * c_Value : 全部
             * ROW_NUMBER : 1
             */

            private String c_Value;
            private String ROW_NUMBER;

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
        }
    }
}
